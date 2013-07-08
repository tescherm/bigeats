package com.bigeat.service.api;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author mattt
 * 
 */
public final class UrlImageType extends ImageRequest {

  public static final ImageType type = ImageType.url;

  @NotNull
  private final URL image;

  public UrlImageType(final Builder builder) {
    this.image = builder.image;
  }

  @JsonCreator
  public static UrlImageType newImage(@JsonProperty("image") final URL image) {

    checkNotNull(image);

    final Builder builder = new Builder();
    builder.image(image);

    return builder.build(false);

  }

  public URL getImage() {
    return image;
  }

  /**
   * @author mattt
   */
  public static final class Builder {

    private URL image;

    public Builder() {
    }

    public Builder(final UrlImageType request) {
      image = request.image;
    }

    public static Builder builder() {
      return new Builder();
    }

    public static Builder builder(final UrlImageType request) {
      return new Builder(request);
    }

    public Builder image(final URL image) {
      this.image = image;
      return this;
    }

    public UrlImageType build(final boolean validate) {
      if (validate) {
        return build();
      }
      return new UrlImageType(this);
    }

    public UrlImageType build() {
      checkNotNull(image, "image not specified");

      return new UrlImageType(this);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(type, image);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof UrlImageType) {
      final UrlImageType that = (UrlImageType) o;

      return Objects.equal(type, type) 
          && Objects.equal(this.image, that.image);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);

    stringHelper.add("type", type);
    stringHelper.add("image", image);

    return stringHelper.toString();
  }

}
