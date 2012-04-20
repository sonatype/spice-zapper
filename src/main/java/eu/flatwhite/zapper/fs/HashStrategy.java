package eu.flatwhite.zapper.fs;

import java.io.File;
import java.io.IOException;

import eu.flatwhite.zapper.hash.Hash;
import eu.flatwhite.zapper.hash.HashAlgorithm;

public interface HashStrategy
{
    HashAlgorithm getHashAlgorithm();

    Hash getHashFor( final File file )
        throws IOException;
}
