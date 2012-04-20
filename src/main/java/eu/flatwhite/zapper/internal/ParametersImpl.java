package eu.flatwhite.zapper.internal;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.codec.Codec;
import eu.flatwhite.zapper.codec.CodecIdentifier;
import eu.flatwhite.zapper.hash.HashAlgorithm;
import eu.flatwhite.zapper.hash.HashAlgorithmIdentifier;
import eu.flatwhite.zapper.hash.Sha1HashAlgorithm;

public class ParametersImpl
    implements Parameters
{
    private final Map<HashAlgorithmIdentifier, HashAlgorithm> hashAlgorithms;

    public ParametersImpl()
        throws NoSuchAlgorithmException
    {
        super();
        this.hashAlgorithms = new HashMap<HashAlgorithmIdentifier, HashAlgorithm>( 1 );
        this.hashAlgorithms.put( Sha1HashAlgorithm.ID, new Sha1HashAlgorithm() );
    }

    @Override
    public Map<HashAlgorithmIdentifier, HashAlgorithm> getHashAlgorithms()
    {
        return hashAlgorithms;
    }

    @Override
    public Map<CodecIdentifier, Codec> getCodecs()
    {
        return Collections.emptyMap();
    }

    @Override
    public int getMaximumSessionCount()
    {
        return 6;
    }

    @Override
    public long getMaximumSegmentLength()
    {
        return 1024L;
    }
}
