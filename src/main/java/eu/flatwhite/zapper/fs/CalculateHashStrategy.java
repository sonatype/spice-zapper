package eu.flatwhite.zapper.fs;

import java.io.File;
import java.io.IOException;

import eu.flatwhite.zapper.hash.Hash;
import eu.flatwhite.zapper.hash.HashAlgorithm;
import eu.flatwhite.zapper.hash.HashUtils;
import eu.flatwhite.zapper.internal.Check;

public class CalculateHashStrategy
    implements HashStrategy
{
    private final HashAlgorithm hashAlgorithm;

    public CalculateHashStrategy( final HashAlgorithm hashAlgorithm )
    {
        this.hashAlgorithm = Check.notNull( hashAlgorithm, HashAlgorithm.class );
    }

    @Override
    public HashAlgorithm getHashAlgorithm()
    {
        return hashAlgorithm;
    }

    @Override
    public Hash getHashFor( final File file )
        throws IOException
    {
        return HashUtils.getDigest( Check.notNull( hashAlgorithm, HashAlgorithm.class ),
            Check.notNull( file, File.class ) );
    }
}
