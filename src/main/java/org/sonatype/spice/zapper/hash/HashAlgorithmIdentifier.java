package org.sonatype.spice.zapper.hash;

import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.StringIdentifier;

public class HashAlgorithmIdentifier
    extends StringIdentifier
{
  private final int hashSizeInBytes;

  public HashAlgorithmIdentifier(final String stringValue, final int hashSizeInBytes) {
    super(stringValue);
    this.hashSizeInBytes = Check.argument(hashSizeInBytes > 0, hashSizeInBytes, "Hash size not positive!");
  }

  public int getHashSize() {
    return hashSizeInBytes;
  }
}
