package com.bigeat.service.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author mattt
 * 
 */
public final class Contact {

  private final String phoneNumber;

  @JsonCreator
  public static Contact newContact(@JsonProperty("phoneNumber") final String phoneNumber) {

    final Builder builder = new Builder();
    builder.phoneNumber(phoneNumber);

    return builder.build(false);

  }

  private Contact(final Builder builder) {
    this.phoneNumber = builder.phoneNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public int hashCode() {
    return phoneNumber.hashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Contact) {
      final Contact that = (Contact) o;

      return Objects.equal(this.phoneNumber, that.phoneNumber);

    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);
    stringHelper.omitNullValues();

    stringHelper.add("phoneNumber", phoneNumber);

    return stringHelper.toString();
  }

  public static final class Builder {

    private String phoneNumber;

    public Builder phoneNumber(final String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public Contact build() {
      return new Contact(this);
    }

    public Contact build(final boolean validate) {
      if (validate) {
        return build();
      }
      return new Contact(this);
    }

  }
}
