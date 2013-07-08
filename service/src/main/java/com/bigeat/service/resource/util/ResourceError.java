package com.bigeat.service.resource.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.bigeat.service.exception.ErrorValue;

/**
 * @author mattt
 * 
 */
public final class ResourceError {

  private ResourceError() {
    throw new AssertionError();
  }

  public static WebApplicationException wrapServiceException(final Throwable e, final Status status) {
    final Response response =
        Response.status(status).entity(buildServiceErrorValue(e, status)).build();

    return new WebApplicationException(e, response);
  }

  public static WebApplicationException wrapServiceException(final Throwable e) {
    return wrapServiceException(e, Status.INTERNAL_SERVER_ERROR);
  }

  public static Response buildErrorResponse(final Throwable e, final Status status) {
    return Response.status(status).entity(buildServiceErrorValue(e, status)).build();
  }

  static ErrorValue buildServiceErrorValue(final Throwable e, final Status status) {
    return new ErrorValue(status.getStatusCode(), e.getLocalizedMessage());
  }

}
