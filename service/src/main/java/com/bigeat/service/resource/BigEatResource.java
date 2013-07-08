package com.bigeat.service.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.core.BigEatService;
import com.bigeat.service.exception.BigEatNotFoundException;
import com.bigeat.service.exception.BigEatServiceException;
import com.bigeat.service.resource.util.ResourceError;
import com.yammer.dropwizard.jersey.caching.CacheControl;
import com.yammer.metrics.annotation.Timed;

/**
 * @author mattt
 * 
 */
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public final class BigEatResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(BigEatResource.class);

  private final BigEatService service;

  public BigEatResource(final BigEatService service) {
    checkNotNull(service);
    this.service = service;
  }

  @GET
  @Timed
  @CacheControl(maxAge = 24, maxAgeUnit = TimeUnit.HOURS)
  public Response getBigEat(final @PathParam("bigEatId") String bigEatId) {
    if (bigEatId == null) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    try {

      final BigEatDefinition bigEat = service.getBigEat(bigEatId);
      final ResponseBuilder builder = Response.ok(bigEat);

      final Date modified = bigEat.getModified();
      builder.lastModified(modified);

      return builder.build();

    } catch (final BigEatNotFoundException e) {
      throw ResourceError.wrapServiceException(e, Status.NOT_FOUND);
    } catch (final BigEatServiceException e) {
      LOGGER.error("error obtaining big eat", e);
      throw ResourceError.wrapServiceException(e);
    } catch (final Throwable e) {
      LOGGER.error("error obtaining big eat", e);
      throw ResourceError.wrapServiceException(e);
    }

  }

  @DELETE
  public Response deleteBigEat(final @PathParam("bigEatId") String bigEatId) {

    try {
      service.deleteBigEat(bigEatId);

      final ResponseBuilder builder = Response.noContent();
      return builder.build();

    } catch (final BigEatNotFoundException e) {
      throw ResourceError.wrapServiceException(e, Status.NOT_FOUND);
    } catch (final BigEatServiceException e) {
      LOGGER.error("error deleting big eat image", e);
      throw ResourceError.wrapServiceException(e);
    } catch (final Throwable e) {
      LOGGER.error("error deleting big eat image", e);
      throw ResourceError.wrapServiceException(e);
    }
  }

  @Path("image")
  public BigEatImageResource getImageResource() {
    return new BigEatImageResource(service);
  }

}
