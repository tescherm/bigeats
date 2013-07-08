package com.bigeat.service.resource;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * @author mattt
 * 
 */
public final class RootResourceTest extends BaseResourceTest {

  @Test
  public void rootResourceGetTest() {
    final ClientResponse response = client().resource("/v1").get(ClientResponse.class);
    assertEquals(response.getStatus(), Status.METHOD_NOT_ALLOWED.getStatusCode());
  }

  @Test
  public void rootResourceOptionsTest() {
    final ClientResponse response = client().resource("/v1").options(ClientResponse.class);
    // TODO could describe sub-resources here
    assertEquals(response.getStatus(), Status.NO_CONTENT.getStatusCode());
  }

}
