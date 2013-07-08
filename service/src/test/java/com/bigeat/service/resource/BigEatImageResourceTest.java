package com.bigeat.service.resource;

import static com.bigeat.service.test.BigEatRequestTestUtil.bigEat;
import static com.bigeat.service.test.BigEatTestUtil.BIGEATS_URI;
import static com.bigeat.service.test.BigEatTestUtil.bigEatImageUri;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.api.ImageDefinition;
import com.bigeat.service.api.ImageType;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource.Builder;

public final class BigEatImageResourceTest extends BaseResourceTest {

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
  public void getBigEatImageTest() {
    createBigEat();

    final ClientResponse response = getBigEat();

    final BigEatDefinition definition = response.getEntity(BigEatDefinition.class);
    assertNotNull(definition);

    final Map<ImageType, ImageDefinition> images = definition.getImages();
    for (final Map.Entry<ImageType, ImageDefinition> entry : images.entrySet()) {

      final URI imageUri = entry.getValue().getEndpoint();
      final ClientResponse locResponse = client().resource(imageUri).get(ClientResponse.class);

      assertEquals(locResponse.getStatus(), Status.OK.getStatusCode());
      assertNotNull(locResponse.getEntityTag());
      assertNotNull(locResponse.getLastModified());
      assertNotNull(locResponse.getType());
      assertTrue(locResponse.getLength() > 0);
    }
  }

  @Test
  public void getBigEatImageNotFoundTest() {

    final URI bigeatUri = bigEatImageUri("foo", "bar");
    final Builder resource = client().resource(bigeatUri).getRequestBuilder();
    final ClientResponse response = resource.get(ClientResponse.class);

    assertEquals(response.getStatus(), Status.NOT_FOUND.getStatusCode());
  }

  private ClientResponse getBigEat() {

    final Builder resource = client().resource(bigEatResource).getRequestBuilder();
    resource.accept(MediaType.APPLICATION_JSON);

    return resource.get(ClientResponse.class);

  }
}
