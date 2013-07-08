package com.bigeat.service.core;

import static com.bigeat.service.test.BigEatRequestTestUtil.bigEat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatImage;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.api.ImageDefinition;
import com.bigeat.service.api.ImageType;
import com.bigeat.service.dal.BigEatImageRepository;
import com.bigeat.service.dal.BigEatRepository;
import com.bigeat.service.dal.mem.InMemoryBigEatImageRepository;
import com.bigeat.service.dal.mem.InMemoryBigEatRepository;
import com.bigeat.service.exception.BigEatImageNotFoundException;
import com.bigeat.service.exception.BigEatNotFoundException;
import com.bigeat.service.exception.BigEatRequestException;
import com.bigeat.service.exception.BigEatServiceException;

public final class BigEatServiceTest {

  private final BigEatService service;

  private BigEatDefinition definition;

  public BigEatServiceTest() {

    final BigEatRepository bigEatRepository = new InMemoryBigEatRepository();
    final BigEatImageRepository imageRepository = new InMemoryBigEatImageRepository();

    service = new BigEatService(bigEatRepository, imageRepository);
  }

  @Test
  public void getBigEatsEmptyTest() throws BigEatServiceException {
    final Collection<BigEatDefinition> bigEats = service.getBigEats();
    assertNotNull(bigEats);
    assertEquals(bigEats.size(), 0);
  }

  @Test(dependsOnMethods = {"getBigEatsEmptyTest"})
  public void createBigEatTest() throws BigEatServiceException, BigEatRequestException {

    final URI base = URI.create("/foo");

    final BigEatRequest bigEat = bigEat();
    final BigEatDefinition definition = service.createBigEat(bigEat, base);
    assertNotNull(definition);

    this.definition = definition;

    assertNotNull(definition.getCreated());
    assertNotNull(definition.getModified());
    assertNotNull(definition.getId());
    assertNotNull(definition.getImages());

    final Map<ImageType, ImageDefinition> images = definition.getImages();
    assertEquals(images.size(), 2);

    for (Map.Entry<ImageType, ImageDefinition> entry : images.entrySet()) {

      final ImageDefinition imageDefinition = entry.getValue();
      final URI endpoint = imageDefinition.getEndpoint();
      assertTrue(endpoint.getPath().contains(base.toString()));
    }

  }

  @Test(dependsOnMethods = {"createBigEatTest"})
  public void getBigEatsTest() throws BigEatServiceException {
    final Collection<BigEatDefinition> bigEats = service.getBigEats();
    assertNotNull(bigEats);
    assertEquals(bigEats.size(), 1);
  }

  @Test(dependsOnMethods = {"getBigEatsTest"})
  public void getBigEatTest() throws BigEatServiceException, BigEatNotFoundException {
    final BigEatDefinition definition = service.getBigEat(this.definition.getId());
    assertNotNull(definition);

    assertEquals(this.definition, definition);
  }

  @Test(dependsOnMethods = {"getBigEatTest"})
  public void getBigEatImageTest() throws BigEatImageNotFoundException, BigEatServiceException {

    final Map<ImageType, ImageDefinition> images = definition.getImages();
    assertEquals(images.size(), 2);

    for (Map.Entry<ImageType, ImageDefinition> entry : images.entrySet()) {
      final BigEatImage bigEatImage = service.getBigEatImage(entry.getValue().getId());
      assertNotNull(bigEatImage);
    }
  }

  @Test(dependsOnMethods = {"getBigEatImageTest"})
  public void deleteBigEatTest() throws BigEatNotFoundException, BigEatServiceException {
    service.deleteBigEat(this.definition.getId());

    final Collection<BigEatDefinition> bigEats = service.getBigEats();
    assertNotNull(bigEats);
    assertEquals(bigEats.size(), 0);

    try {
      service.getBigEat(this.definition.getId());
      Assert.fail();
    } catch (BigEatNotFoundException e) {

    }

  }

  @Test(expectedExceptions = BigEatNotFoundException.class)
  public void deleteNonexistentBigEatTest() throws BigEatNotFoundException, BigEatServiceException {
    service.deleteBigEat("foo");
  }

  @Test(expectedExceptions = BigEatNotFoundException.class)
  public void getNonexistentBigEatTest() throws BigEatNotFoundException, BigEatServiceException {
    service.getBigEat("foo");
  }

  @Test(expectedExceptions = BigEatImageNotFoundException.class)
  public void getNonexistentBigEatImageTest() throws BigEatImageNotFoundException,
      BigEatServiceException {
    service.getBigEatImage("foo");
  }

}
