package com.bigeat.service.api;

import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatRequest.Builder;

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

    builder.image(image());
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

  private Image image() throws MalformedURLException {
    return new Image.Builder().smallUrl(new URL("http://image.small.png"))
        .largeUrl(new URL("http://image.large.png")).build();
  }

  private Venue venue(final String venueName) throws MalformedURLException {
    return new Venue.Builder().name(venueName).address("560 divisadero at hayes")
        .phoneNumber("415-864-8643").website(new URL("http://nopasf.com/")).build();
  }

}
