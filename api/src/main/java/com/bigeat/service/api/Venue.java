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
public final class Venue {

  @NotNull
  private final String name;

  private final URL website;

  private final String address;
  private final String phoneNumber;

  @JsonCreator
  public static Venue newVenue(@JsonProperty("name") final String name,
                               @JsonProperty("website") final URL website, 
                               @JsonProperty("address") final String address,
                               @JsonProperty("phoneNumber") final String phoneNumber) {

    final Builder builder = new Builder();
    builder.name(name);

    builder.website(website);
    builder.address(address);
    builder.phoneNumber(phoneNumber);

    return builder.build(false);

  }

  private Venue(final Builder builder) {
    this.name = builder.name;
    this.website = builder.website;
    this.address = builder.address;
    this.phoneNumber = builder.phoneNumber;
  }

  public String getName() {
    return name;
  }

  public URL getWebsite() {
    return website;
  }

  public String getAddress() {
    return address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, website, address, phoneNumber);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Venue) {
      final Venue that = (Venue) o;

      return Objects.equal(this.name, that.name) 
          && Objects.equal(this.website, that.website)
          && Objects.equal(this.address, that.address)
          && Objects.equal(this.phoneNumber, that.phoneNumber);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);
    stringHelper.omitNullValues();

    stringHelper.add("name", name);
    stringHelper.add("website", website);

    stringHelper.add("address", address);
    stringHelper.add("phoneNumber", phoneNumber);

    return stringHelper.toString();
  }

  public static final class Builder {

    private String name;

    private URL website;

    private String address;
    private String phoneNumber;

    public Builder name(final String name) {
      this.name = name;
      return this;
    }

    public Builder website(final URL website) {
      this.website = website;
      return this;
    }

    public Builder address(final String address) {
      this.address = address;
      return this;
    }

    public Builder phoneNumber(final String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public Venue build() {
      checkNotNull(name, "name not specified");
      return new Venue(this);
    }

    public Venue build(final boolean validate) {
      if (validate) {
        return build();
      }
      return new Venue(this);
    }
  }

}
