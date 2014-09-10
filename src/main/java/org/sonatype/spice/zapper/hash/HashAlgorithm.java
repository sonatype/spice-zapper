package org.sonatype.spice.zapper.hash;

import java.io.InputStream;
import java.io.OutputStream;

import org.sonatype.spice.zapper.Identified;


public interface HashAlgorithm
    extends Identified<HashAlgorithmIdentifier>
{
  Hash hash(byte[] buffer);

  HashingInputStream hashInput(InputStream input);

  HashingOutputStream hashOutput(OutputStream output);
}
