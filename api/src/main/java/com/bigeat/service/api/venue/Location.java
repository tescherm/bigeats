package com.bigeat.service.api.venue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author mattt
 * 
 */
public final class Location {

  private final String address;

  @JsonCreator
  public static Location newLocation(@JsonProperty("address") final String address) {

    final Builder builder = new Builder();
    builder.address(address);

    return builder.build(false);

  }

  private Location(final Builder builder) {
    this.address = builder.address;
  }

  public String getAddress() {
    return address;
  }

  @Override
  public int hashCode() {
    return address.hashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Location) {
      final Location that = (Location) o;

      return Objects.equal(this.address, that.address);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);
    stringHelper.omitNullValues();

    stringHelper.add("address", address);

    return stringHelper.toString();
  }

  public static final class Builder {

    private String address;

    public Builder address(final String address) {
      this.address = address;
      return this;
    }

    public Location build() {
      return new Location(this);
    }

    public Location build(final boolean validate) {
      if (validate) {
        return build();
      }
      return new Location(this);
    }

  }
}
