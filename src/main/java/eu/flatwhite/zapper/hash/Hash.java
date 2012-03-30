package eu.flatwhite.zapper.hash;

import eu.flatwhite.zapper.Identifier;

public interface Hash
    extends Identifier
{
    HashAlgorithm getAlgorithm();

    byte[] getHash();
}
