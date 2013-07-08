package com.bigeat.service.resource;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigeat.service.api.BigEatImage;
import com.bigeat.service.core.BigEatService;
import com.bigeat.service.exception.BigEatImageNotFoundException;
import com.bigeat.service.exception.BigEatServiceException;
import com.bigeat.service.resource.util.ResourceError;
import com.yammer.dropwizard.jersey.caching.CacheControl;

@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public final class BigEatImageResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(BigEatImageResource.class);

  private final BigEatService service;

  public BigEatImageResource(final BigEatService service) {
    this.service = service;
  }

  @GET
  @Path("{imageId}")
  @CacheControl(maxAge = 24, maxAgeUnit = TimeUnit.HOURS)
  public Response getBigEatImage(final @PathParam("imageId") String imageId) {
    if (imageId == null) {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    try {

      final BigEatImage image = service.getBigEatImage(imageId);

      final ResponseBuilder builder = Response.ok(image.getContents());

      builder.type(image.getType());
      builder.header(HttpHeaders.CONTENT_LENGTH, image.getSize());
      builder.tag(image.getHash());
      builder.lastModified(image.getModified());

      return builder.build();

    } catch (final BigEatImageNotFoundException e) {
      throw ResourceError.wrapServiceException(e, Status.NOT_FOUND);
    } catch (final BigEatServiceException e) {
      LOGGER.error("error obtaining big eat image", e);
      throw ResourceError.wrapServiceException(e);
    } catch (final Throwable e) {
      LOGGER.error("error obtaining big eat image", e);
      throw ResourceError.wrapServiceException(e);
    }

  }
}
