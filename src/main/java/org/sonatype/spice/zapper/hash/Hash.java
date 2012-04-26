package org.sonatype.spice.zapper.hash;

import org.sonatype.spice.zapper.Identifier;

public interface Hash
    extends Identifier
{
    HashAlgorithmIdentifier getHashAlgorithmIdentifier();

    byte[] getHash();
}
