package com.bigeat.service.dal.mem;

import static com.bigeat.service.test.BigEatDefinitionTestUtil.definition;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Collection;

import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.dal.BigEatRepository;
import com.bigeat.service.dal.exception.BigEatRepositoryException;
import com.bigeat.service.exception.BigEatNotFoundException;
import com.bigeat.service.exception.BigEatRequestException;

public final class InMemoryBigEatRepositoryTest {

  private final BigEatRepository repository;

  private final BigEatDefinition definition;

  public InMemoryBigEatRepositoryTest() {
    repository = new InMemoryBigEatRepository();
    definition = definition();
  }

  @Test
  public void getBigEatsEmptyTest() throws BigEatRepositoryException {
    final Collection<BigEatDefinition> bigEats = repository.getBigEats();
    assertNotNull(bigEats);
    assertEquals(bigEats.size(), 0);
  }

  @Test(dependsOnMethods = {"getBigEatsEmptyTest"})
  public void createBigEatTest() throws BigEatRepositoryException, BigEatRequestException {

    repository.createBigEat(definition);
  }

  @Test(dependsOnMethods = {"createBigEatTest"})
  public void getBigEatsTest() throws BigEatRepositoryException {
    final Collection<BigEatDefinition> bigEats = repository.getBigEats();
    assertNotNull(bigEats);
    assertEquals(bigEats.size(), 1);
  }

  @Test(dependsOnMethods = {"getBigEatsTest"})
  public void getBigEatTest() throws BigEatRepositoryException, BigEatNotFoundException {
    final BigEatDefinition definition = repository.getBigEat(this.definition.getId());
    assertNotNull(definition);

    assertEquals(this.definition, definition);
  }


  @Test(expectedExceptions = BigEatNotFoundException.class)
  public void deleteNonexistentBigEatTest() throws BigEatNotFoundException,
      BigEatRepositoryException {
    repository.deleteBigEat("foo");
  }

  @Test(expectedExceptions = BigEatNotFoundException.class)
  public void getNonexistentBigEatTest() throws BigEatNotFoundException, BigEatRepositoryException {
    repository.getBigEat("foo");
  }


}
