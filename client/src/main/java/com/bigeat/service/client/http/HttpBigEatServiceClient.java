package com.bigeat.service.client.http;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.bigeat.service.api.BigEatDefinition;
import com.bigeat.service.api.BigEatRequest;
import com.bigeat.service.client.BigEatServiceClient;
import com.bigeat.service.client.exception.BigEatClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.net.MediaType;
import com.yammer.dropwizard.json.ObjectMapperFactory;

/**
 * @author mattt
 * 
 */
public final class HttpBigEatServiceClient implements BigEatServiceClient {

  private static final ObjectMapper MAPPER = new ObjectMapperFactory().build();

  private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";

  private static final URI BIGEATS_ENDPOINT = URI.create("service/v1/bigeats");

  // in ms
  private static final int DEFAULT_SOCKET_TIMEOUT = 30000;
  private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

  private static final int MAX_TOTAL_HTTP_CONNS = 100;
  private static final int MAX_PER_ROUTE_HTTP_CONNS = 10;

  private final HttpClient httpclient;

  private final URI baseUri;

  public HttpBigEatServiceClient(final URI serviceUri) {
    checkNotNull(serviceUri);

    this.baseUri = serviceUri.resolve("/");

    try {
      final URL url = baseUri.toURL();
      assert url != null;
    } catch (final MalformedURLException e) {
      throw new IllegalArgumentException("uri must be a url", e);
    }

    httpclient = createHttpClient();

  }

  @Override
  public BigEatDefinition createBigEat(final BigEatRequest request) throws BigEatClientException {
    checkNotNull(request);

    final HttpPost post = new HttpPost(getEndpointUrl(BIGEATS_ENDPOINT));

    post.setEntity(buildBigEatEntity(request));

    final ResponseHandler<BigEatDefinition> responseHandler =
        new ResponseHandler<BigEatDefinition>() {
          @Override
          public BigEatDefinition handleResponse(final HttpResponse response) throws IOException {
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_CREATED) {
              // consume response
              throw new HttpResponseException(statusCode, responseToString(response));
            }
            return MAPPER.readValue(responseToStreamReader(response), BigEatDefinition.class);
          }
        };

    try {
      return httpclient.execute(post, responseHandler);
    } catch (final HttpResponseException e) {
      throw new BigEatClientException("create big eat failed with status code: "
          + e.getStatusCode(), e);
    } catch (final ClientProtocolException e) {
      throw new BigEatClientException("create big eat failed", e);
    } catch (final IOException e) {
      throw new BigEatClientException("create big eat failed", e);
    } catch (final Throwable e) {
      post.abort();
      throw new BigEatClientException("unexpected error creating big eat", e);
    }


  }

  private static HttpEntity buildBigEatEntity(final BigEatRequest request) {

    // fully buffering should be fine as it's a small payload.
    final String json = readRequest(request);
    return new StringEntity(json, ContentType.APPLICATION_JSON);
  }

  private static String readRequest(final BigEatRequest request) {
    try {
      return MAPPER.writeValueAsString(request);
    } catch (final JsonProcessingException e) {
      throw new IllegalStateException("could not read request", e);
    }
  }

  private static HttpClient createHttpClient() {
    final PoolingClientConnectionManager cm = new PoolingClientConnectionManager();

    cm.setMaxTotal(MAX_TOTAL_HTTP_CONNS);
    cm.setDefaultMaxPerRoute(MAX_PER_ROUTE_HTTP_CONNS);

    final HttpClient client = new DefaultHttpClient(cm);

    final HttpParams params = client.getParams();
    params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
    params.setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);

    return client;
  }

  private URI getEndpointUrl(final URI endpoint) {
    return baseUri.resolve(endpoint);
  }

  private static String responseToString(final HttpResponse response) {
    checkNotNull(response);

    final HttpEntity entity = response.getEntity();
    checkNotNull(entity);

    final ContentType contentType = ContentType.getOrDefault(entity);
    final String charset = contentTypeOrDefaultCharset(contentType);

    try {
      return EntityUtils.toString(entity, charset);
    } catch (final ParseException e) {
      // TODO log
      return "could not parse response";
    } catch (final IOException e) {
      // TODO log
      return "could not read response";
    }
  }

  private static String contentTypeOrDefaultCharset(final ContentType contentType) {
    checkNotNull(contentType);

    final Charset charset = contentType.getCharset();
    // default
    if (charset == null) {
      return Charsets.UTF_8.name();
    }
    return charset.name();
  }

  private static Reader responseToStreamReader(final HttpResponse response) throws IOException {
    checkNotNull(response);

    final HttpEntity entity = response.getEntity();
    checkNotNull(entity);

    final InputStream stream = entity.getContent();
    if (stream == null) {
      throw new IOException("no content");
    }

    final ContentType contentType = ContentType.getOrDefault(entity);
    checkMimeType(contentType);

    final String charset = contentTypeOrDefaultCharset(contentType);
    return new InputStreamReader(stream, charset);
  }

  public static void checkMimeType(final ContentType contentType) throws IOException {
    checkNotNull(contentType);

    final String mime_type = contentType.getMimeType();
    if (!isJsonMimeType(mime_type)) {
      throw new IOException("unsupported content type: " + mime_type);
    }
  }

  private static boolean isJsonMimeType(final String mime_type) {
    checkNotNull(mime_type);

    final boolean isJsonUtf8 = MediaType.JSON_UTF_8.toString().equals(mime_type);
    final boolean isJson = APPLICATION_JSON_CONTENT_TYPE.equals(mime_type);

    return isJsonUtf8 || isJson;
  }
}
