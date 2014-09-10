package org.sonatype.spice.zapper.fs;

import java.io.File;
import java.io.IOException;

import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.HashAlgorithm;


public interface HashStrategy
{
  HashAlgorithm getHashAlgorithm();

  Hash getHashFor(final File file)
      throws IOException;
}
