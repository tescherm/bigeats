package com.bigeat.service.test;

import java.net.MalformedURLException;
import java.net.URL;

import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.api.BigEatRequest.Builder;
import com.bigeat.service.api.Image;
import com.bigeat.service.api.Venue;
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

    builder.image(image());
    builder.venue(venue(venueName));

    return builder.build();
  }

  private static Image image() {

    // file url
    final URL uploadImage = Resources.getResource("fixtures/upload_image.jpg");
    return new Image.Builder().smallUrl(uploadImage).largeUrl(uploadImage).build();
  }

  private static Venue venue(final String venueName) {
    try {
      return new Venue.Builder().name(venueName).address("560 divisadero at hayes")
          .phoneNumber("415-864-8643").website(new URL("http://nopasf.com/")).build();
    } catch (final MalformedURLException e) {
      throw new IllegalStateException(e);
    }
  }
}
