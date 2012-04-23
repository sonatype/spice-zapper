package eu.flatwhite.zapper.fs;

import java.io.File;
import java.io.IOException;

import eu.flatwhite.zapper.hash.Hash;
import eu.flatwhite.zapper.hash.HashAlgorithm;
import eu.flatwhite.zapper.hash.HashUtils;
import eu.flatwhite.zapper.hash.Sha1HashAlgorithm;
import eu.flatwhite.zapper.internal.Check;

public class MavenHashStrategy
    implements HashStrategy
{
    private final HashAlgorithm hashAlgorithm;

    public MavenHashStrategy( final HashAlgorithm hashAlgorithm )
    {
        this.hashAlgorithm = Check.notNull( hashAlgorithm, HashAlgorithm.class );
        if ( !Sha1HashAlgorithm.ID.equals( hashAlgorithm.getIdentifier() ) )
        {
            throw new IllegalArgumentException( "Maven repository layout supports SHA1 hashes only!" );
        }
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
