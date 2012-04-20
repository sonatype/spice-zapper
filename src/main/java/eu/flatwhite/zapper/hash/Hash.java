package eu.flatwhite.zapper.hash;

import eu.flatwhite.zapper.Identifier;

public interface Hash
    extends Identifier
{
    HashAlgorithmIdentifier getHashAlgorithmIdentifier();

    byte[] getHash();
}
