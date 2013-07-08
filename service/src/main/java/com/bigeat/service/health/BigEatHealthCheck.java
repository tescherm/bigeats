package com.bigeat.service.health;

import com.yammer.metrics.core.HealthCheck;

/**
 * @author mattt
 * 
 */
public final class BigEatHealthCheck extends HealthCheck {

  public BigEatHealthCheck() {
    super("bigeat");
  }

  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }
}
