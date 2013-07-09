package com.bigeat.service.api.image;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author matt
 */
public final class ImageDefinition {

  private final String id;
  private final URI endpoint;

  @JsonCreator
  public static ImageDefinition newImage(@JsonProperty("id") final String id,
                                         @JsonProperty("endpoint") final URI endpoint) {

    final Builder builder = new Builder();
    builder.id(id);
    builder.endpoint(endpoint);

    return builder.build();

  }

  private ImageDefinition(final Builder builder) {
    this.id = builder.id;
    this.endpoint = builder.endpoint;
  }

  public String getId() {
    return id;
  }

  public URI getEndpoint() {
    return endpoint;
  }


  @Override
  public int hashCode() {
    return Objects.hashCode(id, endpoint);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ImageDefinition) {
      final ImageDefinition that = (ImageDefinition) o;

      return Objects.equal(this.id, that.id) 
          && Objects.equal(this.endpoint, that.endpoint);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);

    stringHelper.add("id", id);
    stringHelper.add("endpoint", endpoint);

    return stringHelper.toString();
  }

  /**
   * @author mattt
   */
  public static final class Builder {

    private String id;
    private URI endpoint;

    public Builder id(final String id) {
      this.id = id;
      return this;
    }

    public Builder endpoint(final URI endpoint) {
      this.endpoint = endpoint;
      return this;
    }

    public ImageDefinition build() {

      checkNotNull(id, "id not specified");
      checkNotNull(endpoint, "endpoint uri not specified");

      return new ImageDefinition(this);
    }
  }
}
