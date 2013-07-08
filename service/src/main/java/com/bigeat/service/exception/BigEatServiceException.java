package com.bigeat.service.exception;

/**
 * @author mattt
 * 
 */
public final class BigEatServiceException extends Exception {

  private static final long serialVersionUID = 1121617708825544235L;

  public BigEatServiceException(final String message, final Throwable e) {
    super(message, e);
  }

  public BigEatServiceException(final String message) {
    super(message);
  }

}
