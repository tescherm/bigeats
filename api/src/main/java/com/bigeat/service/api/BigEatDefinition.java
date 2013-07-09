package com.bigeat.service.api;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Date;
import java.util.Map;

import com.bigeat.service.api.image.ImageDefinition;
import com.bigeat.service.api.image.ImageSize;
import com.bigeat.service.api.venue.Venue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author mattt
 * 
 */
public final class BigEatDefinition extends BigEat {

  private final Date created;
  private final Date modified;

  private final String id;

  private final Map<ImageSize, ImageDefinition> images;

  private BigEatDefinition(final Builder builder) {
    super(builder);

    id = builder.id;

    created = builder.created;
    modified = builder.modified;

    images = builder.images;

  }

  @JsonCreator
  public static BigEatDefinition newDefinition(@JsonProperty("id") final String id,
                                               @JsonProperty("itemNum") final Integer itemNum, 
                                               @JsonProperty("item") final String item,
                                               @JsonProperty("venue") final Venue venue,
                                               @JsonProperty("images") final Map<ImageSize, ImageDefinition> images,
                                               @JsonProperty("created") final Date created, 
                                               @JsonProperty("modified") final Date modified) {

    final Builder builder = new BigEatDefinition.Builder();

    builder.id(id);

    builder.itemNum(itemNum);
    builder.item(item);

    builder.images(images);
    builder.venue(venue);

    builder.created(created);
    builder.modified(modified);

    return builder.build();
  }

  public static final class Builder extends BigEatBuilder<Builder> {

    private Date created;
    private Date modified;

    private String id;

    private Map<ImageSize, ImageDefinition> images;

    public Builder() {

    }

    public Builder(final BigEatDefinition definition) {

      id = definition.id;

      item = definition.item;
      itemNum = definition.itemNum;

      images = definition.images;
      venue = definition.venue;

      created = definition.created;
      modified = definition.modified;
    }

    public static Builder builder() {
      return new Builder();
    }

    public static Builder builder(final BigEatDefinition definition) {
      return new Builder(definition);
    }

    public Builder created(final Date created) {
      this.created = created;
      return this;
    }

    public Builder modified(final Date modified) {
      this.modified = modified;
      return this;
    }

    public Builder id(final String id) {
      this.id = id;
      return this;
    }

    public Builder images(final Map<ImageSize, ImageDefinition> images) {
      this.images = images;
      return this;
    }

    public BigEatDefinition build() {

      // required
      checkNotNull(id, "id not specified");

      checkNotNull(created, "created time not specified");
      checkNotNull(modified, "modified time not specified");

      checkNotNull(itemNum, "item num not specified");
      // only 100 big eats
      checkState(itemNum >= 0 && itemNum < 100);

      checkNotNull(item, "item not specified");

      checkNotNull(images, "image endpoints not specified");
      checkNotNull(venue, "venue not specified");

      return new BigEatDefinition(this);
    }

  }

  public String getId() {
    return id;
  }

  public Map<ImageSize, ImageDefinition> getImages() {
    return images;
  }

  public Date getCreated() {
    return created;
  }

  public Date getModified() {
    return modified;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    final int superResult = super.hashCode();

    final int result = Objects.hashCode(id, created, modified, images);
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

    if (o instanceof BigEatDefinition) {
      final BigEatDefinition that = (BigEatDefinition) o;

      return Objects.equal(this.id, that.id) 
          && Objects.equal(this.created, that.created)
          && Objects.equal(this.modified, that.modified) 
          && Objects.equal(this.images, that.images);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);
    stringHelper.omitNullValues();

    stringHelper.add("id", id);

    stringHelper.add("itemNum", itemNum);
    stringHelper.add("item", item);

    stringHelper.add("images", images);
    stringHelper.add("venue", venue);

    stringHelper.add("created", created);
    stringHelper.add("modified", modified);

    return stringHelper.toString();
  }

}
