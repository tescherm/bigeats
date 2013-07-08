package com.bigeat.service.api;

import javax.validation.constraints.NotNull;

import com.google.common.base.Objects;

/**
 * @author mattt
 */
public abstract class BigEat {

  @NotNull
  protected final Integer itemNum;

  @NotNull
  protected final String item;

  @NotNull
  protected final Venue venue;
  
  protected BigEat(final BigEatBuilder<?> builder) {
    this.item = builder.item;
    this.itemNum = builder.itemNum;
    this.venue = builder.venue;
  }

  public static abstract class BigEatBuilder<T extends BigEatBuilder<T>> {

    protected Integer itemNum;
    protected String item;

    protected Venue venue;

    protected Contact contact;
    
    @SuppressWarnings("unchecked")
    public T itemNum(final Integer itemNum) {
      this.itemNum = itemNum;
      return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T item(final String item) {
      this.item = item;
      return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T venue(final Venue venue) {
      this.venue = venue;
      return (T) this;
    }
    
  }

  public Integer getItemNum() {
    return itemNum;
  }

  public String getItem() {
    return item;
  }

  public Venue getVenue() {
    return venue;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(item, itemNum, venue);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof BigEat) {
      final BigEat that = (BigEat) o;

      return Objects.equal(this.itemNum, that.itemNum)
          && Objects.equal(this.item, that.item)
          && Objects.equal(this.venue, that.venue);
    }
    return false;
  }

}
