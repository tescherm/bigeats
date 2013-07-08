package com.bigeat.service.api;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.UUID;

import com.google.common.hash.Hashing;


/**
 * @author mattt
 * 
 */
public final class BigEatImage {

  private final String id;

  private final byte[] contents;

  private final long size;
  private final String type;

  private final Date created;
  private final Date modified;

  private final String hash;

  private BigEatImage(final String id, final byte[] contents, final long size, final String type,
      final Date created, final Date modified) {
    checkNotNull(id);
    checkNotNull(contents);
    checkArgument(size > 0);

    checkNotNull(type);
    checkNotNull(created);
    checkNotNull(modified);

    this.id = id;
    this.contents = contents;
    this.hash = Hashing.murmur3_128().hashBytes(contents).toString();

    this.size = size;
    this.type = type;

    this.created = created;
    this.modified = modified;
  }

  public String getId() {
    return id;
  }

  public byte[] getContents() {
    return contents;
  }

  public long getSize() {
    return size;
  }

  public String getType() {
    return type;
  }

  public Date getCreated() {
    return created;
  }

  public Date getModified() {
    return modified;
  }

  public String getHash() {
    return hash;
  }

  public static BigEatImage newImage(final byte[] contents, final Long size, final String type,
      final Date created, final Date modified) {

    final String id = "im_" + UUID.randomUUID().toString();
    return new BigEatImage(id, contents, size, type, created, modified);
  }
}
