package com.bigeat.service.api;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public final class Image {

  @NotNull
  private final URL smallUrl;

  @NotNull
  private final URL largeUrl;

  @JsonCreator
  public static Image newImage(@JsonProperty("smallUrl") final URL smallUrl,
                               @JsonProperty("largeUrl") final URL largeUrl) {

    final Builder builder = new Builder();
    builder.smallUrl(smallUrl);
    builder.largeUrl(largeUrl);

    return builder.build(false);

  }

  private Image(final Builder builder) {
    this.smallUrl = builder.smallUrl;
    this.largeUrl = builder.largeUrl;
  }

  public URL getSmallUrl() {
    return smallUrl;
  }

  public URL getLargeUrl() {
    return largeUrl;
  }


  @Override
  public int hashCode() {
    return Objects.hashCode(smallUrl, largeUrl);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Image) {
      final Image that = (Image) o;

      return Objects.equal(this.smallUrl, that.smallUrl)
          && Objects.equal(this.largeUrl, that.largeUrl);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);

    stringHelper.add("smallUrl", smallUrl);
    stringHelper.add("largeUrl", largeUrl);

    return stringHelper.toString();
  }

  /**
   * @author mattt
   */
  public static final class Builder {

    private URL smallUrl;
    private URL largeUrl;

    public Builder() {

    }

    public Builder(final Image image) {
      smallUrl = image.smallUrl;
      smallUrl = image.smallUrl;
    }

    public static Builder builder() {
      return new Builder();
    }

    public static Builder builder(final Image image) {
      return new Builder(image);
    }

    public Builder smallUrl(final URL smallUrl) {
      this.smallUrl = smallUrl;
      return this;
    }

    public Builder largeUrl(final URL largeUrl) {
      this.largeUrl = largeUrl;
      return this;
    }

    public Image build(final boolean validate) {
      if (validate) {
        return build();
      }
      return new Image(this);
    }

    public Image build() {

      checkNotNull(smallUrl, "small url not specified");
      checkNotNull(largeUrl, "large url not specified");

      return new Image(this);
    }
  }
}
