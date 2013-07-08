package com.bigeat.service.exception;

/**
 * @author mattt
 * 
 */
public class BigEatNotFoundException extends Exception {

  private static final long serialVersionUID = 9048550046813742807L;

  public BigEatNotFoundException(final String bigEatId) {
    super(bigEatId + " not found");
  }

}
