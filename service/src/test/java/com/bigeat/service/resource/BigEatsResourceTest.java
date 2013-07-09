package com.bigeat.service.resource;

import static com.bigeat.service.test.BigEatRequestTestUtil.bigEat;
import static com.bigeat.service.test.BigEatTestUtil.BIGEATS_URI;
import static com.bigeat.service.test.BigEatTestUtil.checkDefinition;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.api.image.ImageDefinition;
import com.bigeat.service.api.image.ImageRequest;
import com.bigeat.service.api.image.ImageSize;
import com.bigeat.service.api.image.UrlImageType;
import com.bigeat.service.exception.ErrorValue;
import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource.Builder;
import com.yammer.dropwizard.validation.InvalidEntityException;

/**
 * @author mattt
 * 
 */
public final class BigEatsResourceTest extends BaseResourceTest {

  private final BigEatRequest request;

  public BigEatsResourceTest() {
    request = bigEat();
  }

  @Test
  public void createBigEatTest() throws MalformedURLException {

    final ClientResponse response = createBigEat(request);

    assertEquals(response.getStatus(), Status.CREATED.getStatusCode());
    assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON + "; charset=UTF-8");

    final List<String> list = response.getHeaders().get("Location");
    Assert.assertEquals(list.size(), 1);

    final String location = list.get(0);
    assertNotNull(location);

    // is url
    new URL(location);

    // can dereference location
    final ClientResponse locResponse = client().resource(location).head();
    assertEquals(locResponse.getStatus(), Status.OK.getStatusCode());

    // check definition
    final BigEatDefinition definition = response.getEntity(BigEatDefinition.class);
    assertNotNull(definition);

    checkDefinition(request, definition);

    // check images
    final Map<ImageSize, ImageDefinition> images = definition.getImages();
    checkImages(images);

  }

  private ClientResponse createBigEat(final BigEatRequest request) {

    final Builder resource = client().resource(BIGEATS_URI).getRequestBuilder();
    resource.type(MediaType.APPLICATION_JSON);
    resource.accept(MediaType.APPLICATION_JSON);

    return resource.post(ClientResponse.class, request);
  }

  @Test(expectedExceptions = InvalidEntityException.class)
  public void createBigEatMissingItemTest() {
    final BigEatRequest missingStuff = new BigEatRequest.Builder(request).item(null).build(false);
    createBigEat(missingStuff);
  }

  @Test(expectedExceptions = InvalidEntityException.class)
  public void createBigEatMissingVenueTest() {
    final BigEatRequest missingStuff = new BigEatRequest.Builder(request).venue(null).build(false);
    createBigEat(missingStuff);
  }

  @Test(expectedExceptions = InvalidEntityException.class)
  public void createBigEatMissingImagesTest() {
    final BigEatRequest missingStuff = new BigEatRequest.Builder(request).images(null).build(false);
    createBigEat(missingStuff);
  }

  @Test(expectedExceptions = InvalidEntityException.class)
  public void createBigEatMissingItemNumTest() {
    final BigEatRequest missingStuff =
        new BigEatRequest.Builder(request).itemNum(null).build(false);
    createBigEat(missingStuff);
  }

  @Test
  public void createBigEatNotAnImageTest() throws IOException {

    final File file = File.createTempFile("bogus", ".tmp");

    try {

      final ImageRequest bogusImage =
          new UrlImageType.Builder().image(file.toURI().toURL()).build();

      final Map<ImageSize, ImageRequest> images =
          ImmutableMap.of(ImageSize.small, bogusImage, ImageSize.large, bogusImage);

      final BigEatRequest request =
          new BigEatRequest.Builder(this.request).images(images).build(false);

      final ClientResponse response = createBigEat(request);
      assertEquals(response.getStatus(), Status.BAD_REQUEST.getStatusCode());

      final ErrorValue errorValue = response.getEntity(ErrorValue.class);
      assertNotNull(errorValue);

      assertNotNull(errorValue.getCode());
      assertNotNull(errorValue.getMessage());

    } finally {
      file.delete();
    }
  }

  private void checkImages(final Map<ImageSize, ImageDefinition> images) {
    for (final Map.Entry<ImageSize, ImageDefinition> entry : images.entrySet()) {

      final URI imageUri = entry.getValue().getEndpoint();
      final ClientResponse locResponse = client().resource(imageUri).head();

      assertEquals(locResponse.getStatus(), Status.OK.getStatusCode());
    }
  }

  @Test(dependsOnMethods = {"createBigEatTest"})
  public void getBigEatsTest() {

    final Collection<BigEatDefinition> bigEats = getBigEats();

    assertNotNull(bigEats);
    assertEquals(bigEats.size(), 1);

    for (final BigEatDefinition definition : bigEats) {
      checkDefinition(request, definition);
    }

  }

  private Collection<BigEatDefinition> getBigEats() {

    final Builder resource = client().resource(BIGEATS_URI).getRequestBuilder();
    resource.accept(MediaType.APPLICATION_JSON);
    final ClientResponse response = resource.get(ClientResponse.class);

    assertEquals(response.getStatus(), Status.OK.getStatusCode());
    assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON + "; charset=UTF-8");

    final Collection<BigEatDefinition> bigEats =
        response.getEntity(new GenericType<Collection<BigEatDefinition>>() {});

    return bigEats;
  }

}
