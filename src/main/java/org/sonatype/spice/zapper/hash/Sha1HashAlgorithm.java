package org.sonatype.spice.zapper.hash;

import java.security.NoSuchAlgorithmException;

public class Sha1HashAlgorithm
    extends AbstractMessageDigestHashAlgorithm
    implements HashAlgorithm
{
  public static final HashAlgorithmIdentifier ID = new HashAlgorithmIdentifier("SHA-1", 20);

  public Sha1HashAlgorithm()
      throws NoSuchAlgorithmException
  {
    super(ID);
  }
}
