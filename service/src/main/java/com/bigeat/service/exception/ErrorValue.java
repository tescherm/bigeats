package com.bigeat.service.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author mattt
 * 
 */
public final class ErrorValue {

  private final String code;
  private final String message;

  @JsonCreator
  public static ErrorValue newImage(@JsonProperty("code") final int code,
      @JsonProperty("message") final String message) {
    return new ErrorValue(code, message);
  }

  public ErrorValue(final int code, final String message) {
    this.code = String.valueOf(code);
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
