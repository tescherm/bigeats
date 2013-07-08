package com.bigeat.service.configuration;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * @author mattt
 * 
 */
public final class BigEatConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty
  private String name;

  public String getName() {
    return name;
  }
}
