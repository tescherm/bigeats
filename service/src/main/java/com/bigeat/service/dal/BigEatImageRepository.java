package com.bigeat.service.dal;

import com.bigeat.service.api.BigEatImage;
import com.bigeat.service.dal.exception.BigEatRepositoryException;
import com.bigeat.service.exception.BigEatImageNotFoundException;

/**
 * @author mattt
 * 
 */
public interface BigEatImageRepository {

  void storeImage(BigEatImage image) throws BigEatRepositoryException;

  BigEatImage getImage(String imageId) throws BigEatImageNotFoundException,
      BigEatRepositoryException;

  void deleteImage(String imageId) throws BigEatImageNotFoundException, BigEatRepositoryException;

}
