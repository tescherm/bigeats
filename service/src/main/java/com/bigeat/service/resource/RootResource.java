package com.bigeat.service.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.ws.rs.Path;

import com.bigeat.service.core.BigEatService;

/**
 * @author mattt
 * 
 */
@Path("/v1/")
public final class RootResource {

  private final BigEatService service;

  public RootResource(final BigEatService service) {
    checkNotNull(service);
    this.service = service;
  }

  @Path("bigeats")
  public BigEatsResource getImportsResource() {
    return new BigEatsResource(service);
  }
}
