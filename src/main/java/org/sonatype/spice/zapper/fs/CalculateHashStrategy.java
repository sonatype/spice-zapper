package org.sonatype.spice.zapper.fs;

import java.io.File;
import java.io.IOException;

import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.HashAlgorithm;
import org.sonatype.spice.zapper.hash.HashUtils;
import org.sonatype.spice.zapper.internal.Check;

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
