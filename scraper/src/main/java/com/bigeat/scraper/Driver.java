package com.bigeat.scraper;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;


/**
 * @author mattt
 * 
 */
public final class Driver {

  public static void main(final String[] args) {
    final Properties properties = readProperties();

    final URI serviceUri = readServiceUri(properties);
    final URL bigeatUrl = readBigEatUrl(properties);

    final BigEatScraper scraper = new BigEatScraper(serviceUri, bigeatUrl);
    scraper.scrape();
  }

  private static URL readBigEatUrl(final Properties properties) {
    final String urlString = checkNotNull(properties.getProperty("bigeat.scraper.url"));
    try {
      return new URL(urlString);
    } catch (final MalformedURLException e) {
      throw new IllegalStateException("malformed big eat url", e);
    }
  }

  private static URI readServiceUri(final Properties properties) {
    final String uriString = checkNotNull(properties.getProperty("bigeat.scraper.service.uri"));
    try {
      return new URI(uriString);
    } catch (final URISyntaxException e) {
      throw new IllegalStateException("malformed service uri", e);
    }
  }

  private static Properties readProperties() {

    final InputSupplier<InputStream> propsStreamSupplier = readPropsResource();

    try {
      return doLoadProps(propsStreamSupplier);
    } catch (final IOException e) {
      throw new IllegalStateException("could not read scraper props", e);
    }
  }

  private static Properties doLoadProps(final InputSupplier<InputStream> propsStreamSupplier)
      throws IOException {

    final Properties properties = new Properties();
    final InputStream propsStream = propsStreamSupplier.getInput();

    boolean threw = true;
    try {
      properties.load(propsStream);
      threw = false;
    } finally {
      Closeables.close(propsStream, threw);
    }

    return properties;
  }

  private static InputSupplier<InputStream> readPropsResource() {
    final URL propsResource = Resources.getResource("config.properties");
    return Resources.newInputStreamSupplier(propsResource);
  }
}
