package com.bigeat.service.core;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.net.URI;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatDefinition.Builder;
import com.bigeat.service.api.image.ImageDefinition;
import com.bigeat.service.api.image.ImageRequest;
import com.bigeat.service.api.image.ImageSize;
import com.bigeat.service.api.image.UrlImageType;
import com.bigeat.service.api.BigEatImage;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.dal.BigEatImageRepository;
import com.bigeat.service.dal.BigEatRepository;
import com.bigeat.service.dal.exception.BigEatRepositoryException;
import com.bigeat.service.exception.BigEatImageNotFoundException;
import com.bigeat.service.exception.BigEatNotFoundException;
import com.bigeat.service.exception.BigEatRequestException;
import com.bigeat.service.exception.BigEatServiceException;
import com.bigeat.service.resource.BigEatResource;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author mattt
 */
public final class BigEatService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BigEatResource.class);

  private final BigEatImageRepository imageRepository;
  private final BigEatRepository bigEatRepository;

  private final BigEatImageUrlReader imageReader;

  public BigEatService(final BigEatRepository bigEatRepository,
      final BigEatImageRepository imageRepository) {
    checkNotNull(bigEatRepository);
    checkNotNull(imageRepository);

    this.bigEatRepository = bigEatRepository;
    this.imageRepository = imageRepository;

    imageReader = new BigEatImageUrlReader();

  }

  public BigEatDefinition createBigEat(final BigEatRequest bigEatRequest, final URI baseUri)
      throws BigEatServiceException, BigEatRequestException {
    checkNotNull(bigEatRequest);

    final Map<ImageSize, BigEatImage> images = buildImages(bigEatRequest);
    final BigEatDefinition bigEat = bigEatFromRequest(bigEatRequest, baseUri, images);

    try {
      bigEatRepository.createBigEat(bigEat);
    } catch (final BigEatRepositoryException e) {
      throw new BigEatServiceException("could not create big eat", e);
    }

    try {
      storeImages(images);
    } catch (final BigEatServiceException e) {

      doRollbackOnCreate(bigEat);
      throw new BigEatServiceException("could not store image", e);
    } catch (final Throwable e) {

      doRollbackOnCreate(bigEat);
      throw new BigEatServiceException("could not store image", e);
    }

    return bigEat;
  }

  private void storeImages(final Map<ImageSize, BigEatImage> images) throws BigEatServiceException {

    final List<BigEatImage> failedImages = Lists.newLinkedList();
    final List<BigEatImage> storedImages = Lists.newLinkedList();

    for (final BigEatImage image : images.values()) {
      try {
        imageRepository.storeImage(image);
        storedImages.add(image);
      } catch (final BigEatRepositoryException e) {
        LOGGER.error("could not store image " + image.getId(), e);
        failedImages.add(image);
      } catch (final Throwable e) {
        LOGGER.error("could not store image " + image.getId(), e);
        failedImages.add(image);
      }
    }

    if (!failedImages.isEmpty()) {
      rollbackStoredImages(storedImages);
      throw new BigEatServiceException("could not store the following images: " + images);
    }
  }

  private void rollbackStoredImages(final List<BigEatImage> storedImages) {
    for (final BigEatImage bigEatImage : storedImages) {
      try {
        imageRepository.deleteImage(bigEatImage.getId());
      } catch (final BigEatImageNotFoundException e) {
        LOGGER.error("could not delete image on rollback", e);
      } catch (final BigEatRepositoryException e) {
        LOGGER.error("could not delete image on rollback", e);
      } catch (final Throwable e) {
        LOGGER.error("could not delete image on rollback", e);
      }
    }
  }

  private BigEatDefinition bigEatFromRequest(final BigEatRequest bigEatRequest, final URI baseUri,
      final Map<ImageSize, BigEatImage> images) {

    final String id = "be_" + UUID.randomUUID().toString();
    final Date now = Calendar.getInstance().getTime();

    final Builder builder = new BigEatDefinition.Builder();

    builder.id(id);

    builder.itemNum(bigEatRequest.getItemNum());
    builder.item(bigEatRequest.getItem());

    builder.venue(bigEatRequest.getVenue());
    builder.images(images(id, baseUri, images));

    builder.created(now);
    builder.modified(now);

    return builder.build();
  }

  private Map<ImageSize, ImageDefinition> images(final String bigEatId, final URI baseUri,
      final Map<ImageSize, BigEatImage> images) {

    final BigEatImage smallImage = images.get(ImageSize.small);
    final BigEatImage largeImage = images.get(ImageSize.large);

    final String smallId = smallImage.getId();
    final String largeId = largeImage.getId();

    final URI smallUri = imageEndpoint(baseUri, bigEatId, smallId);
    final URI largeUri = imageEndpoint(baseUri, bigEatId, largeId);

    final ImageDefinition small =
        new ImageDefinition.Builder().id(smallId).endpoint(smallUri).build();
    final ImageDefinition large =
        new ImageDefinition.Builder().id(largeId).endpoint(largeUri).build();

    return ImmutableMap.of(ImageSize.small, small, ImageSize.large, large);
  }

  private URI imageEndpoint(final URI baseUri, final String bigEatId, final String imageId) {

    return UriBuilder.fromUri(baseUri).path("{bigEatId}").path("image").path("{imageId}")
        .build(bigEatId, imageId);
  }

  public BigEatDefinition getBigEat(final String bigEatId) throws BigEatNotFoundException,
      BigEatServiceException {
    checkNotNull(bigEatId);

    try {
      return bigEatRepository.getBigEat(bigEatId);
    } catch (final BigEatRepositoryException e) {
      throw new BigEatServiceException("could not obtain big eat", e);
    }
  }

  public Collection<BigEatDefinition> getBigEats() throws BigEatServiceException {

    try {
      return bigEatRepository.getBigEats();
    } catch (final BigEatRepositoryException e) {
      throw new BigEatServiceException("could not obtain big eats", e);
    }

  }

  public BigEatImage getBigEatImage(final String imageId) throws BigEatImageNotFoundException,
      BigEatServiceException {
    checkNotNull(imageId);

    try {
      return imageRepository.getImage(imageId);
    } catch (final BigEatRepositoryException e) {
      throw new BigEatServiceException("could not obtain big eat image", e);
    }
  }

  private void doRollbackOnCreate(final BigEatDefinition bigEat) {

    try {
      bigEatRepository.deleteBigEat(bigEat.getId());
    } catch (final BigEatNotFoundException e) {
      // shouldn't happen
      LOGGER.error("could not roll back on create", e);
    } catch (final BigEatRepositoryException e) {
      LOGGER.error("could not roll back on create", e);
    } catch (final Throwable e) {
      LOGGER.error("could not roll back on create", e);
    }

  }

  private Map<ImageSize, BigEatImage> buildImages(final BigEatRequest request)
      throws BigEatServiceException, BigEatRequestException {

    final Map<ImageSize, BigEatImage> imageMap = Maps.newHashMap();

    // TODO if doing serially is slow you could run these in separate threads
    final Map<ImageSize, ImageRequest> images = request.getImages();
    for (Map.Entry<ImageSize, ImageRequest> entry : images.entrySet()) {

      final ImageRequest requestedImage = entry.getValue();
      imageMap.put(entry.getKey(), buildImage(requestedImage));
    }

    return imageMap;

  }

  private BigEatImage buildImage(final ImageRequest requestedImage) throws BigEatRequestException,
      BigEatServiceException {

    // only supported type atm
    checkState(requestedImage instanceof UrlImageType, "unknown image request type: %s",
        requestedImage.getClass());

    final UrlImageType urlImage = (UrlImageType) requestedImage;
    return imageReader.read(urlImage.getImage());
  }

  public void deleteBigEat(final String bigEatId) throws BigEatNotFoundException,
      BigEatServiceException {
    checkNotNull(bigEatId);

    try {

      final BigEatDefinition bigEat = bigEatRepository.getBigEat(bigEatId);

      final Map<ImageSize, ImageDefinition> images = bigEat.getImages();
      deleteBigEatImages(images);

      bigEatRepository.deleteBigEat(bigEatId);

    } catch (final BigEatRepositoryException e) {
      throw new BigEatServiceException("could not delete big eat", e);
    }
  }

  private void deleteBigEatImages(final Map<ImageSize, ImageDefinition> images) {
    for (final Map.Entry<ImageSize, ImageDefinition> entry : images.entrySet()) {

      try {
        imageRepository.deleteImage(entry.getValue().getId());
      } catch (final BigEatImageNotFoundException e) {
        // shouldn't happen
        LOGGER.error("image delete - big eat image not found", e);
      } catch (final BigEatRepositoryException e) {
        LOGGER.error("could not delete big eat image", e);
      }

    }
  }
}
