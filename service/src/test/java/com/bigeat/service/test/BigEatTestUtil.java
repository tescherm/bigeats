package com.bigeat.service.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.core.BigEatService;
import com.bigeat.service.dal.BigEatImageRepository;
import com.bigeat.service.dal.BigEatRepository;
import com.bigeat.service.dal.mem.InMemoryBigEatImageRepository;
import com.bigeat.service.dal.mem.InMemoryBigEatRepository;

/**
 * @author mattt
 * 
 */
public final class BigEatTestUtil {

  public static final URI BIGEATS_URI = URI.create("/v1/bigeats");

  private BigEatTestUtil() {
    throw new AssertionError();
  }

  public static BigEatService testService() {
    final BigEatImageRepository imageRepository = new InMemoryBigEatImageRepository();
    final BigEatRepository bigEatRepository = new InMemoryBigEatRepository();

    return new BigEatService(bigEatRepository, imageRepository);
  }

  public static void checkDefinition(final BigEatRequest request, final BigEatDefinition definition) {

    assertNotNull(definition.getCreated());
    assertNotNull(definition.getModified());
    assertNotNull(definition.getId());
    assertNotNull(definition.getImages());
    assertNotNull(definition.getVenue());
    assertNotNull(definition.getItem());

    assertEquals(definition.getItemNum(), request.getItemNum());
    assertEquals(definition.getItem(), request.getItem());
    assertEquals(definition.getVenue(), request.getVenue());
  }

  public static URI bigEatUri(final String id) {
    return UriBuilder.fromUri(BIGEATS_URI).path("{bigEatId}").build(id);
  }

  public static URI bigEatImageUri(final String bigEatId, final String imageId) {
    return UriBuilder.fromUri(BIGEATS_URI).path("{bigEatId}").path("image").path("{imageId}")
        .build(bigEatId, imageId);
  }
}
