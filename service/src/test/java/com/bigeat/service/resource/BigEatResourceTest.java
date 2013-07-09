package com.bigeat.service.resource;

import static com.bigeat.service.test.BigEatRequestTestUtil.bigEat;
import static com.bigeat.service.test.BigEatTestUtil.BIGEATS_URI;
import static com.bigeat.service.test.BigEatTestUtil.checkDefinition;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.api.image.ImageDefinition;
import com.bigeat.service.api.image.ImageSize;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * @author mattt
 * 
 */
public final class BigEatResourceTest extends BaseResourceTest {

  private BigEatDefinition definition;
  private BigEatRequest request;

  private String bigEatResource;

  private void createBigEat() {

    request = bigEat();

    final Builder resource = client().resource(BIGEATS_URI).getRequestBuilder();
    resource.type(MediaType.APPLICATION_JSON);
    resource.accept(MediaType.APPLICATION_JSON);
    final ClientResponse response = resource.post(ClientResponse.class, request);

    final List<String> list = response.getHeaders().get("Location");
    Assert.assertEquals(list.size(), 1);

    bigEatResource = list.get(0);
  }

  @Test
  public void getBigEatTest() {
    createBigEat();

    final ClientResponse response = getBigEat();

    assertEquals(response.getStatus(), Status.OK.getStatusCode());
    assertEquals(response.getType().toString(), MediaType.APPLICATION_JSON + "; charset=UTF-8");

    final BigEatDefinition definition = response.getEntity(BigEatDefinition.class);
    assertNotNull(definition);

    this.definition = definition;
    checkDefinition(request, definition);
  }

  @Test
  public void getBigEatNotFoundTest() {

    final URI bigeatUri = bigEatUri("foo");
    final Builder resource = client().resource(bigeatUri).getRequestBuilder();
    resource.accept(MediaType.APPLICATION_JSON);
    final ClientResponse response = resource.get(ClientResponse.class);

    assertEquals(response.getStatus(), Status.NOT_FOUND.getStatusCode());
  }

  @Test(dependsOnMethods = {"getBigEatTest"})
  public void deleteBigEatTest() {

    final String id = definition.getId();
    final URI bigeatUri = bigEatUri(id);

    final Builder resource = client().resource(bigeatUri).getRequestBuilder();
    resource.accept(MediaType.APPLICATION_JSON);
    final ClientResponse response = resource.delete(ClientResponse.class);
    assertEquals(response.getStatus(), Status.NO_CONTENT.getStatusCode());

    // empty
    final Collection<BigEatDefinition> bigEats = getBigEats();
    assertEquals(bigEats.size(), 0);

    // 404
    final ClientResponse bigEatResponse = getBigEat();
    assertEquals(bigEatResponse.getStatus(), Status.NOT_FOUND.getStatusCode());

    // image 404s
    final Map<ImageSize, ImageDefinition> images = definition.getImages();
    for (final Map.Entry<ImageSize, ImageDefinition> entry : images.entrySet()) {

      final URI imageUri = entry.getValue().getEndpoint();
      final ClientResponse locResponse = client().resource(imageUri).head();

      assertEquals(locResponse.getStatus(), Status.NOT_FOUND.getStatusCode());
    }
  }

  private ClientResponse getBigEat() {

    final Builder resource = client().resource(bigEatResource).getRequestBuilder();
    resource.accept(MediaType.APPLICATION_JSON);

    return resource.get(ClientResponse.class);

  }

  private Collection<BigEatDefinition> getBigEats() {
    final Builder resource = client().resource(BIGEATS_URI).getRequestBuilder();
    resource.accept(MediaType.APPLICATION_JSON);
    final ClientResponse response = resource.get(ClientResponse.class);

    return response.getEntity(new GenericType<Collection<BigEatDefinition>>() {});
  }

  private URI bigEatUri(final String id) {
    return UriBuilder.fromUri(BIGEATS_URI).path("{bigEatId}").build(id);
  }
}
