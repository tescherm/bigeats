package com.bigeat.service.dal.mem;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentMap;

import com.bigeat.service.api.BigEatImage;
import com.bigeat.service.dal.BigEatImageRepository;
import com.bigeat.service.dal.exception.BigEatRepositoryException;
import com.bigeat.service.exception.BigEatImageNotFoundException;
import com.google.common.collect.Maps;

/**
 * In memory image repository. In a real environment this could be s3, a CDN, etc.
 * 
 * @author mattt
 */
public final class InMemoryBigEatImageRepository implements BigEatImageRepository {

  private final ConcurrentMap<String, BigEatImage> repo;

  public InMemoryBigEatImageRepository() {
    repo = Maps.newConcurrentMap();
  }

  @Override
  public void storeImage(final BigEatImage image) throws BigEatRepositoryException {
    checkNotNull(image);

    final String imageId = image.getId();
    if (repo.containsKey(imageId)) {
      throw new BigEatRepositoryException("image with id already exists: " + imageId);
    }

    repo.put(imageId, image);

  }

  @Override
  public BigEatImage getImage(final String imageId) throws BigEatImageNotFoundException,
      BigEatRepositoryException {
    checkNotNull(imageId);

    if (!repo.containsKey(imageId)) {
      throw new BigEatImageNotFoundException(imageId);
    }

    return repo.get(imageId);
  }

  @Override
  public void deleteImage(final String imageId) throws BigEatImageNotFoundException,
      BigEatRepositoryException {
    checkNotNull(imageId);

    if (!repo.containsKey(imageId)) {
      throw new BigEatImageNotFoundException(imageId);
    }

    repo.remove(imageId);

  }

}
