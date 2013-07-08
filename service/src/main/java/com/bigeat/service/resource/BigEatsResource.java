package com.bigeat.service.resource;

import java.net.URI;
import java.util.Collection;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.core.BigEatService;
import com.bigeat.service.exception.BigEatRequestException;
import com.bigeat.service.exception.BigEatServiceException;
import com.bigeat.service.resource.util.ResourceError;
import com.yammer.metrics.annotation.Timed;

/**
 * @author mattt
 * 
 */
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public final class BigEatsResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(BigEatsResource.class);

  private final BigEatService service;

  public BigEatsResource(final BigEatService service) {
    this.service = service;
  }

  @GET
  @Timed
  public Collection<BigEatDefinition> getBigEats(@Context final UriInfo uriInfo) {
    try {
      return service.getBigEats();
    } catch (final BigEatServiceException e) {

      LOGGER.error("error obtaining big eats", e);
      throw ResourceError.wrapServiceException(e);
    }
  }

  @POST
  @Timed
  public Response createBigEat(@Valid final BigEatRequest bigEatRequest,
      @Context final UriInfo uriInfo) {
    if (bigEatRequest == null) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    final URI pathUri = URI.create(uriInfo.getRequestUri().getPath());

    try {
      final BigEatDefinition bigEat = service.createBigEat(bigEatRequest, pathUri);
      return buildCreateResponse(bigEat, uriInfo);
    } catch (final BigEatServiceException e) {

      LOGGER.error("error creating big eat", e);
      throw ResourceError.wrapServiceException(e);
    } catch (final BigEatRequestException e) {
      throw ResourceError.wrapServiceException(e, Status.BAD_REQUEST);
    } catch (final Throwable e) {
      LOGGER.error("error creating big eat", e);
      throw ResourceError.wrapServiceException(e);
    }

  }

  @Path("{bigEatId}")
  public BigEatResource getBigEat() {
    return new BigEatResource(service);
  }

  private Response buildCreateResponse(final BigEatDefinition bigEat, final UriInfo uriInfo) {

    final URI baseUri = uriInfo.getBaseUri();
    final URI resourceUri =
        UriBuilder.fromUri(uriInfo.getRequestUri()).path("{bigEatId}").build(bigEat.getId());

    final URI created = baseUri.resolve(resourceUri);
    return Response.created(created).entity(bigEat).build();
  }

}
