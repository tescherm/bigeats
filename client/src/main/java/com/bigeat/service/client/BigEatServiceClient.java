package com.bigeat.service.client;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.client.exception.BigEatClientException;

/**
 * @author mattt
 *
 */
public interface BigEatServiceClient {

  BigEatDefinition createBigEat(BigEatRequest request) throws BigEatClientException;

}
