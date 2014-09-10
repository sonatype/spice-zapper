package org.sonatype.spice.zapper.hash;

import java.security.NoSuchAlgorithmException;

public class Sha256HashAlgorithm
    extends AbstractMessageDigestHashAlgorithm
    implements HashAlgorithm
{
  public static final HashAlgorithmIdentifier ID = new HashAlgorithmIdentifier("SHA-256", 32);

  public Sha256HashAlgorithm()
      throws NoSuchAlgorithmException
  {
    super(ID);
  }
}
