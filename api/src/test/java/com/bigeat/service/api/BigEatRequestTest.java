package com.bigeat.service.api;

import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;

import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatRequest.Builder;
import com.google.common.collect.Maps;

/**
 * @author mattt
 * 
 */
public class BigEatRequestTest {

  public BigEatRequestTest() {}

  @Test
  public void bigEatRequestSerializeTest() throws IOException, ParseException {
    final BigEatRequest bigEat = bigEat("scrumptious", "nopa");
    assertThat(asJson(bigEat)).isEqualTo(jsonFixture("fixtures/bigeat_request.json"));
  }

  @Test
  public void bigEatRequestSerializeUnicodeTest() throws IOException, ParseException {
    final BigEatRequest bigEat = bigEat("Pomme d’amour", "Cassava Bakery and Café");
    assertThat(asJson(bigEat)).isEqualTo(jsonFixture("fixtures/bigeat_unicode_request.json"));
  }

  private BigEatRequest bigEat(final String itemName, final String venueName)
      throws ParseException, MalformedURLException {

    final Builder builder = new BigEatRequest.Builder();

    builder.item(itemName);
    builder.itemNum(99);

    builder.images(images());
    builder.venue(venue(venueName));

    return builder.build();
  }

  @Test
  public void bigEatRequestDeserializeTest() throws Exception {
    final BigEatRequest bigEat = bigEat("scrumptious", "nopa");
    assertThat(fromJson(jsonFixture("fixtures/bigeat_request.json"), BigEatRequest.class))
        .isEqualTo(bigEat);
  }

  @Test
  public void bigEatRequestDeserializeUnicodeTest() throws Exception {
    final BigEatRequest bigEat = bigEat("Pomme d’amour", "Cassava Bakery and Café");

    final BigEatRequest eatRequest =
        fromJson(jsonFixture("fixtures/bigeat_unicode_request.json"), BigEatRequest.class);
    assertThat(eatRequest).isEqualTo(bigEat);
  }

  private static Map<ImageSize, ImageRequest> images() throws MalformedURLException {

    final Map<ImageSize, ImageRequest> images = Maps.newTreeMap();

    {
      final UrlImageType image =
          new UrlImageType.Builder().image(new URL("http://image.small.png")).build();
      images.put(ImageSize.small, image);
    }

    {
      final UrlImageType image =
          new UrlImageType.Builder().image(new URL("http://image.large.png")).build();
      images.put(ImageSize.large, image);
    }

    return images;
  }

  private static Venue venue(final String venueName) throws MalformedURLException {
    return new Venue.Builder().name(venueName).location(location()).contact(contact())
        .website(new URL("http://nopasf.com/")).build();
  }

  private static Contact contact() {
    return new Contact.Builder().phoneNumber("415-864-8643").build();
  }

  private static Location location() {
    return new Location.Builder().address("560 divisadero at hayes").build();
  }

}
