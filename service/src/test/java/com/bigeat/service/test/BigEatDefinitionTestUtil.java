package com.bigeat.service.test;

import static com.bigeat.service.test.BigEatRequestTestUtil.bigEat;

import java.net.URI;
import java.util.Date;
import java.util.Map;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatDefinition.Builder;
import com.bigeat.service.api.image.ImageDefinition;
import com.bigeat.service.api.image.ImageSize;
import com.bigeat.service.api.BigEatRequest;
import com.google.common.collect.ImmutableMap;

public final class BigEatDefinitionTestUtil {

  private BigEatDefinitionTestUtil() {
    throw new AssertionError();
  }

  public static BigEatDefinition definition() {
    final BigEatRequest request = bigEat();

    final Builder builder = new BigEatDefinition.Builder();

    builder.created(new Date());
    builder.modified(new Date());
    builder.id("def-id");

    builder.item(request.getItem());
    builder.images(images());
    builder.itemNum(request.getItemNum());

    builder.venue(request.getVenue());

    return builder.build();

  }

  private static Map<ImageSize, ImageDefinition> images() {

    final ImageDefinition small =
        new ImageDefinition.Builder().id("small").endpoint(URI.create("small")).build();
    final ImageDefinition large =
        new ImageDefinition.Builder().id("large").endpoint(URI.create("large")).build();

    return ImmutableMap.of(ImageSize.small, small, ImageSize.large, large);
  }
}
