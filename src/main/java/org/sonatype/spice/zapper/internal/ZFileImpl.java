package org.sonatype.spice.zapper.internal;

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.ZFile;
import org.sonatype.spice.zapper.hash.Hash;

public class ZFileImpl
    extends AbstractHashedRange
    implements ZFile
{
  private final Path path;

  private final long lastModified;

  public ZFileImpl(final Path path, final long length, final long lastModified, final Hash hash) {
    super(0, length, hash);
    this.path = Check.notNull(path, "Path is null!");
    this.lastModified = lastModified;
  }

  public Path getIdentifier() {
    return path;
  }

  public long getLastModifiedTimestamp() {
    return lastModified;
  }

  // ==

  @Override
  public String toString() {
    return super.toString() + "(path=" + getIdentifier() + ")";
  }
}
