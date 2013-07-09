package com.bigeat.service.api;

import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatDefinition.Builder;
import com.bigeat.service.api.image.ImageDefinition;
import com.bigeat.service.api.image.ImageSize;
import com.bigeat.service.api.venue.Contact;
import com.bigeat.service.api.venue.Location;
import com.bigeat.service.api.venue.Venue;
import com.google.common.collect.Maps;

/**
 * @author mattt
 *
 */
public class BigEatDefinitionTest {

  private final DateFormat format;

  public BigEatDefinitionTest() {
    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  }

  @Test
  public void bigEatDefinitionSerializeTest() throws ParseException, URISyntaxException,
      IOException {
    final BigEatDefinition bigEat = bigEat();
    assertThat(asJson(bigEat)).isEqualTo(jsonFixture("fixtures/bigeat_definition.json"));
  }

  private BigEatDefinition bigEat() throws ParseException, URISyntaxException,
      MalformedURLException {

    final Date now = format.parse("2012-05-04 00:00:00.0");

    final Builder builder = new BigEatDefinition.Builder();
    builder.id("id-1337");

    builder.item("scrumptious");
    builder.itemNum(99);

    builder.created(now);
    builder.modified(now);

    builder.images(images());
    builder.venue(venue());

    return builder.build();
  }

  @Test
  public void bigEatDefinitionDeserializeTest() throws Exception {
    final BigEatDefinition bigEat = bigEat();
    assertThat(fromJson(jsonFixture("fixtures/bigeat_definition.json"), BigEatDefinition.class))
        .isEqualTo(bigEat);
  }

  private Map<ImageSize, ImageDefinition> images() throws URISyntaxException {

    final Map<ImageSize, ImageDefinition> images = Maps.newTreeMap();

    images.put(ImageSize.small,
        new ImageDefinition.Builder().id("1337").endpoint(new URI("/image/1337")).build());
    images.put(ImageSize.large,
        new ImageDefinition.Builder().id("42").endpoint(new URI("/image/42")).build());

    return images;
  }

  private static Venue venue() throws MalformedURLException {

    return new Venue.Builder().name("nopa").location(location()).contact(contact())
        .website(new URL("http://nopasf.com/")).build();
  }

  private static Contact contact() {
    return new Contact.Builder().phoneNumber("415-864-8643").build();
  }

  private static Location location() {
    return new Location.Builder().address("560 divisadero at hayes").build();
  }
}
