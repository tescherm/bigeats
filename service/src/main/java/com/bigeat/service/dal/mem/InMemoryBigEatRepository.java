package com.bigeat.service.dal.mem;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.dal.BigEatRepository;
import com.bigeat.service.dal.exception.BigEatRepositoryException;
import com.bigeat.service.exception.BigEatNotFoundException;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * In memory big eat repository. In a real environment this could be a database, filesystem, etc.
 * 
 * @author mattt
 * 
 */
public final class InMemoryBigEatRepository implements BigEatRepository {

  private final ConcurrentMap<String, BigEatDefinition> repo;

  public InMemoryBigEatRepository() {
    repo = Maps.newConcurrentMap();
  }

  @Override
  public BigEatDefinition getBigEat(final String bigEatId) throws BigEatNotFoundException {
    checkNotNull(bigEatId);

    if (!repo.containsKey(bigEatId)) {
      throw new BigEatNotFoundException(bigEatId);
    }

    return repo.get(bigEatId);
  }

  @Override
  public Collection<BigEatDefinition> getBigEats() {
    return ImmutableSet.copyOf(repo.values());
  }

  @Override
  public void createBigEat(final BigEatDefinition bigEat) throws BigEatRepositoryException {
    checkNotNull(bigEat);

    final String bigEatId = bigEat.getId();
    if (repo.containsKey(bigEatId)) {
      throw new BigEatRepositoryException("big eat already exists: " + bigEatId);
    }

    repo.put(bigEatId, bigEat);
  }

  @Override
  public void deleteBigEat(final String bigEatId) throws BigEatNotFoundException,
      BigEatRepositoryException {
    checkNotNull(bigEatId);

    if (!repo.containsKey(bigEatId)) {
      throw new BigEatNotFoundException(bigEatId);
    }

    repo.remove(bigEatId);

  }
}
