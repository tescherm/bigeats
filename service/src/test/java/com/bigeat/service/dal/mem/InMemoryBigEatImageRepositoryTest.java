package com.bigeat.service.dal.mem;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bigeat.service.api.BigEatImage;
import com.bigeat.service.dal.BigEatImageRepository;
import com.bigeat.service.dal.exception.BigEatRepositoryException;
import com.bigeat.service.exception.BigEatImageNotFoundException;
import com.bigeat.service.exception.BigEatRequestException;

public class InMemoryBigEatImageRepositoryTest {

  private final BigEatImageRepository repository;

  private final BigEatImage image;

  public InMemoryBigEatImageRepositoryTest() {
    repository = new InMemoryBigEatImageRepository();

    final byte[] bytes = "foo".getBytes();
    final Long length = Long.valueOf(bytes.length);

    image = BigEatImage.newImage(bytes, length, "image/png", new Date(), new Date());
  }

  @Test
  public void createImageTest() throws BigEatRepositoryException, BigEatRequestException {
    repository.storeImage(image);
  }

  @Test(dependsOnMethods = {"createImageTest"})
  public void getImageTest() throws BigEatRepositoryException, BigEatImageNotFoundException {
    final BigEatImage image = repository.getImage(this.image.getId());
    assertNotNull(image);

    assertEquals(this.image, image);
  }

  @Test(dependsOnMethods = {"getImageTest"})
  public void deleteImageTest() throws BigEatRepositoryException, BigEatImageNotFoundException {
    repository.deleteImage(this.image.getId());

    try {
      repository.getImage(this.image.getId());
      Assert.fail();
    } catch (BigEatImageNotFoundException e) {

    }

  }

  @Test(expectedExceptions = BigEatImageNotFoundException.class)
  public void deleteNonexistentBigEatTest() throws BigEatImageNotFoundException,
      BigEatRepositoryException {
    repository.deleteImage("foo");
  }

  @Test(expectedExceptions = BigEatImageNotFoundException.class)
  public void getNonexistentBigEatTest() throws BigEatImageNotFoundException,
      BigEatRepositoryException {
    repository.getImage("foo");
  }

}
