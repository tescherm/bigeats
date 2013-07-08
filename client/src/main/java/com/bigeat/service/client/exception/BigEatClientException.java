package com.bigeat.service.client.exception;

/**
 * @author mattt
 */
public class BigEatClientException extends Exception {

  private static final long serialVersionUID = -1668508342127036609L;

  public BigEatClientException(final String message, final Throwable e) {
    super(message, e);
  }

}
