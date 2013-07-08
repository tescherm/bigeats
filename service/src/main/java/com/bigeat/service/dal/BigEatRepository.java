package com.bigeat.service.dal;

import java.util.Collection;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.dal.exception.BigEatRepositoryException;
import com.bigeat.service.exception.BigEatNotFoundException;

/**
 * @author mattt
 * 
 */
public interface BigEatRepository {

  BigEatDefinition getBigEat(String bigEatId) throws BigEatNotFoundException,
      BigEatRepositoryException;

  Collection<BigEatDefinition> getBigEats() throws BigEatRepositoryException;

  void createBigEat(BigEatDefinition bigEat) throws BigEatRepositoryException;

  void deleteBigEat(String bigEatId) throws BigEatNotFoundException, BigEatRepositoryException;
}
