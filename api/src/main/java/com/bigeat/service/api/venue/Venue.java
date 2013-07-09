package com.bigeat.service.api.venue;

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

  private final Location location;
  private final Contact contact;

  @JsonCreator
  public static Venue newVenue(@JsonProperty("name") final String name,
                               @JsonProperty("website") final URL website, 
                               @JsonProperty("location") final Location location,
                               @JsonProperty("contact") final Contact contact) {

    final Builder builder = new Builder();
    builder.name(name);

    builder.website(website);
    builder.location(location);
    builder.contact(contact);

    return builder.build(false);

  }

  private Venue(final Builder builder) {
    this.name = builder.name;
    this.website = builder.website;
    this.location = builder.location;
    this.contact = builder.contact;
  }

  public String getName() {
    return name;
  }

  public URL getWebsite() {
    return website;
  }

  public Location getLocation() {
    return location;
  }
  
  public Contact getContact() {
    return contact;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, website, location, contact);
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
          && Objects.equal(this.location, that.location)
          && Objects.equal(this.contact, that.contact);
    }
    return false;
  }

  @Override
  public String toString() {
    final ToStringHelper stringHelper = Objects.toStringHelper(this);
    stringHelper.omitNullValues();

    stringHelper.add("name", name);
    stringHelper.add("website", website);

    stringHelper.add("location", location);
    stringHelper.add("contact", contact);

    return stringHelper.toString();
  }

  public static final class Builder {

    private String name;

    private URL website;

    private Location location;
    private Contact contact;
    
    public Builder name(final String name) {
      this.name = name;
      return this;
    }

    public Builder website(final URL website) {
      this.website = website;
      return this;
    }

    public Builder location(final Location location) {
      this.location = location;
      return this;
    }
    
    public Builder contact(final Contact contact) {
      this.contact = contact;
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
