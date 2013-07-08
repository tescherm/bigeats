package com.bigeat.service.test;

import java.net.MalformedURLException;
import java.net.URL;

import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.api.BigEatRequest.Builder;
import com.bigeat.service.api.Contact;
import com.bigeat.service.api.ImageRequest;
import com.bigeat.service.api.ImageSize;
import com.bigeat.service.api.Location;
import com.bigeat.service.api.UrlImageType;
import com.bigeat.service.api.Venue;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;

/**
 * @author mattt
 * 
 */
public final class BigEatRequestTestUtil {

  private BigEatRequestTestUtil() {
    throw new AssertionError();
  }

  public static BigEatRequest bigEat() {

    return bigEat("scrumptious", "nopa");
  }

  public static BigEatRequest bigEat(final String itemName, final String venueName) {

    final Builder builder = new BigEatRequest.Builder();

    builder.item(itemName);
    builder.itemNum(99);

    builder.images(images());
    builder.venue(venue(venueName));

    return builder.build();
  }

  private static ImmutableMap<ImageSize, ImageRequest> images() {

    // file url
    final URL uploadImage = Resources.getResource("fixtures/upload_image.jpg");

    final ImageRequest small = new UrlImageType.Builder().image(uploadImage).build();
    final ImageRequest large = new UrlImageType.Builder().image(uploadImage).build();

    return ImmutableMap.of(ImageSize.small, small, ImageSize.large, large);
  }

  private static Venue venue(final String venueName) {
    try {
      return new Venue.Builder().name(venueName).location(location()).contact(contact())
          .website(new URL("http://nopasf.com/")).build();
    } catch (final MalformedURLException e) {
      throw new IllegalStateException(e);
    }
  }

  private static Contact contact() {
    return new Contact.Builder().phoneNumber("415-864-8643").build();
  }

  private static Location location() {
    return new Location.Builder().address("560 divisadero at hayes").build();
  }
}
