package com.bigeat.service.exception;

/**
 * @author mattt
 * 
 */
public class BigEatImageNotFoundException extends Exception {

  private static final long serialVersionUID = 9048550046813742807L;

  public BigEatImageNotFoundException(final String bigEatId) {
    super(bigEatId + " not found");
  }

}
