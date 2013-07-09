package com.bigeat.service.api;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.bigeat.service.api.image.ImageRequest;
import com.bigeat.service.api.image.ImageSize;
import com.bigeat.service.api.venue.Contact;
import com.bigeat.service.api.venue.Venue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author mattt
 * 
 */
public final class BigEatRequest extends BigEat {

  @NotNull
  private final Map<ImageSize, ImageRequest> images;

  private BigEatRequest(final Builder builder) {
    super(builder);

    this.images = builder.images;
  }

  @JsonCreator
  public static BigEatRequest newRequest(@JsonProperty("itemNum") final Integer itemNum,
                                         @JsonProperty("item") final String item,
                                         @JsonProperty("images") final Map<ImageSize, ImageRequest> images,
                                         @JsonProperty("venue") final Venue venue,
                                         @JsonProperty("contact") final Contact contact) {

    final Builder builder = new BigEatRequest.Builder();

    builder.itemNum(itemNum);
    builder.item(item);

    builder.images(images);
    builder.venue(venue);

    return builder.build(false);
  }

  public Map<ImageSize, ImageRequest> getImages() {
    return images;
  }

  public static final class Builder extends BigEatBuilder<Builder> {

    private Map<ImageSize, ImageRequest> images;

    public Builder() {

    }

    public Builder(final BigEatRequest request) {
      item = request.item;
      images = request.images;
      itemNum = request.itemNum;
      venue = request.venue;
    }

    public static Builder builder() {
      return new Builder();
    }

    public static Builder builder(final BigEatRequest request) {
      return new Builder(request);
    }

    public Builder images(final Map<ImageSize, ImageRequest> images) {
      this.images = images;
      return this;
    }

    public BigEatRequest build(final boolean validate) {
      if (validate) {
        return build();
      }
      return new BigEatRequest(this);
    }

    public BigEatRequest build() {

      // required

      checkNotNull(itemNum, "item num not specified");
      // only 100 big eats
      checkState(itemNum >= 0 && itemNum < 100);

      checkNotNull(item, "item not specified");

      checkNotNull(images, "images not specified");
      checkNotNull(venue, "venue not specified");

      return new BigEatRequest(this);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    final int superResult = super.hashCode();

    final int result = images == null ? 0 : images.hashCode();
    return prime * superResult + result;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!super.equals(o)) {
      return false;
    }

    if (o instanceof BigEatRequest) {
      final BigEatRequest that = (BigEatRequest) o;

      return Objects.equal(this.images, that.images);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);
    stringHelper.omitNullValues();

    stringHelper.add("itemNum", itemNum);
    stringHelper.add("item", item);

    stringHelper.add("images", images);
    stringHelper.add("venue", venue);

    return stringHelper.toString();
  }
}
