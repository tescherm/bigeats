package com.bigeat.service.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.core.HttpHeaders;

import com.bigeat.service.api.BigEatImage;
import com.bigeat.service.exception.BigEatRequestException;
import com.bigeat.service.exception.BigEatServiceException;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;

/**
 * Could have other ways to read images (a base64 string)
 * 
 * @author mattt
 * 
 */
public final class BigEatImageUrlReader {

  private static final Set<String> ALLOWED_IMAGE_TYPES = ImmutableSet.of("image/jpeg", "image/png");

  public BigEatImageUrlReader() {}

  public BigEatImage read(final URL imageUrl) throws BigEatRequestException, BigEatServiceException {
    checkNotNull(imageUrl);

    final URLConnection connection = doObtainConnection(imageUrl);
    checkResponseCode(connection);

    final String type = connection.getHeaderField(HttpHeaders.CONTENT_TYPE);
    if (type == null) {
      throw new BigEatRequestException("image '" + imageUrl + "' had no content type header");
    }
    checkType(type);

    final String lengthHeader = connection.getHeaderField(HttpHeaders.CONTENT_LENGTH);
    if (lengthHeader == null) {
      throw new BigEatRequestException("image '" + imageUrl + "' had no content length header");
    }
    final Long size = Long.valueOf(lengthHeader);

    final byte[] contents = doReadContents(connection);

    // TODO could fast fail if size is too large

    final Date now = Calendar.getInstance().getTime();
    return BigEatImage.newImage(contents, size, type, now, now);

  }

  private void checkResponseCode(URLConnection connection) throws BigEatRequestException {

    // may not be for tests
    if (connection instanceof HttpURLConnection) {
      final HttpURLConnection httpConnection = (HttpURLConnection) connection;
      final int status = doObtainStatusCode(httpConnection);

      // 4xx or 5xx
      if (status / 100 == 4 || status / 100 == 5) {
        throw new BigEatRequestException("unexpected status code obtained for image: "
            + connection.getURL() + ",code=" + status);
      }
    }

  }

  private int doObtainStatusCode(final HttpURLConnection httpConnection) {
    try {
      return httpConnection.getResponseCode();
    } catch (IOException e) {
      throw new IllegalStateException("coud not obtain response code", e);
    }
  }

  private static void checkType(final String type) throws BigEatRequestException {
    if (!ALLOWED_IMAGE_TYPES.contains(type)) {
      throw new BigEatRequestException("type '" + type + "' is not an allowed image media type");
    }
  }

  private byte[] doReadContents(final URLConnection connection) throws BigEatServiceException {

    // TODO should check that the resource is an image here since you can spoof the content type

    try {

      final InputStream contents = connection.getInputStream();
      final byte[] bytes = ByteStreams.toByteArray(contents);
      return bytes;

    } catch (final IOException e) {
      throw new BigEatServiceException("could not read image: " + connection.getURL(), e);
    }
  }

  private URLConnection doObtainConnection(final URL imageUrl) throws BigEatServiceException {
    try {
      return imageUrl.openConnection();
    } catch (final IOException e) {
      throw new BigEatServiceException("could not open connection: " + imageUrl, e);
    }
  }
}
