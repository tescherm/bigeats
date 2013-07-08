package com.bigeat.scraper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.api.BigEatRequest.Builder;
import com.bigeat.service.api.Contact;
import com.bigeat.service.api.ImageRequest;
import com.bigeat.service.api.ImageSize;
import com.bigeat.service.api.Location;
import com.bigeat.service.api.UrlImageType;
import com.bigeat.service.api.Venue;
import com.bigeat.service.client.BigEatServiceClient;
import com.bigeat.service.client.exception.BigEatClientException;
import com.bigeat.service.client.http.HttpBigEatServiceClient;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author mattt
 * 
 */
public final class BigEatScraper {

  private static final Logger LOGGER = LoggerFactory.getLogger(BigEatScraper.class);

  private static final String PATTERN =
      "^(.+) (at|from) (<a ?href=\"(.+)\">(.+)</a>|(.+)).*\\. (((.*)(\\.|,)|(.*)))? ?([0-9\\(\\)\\- ]*)$";
  private static final Pattern ITEM_INFO_PATTERN = Pattern.compile(PATTERN);

  // in millis
  private static final int GET_BIGEATS_TIMEOUT = 5000;

  private final URL bigeatUrl;

  private final BigEatServiceClient serviceClient;

  public BigEatScraper(final URI serviceUrl, final URL bigeatUrl) {

    checkNotNull(serviceUrl);
    checkNotNull(bigeatUrl);

    this.bigeatUrl = bigeatUrl;
    this.serviceClient = new HttpBigEatServiceClient(serviceUrl);
  }

  public void scrape() {

    final Document document = readDocument();
    final Element carouselElement = document.getElementById("galleria");
    checkNotNull(carouselElement);

    final Elements bigEatElements = carouselElement.select("a[id^=galleria-image]");
    checkNotNull(bigEatElements);
    checkState(bigEatElements.size() == 100);

    processBigEatRequestElements(bigEatElements);

  }

  private void processBigEatRequestElements(final Elements bigEatElements) {

    final List<Element> failedElements = Lists.newLinkedList();

    final ListeningExecutorService executorService =
        MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    for (final Element bigEatElement : bigEatElements) {
      final ListenableFuture<BigEatDefinition> future =
          submitBigEatRequest(executorService, bigEatElement);
      Futures.addCallback(future, new FutureCallback<BigEatDefinition>() {

        @Override
        public void onSuccess(final BigEatDefinition result) {
          LOGGER.info("created big eat: {}", result);
        }

        @Override
        public void onFailure(final Throwable t) {
          LOGGER.error("could not create big eat for element: " + bigEatElement, t);
          failedElements.add(bigEatElement);
        }
      });
    }

    executorService.shutdown();
    waitForCompletion(executorService);

    reportErrors(failedElements);
  }

  private void reportErrors(final List<Element> failedElements) {
    LOGGER.info("could not submit the following " + failedElements.size() + " big eats:");
    for (final Element element : failedElements) {
      LOGGER.info(element.toString());
    }
  }

  private void waitForCompletion(final ListeningExecutorService executorService) {
    try {
      // should not take any longer
      executorService.awaitTermination(3, TimeUnit.MINUTES);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("interrupted while waiting for big eats to be submitted", e);
    }
  }

  private ListenableFuture<BigEatDefinition> submitBigEatRequest(
      final ListeningExecutorService executorService, final Element bigEatElement) {

    return executorService.submit(new Callable<BigEatDefinition>() {
      @Override
      public BigEatDefinition call() throws MalformedURLException, BigEatClientException {
        final BigEatRequest request = parseBigEatRequest(bigEatElement);
        return serviceClient.createBigEat(request);
      }
    });

  }

  private BigEatRequest parseBigEatRequest(final Element bigEatElement)
      throws MalformedURLException {

    checkState(bigEatElement.hasAttr("href"));

    final Builder builder = new BigEatRequest.Builder();

    final Elements imageElements = bigEatElement.getElementsByTag("img");
    checkState(imageElements.size() == 1);

    final Element imageElement = imageElements.get(0);
    final String itemInfo = checkNotNull(imageElement.attr("title"));

    final int itemNum = Integer.valueOf(imageElement.attr("name"));
    builder.itemNum(itemNum);

    buildImage(builder, bigEatElement);
    parseItemInfo(builder, itemInfo);

    return builder.build();
  }

  private void buildImage(final Builder builder, final Element bigEatElement)
      throws MalformedURLException {

    final String smallHref = bigEatElement.attr("href");
    final URL smallImageUrl = new URL(smallHref);

    // bit of a hack to get large image url
    final URL largeImageUrl =
        new URL(smallHref.replace("mobile_imagegallery_main", "blog_imagegallery_main"));

    final ImageRequest small = new UrlImageType.Builder().image(smallImageUrl).build();
    final ImageRequest large = new UrlImageType.Builder().image(largeImageUrl).build();

    builder.images(ImmutableMap.of(ImageSize.small, small, ImageSize.large, large));

  }

  private void parseItemInfo(final Builder builder, final String itemInfo)
      throws MalformedURLException {

    final Matcher matcher = ITEM_INFO_PATTERN.matcher(itemInfo);

    LOGGER.info("info: {}", itemInfo);

    if (matcher.matches()) {
      buildMatched(builder, matcher);
    }
  }

  private void buildMatched(final Builder builder, final Matcher matcher)
      throws MalformedURLException {

    final String itemName = checkNotNull(matcher.group(1));
    builder.item(itemName);
    buildVenue(builder, matcher);
  }

  private void buildVenue(final Builder builder, final Matcher matcher)
      throws MalformedURLException {

    final String webpageString = matcher.group(4);
    final URL webpage = webpageString == null ? null : new URL(webpageString);

    String venueName = matcher.group(5);
    if (venueName == null) {
      venueName = matcher.group(6);
    }

    if (venueName == null) {
      throw new IllegalStateException("could not parse venue name");
    }

    String address = matcher.group(9);
    if (address == null) {
      address = matcher.group(11);
    }

    if (address == null) {
      address = "";
    }

    final String phoneNumber = matcher.group(12);

    final Venue venue =
        new Venue.Builder().name(venueName).website(webpage).location(location(address))
            .contact(contact(phoneNumber)).build();
    builder.venue(venue);
  }

  private Location location(final String address) {
    return new Location.Builder().address(address).build();
  }

  private Contact contact(final String phoneNumber) {
    return new Contact.Builder().phoneNumber(phoneNumber).build();
  }

  private Document readDocument() {
    try {
      final Connection connection = Jsoup.connect(bigeatUrl.toString());
      return connection.timeout(GET_BIGEATS_TIMEOUT).get();
    } catch (final IOException e) {
      throw new IllegalStateException("unable to read 7x7 website: " + bigeatUrl, e);
    }
  }
}
