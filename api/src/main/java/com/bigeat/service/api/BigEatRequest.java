package com.bigeat.service.api;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import javax.validation.constraints.NotNull;

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
  private final Image image;

  private BigEatRequest(final Builder builder) {
    super(builder);

    this.image = builder.image;
  }

  @JsonCreator
  public static BigEatRequest newRequest(@JsonProperty("itemNum") final Integer itemNum,
                                         @JsonProperty("item") final String item,
                                         @JsonProperty("image") final Image image,
                                         @JsonProperty("venue") final Venue venue) {

    final Builder builder = new BigEatRequest.Builder();

    builder.itemNum(itemNum);
    builder.item(item);

    builder.image(image);
    builder.venue(venue);

    return builder.build(false);
  }

  public Image getImage() {
    return image;
  }

  public static final class Builder extends BigEatBuilder<Builder> {

    private Image image;

    public Builder() {

    }

    public Builder(final BigEatRequest request) {
      item = request.item;
      image = request.image;
      itemNum = request.itemNum;
      venue = request.venue;
    }

    public static Builder builder() {
      return new Builder();
    }

    public static Builder builder(final BigEatRequest request) {
      return new Builder(request);
    }

    public Builder image(final Image image) {
      this.image = image;
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

      checkNotNull(image, "image not specified");
      checkNotNull(venue, "venue not specified");

      return new BigEatRequest(this);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    final int superResult = super.hashCode();

    final int result = image == null ? 0 : image.hashCode();
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

      return Objects.equal(this.image, that.image);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);
    stringHelper.omitNullValues();

    stringHelper.add("itemNum", itemNum);
    stringHelper.add("item", item);

    stringHelper.add("image", image);
    stringHelper.add("venue", venue);

    return stringHelper.toString();
  }
}
