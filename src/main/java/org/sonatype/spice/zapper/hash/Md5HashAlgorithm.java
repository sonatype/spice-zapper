package org.sonatype.spice.zapper.hash;

import java.security.NoSuchAlgorithmException;

public class Md5HashAlgorithm
    extends AbstractMessageDigestHashAlgorithm
    implements HashAlgorithm
{
  public static final HashAlgorithmIdentifier ID = new HashAlgorithmIdentifier("MD5", 16);

  public Md5HashAlgorithm()
      throws NoSuchAlgorithmException
  {
    super(ID);
  }
}
