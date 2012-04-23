package eu.flatwhite.zapper;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import eu.flatwhite.zapper.codec.Codec;
import eu.flatwhite.zapper.codec.CodecIdentifier;
import eu.flatwhite.zapper.hash.HashAlgorithm;
import eu.flatwhite.zapper.hash.HashAlgorithmIdentifier;
import eu.flatwhite.zapper.hash.Sha1HashAlgorithm;
import eu.flatwhite.zapper.internal.ParametersImpl;

public class ParametersBuilder
{
    private Map<HashAlgorithmIdentifier, HashAlgorithm> hashAlgorithms;

    private Map<CodecIdentifier, Codec> codecs;

    private int maximumTrackCount;

    private long maximumSegmentLength;

    public ParametersBuilder()
    {
        this.hashAlgorithms = new HashMap<HashAlgorithmIdentifier, HashAlgorithm>();
        this.codecs = new HashMap<CodecIdentifier, Codec>();
        this.maximumTrackCount = 6;
        this.maximumSegmentLength = 1073741824L; // 1MB
    }

    public ParametersBuilder addHashAlgorithm( final HashAlgorithm hashAlgorithm )
    {
        this.hashAlgorithms.put( hashAlgorithm.getIdentifier(), hashAlgorithm );
        return this;
    }

    public ParametersBuilder addCodec( final Codec codec )
    {
        this.codecs.put( codec.getIdentifier(), codec );
        return this;
    }

    public ParametersBuilder setMaximumTrackCount( final int maximumTrackCount )
    {
        this.maximumTrackCount = maximumTrackCount;
        return this;
    }

    public ParametersBuilder setMaximumSegmentLength( final long maximumSegmentLength )
    {
        this.maximumSegmentLength = maximumSegmentLength;
        return this;
    }

    public Parameters build()
    {
        return new ParametersImpl( hashAlgorithms, codecs, maximumTrackCount, maximumSegmentLength );
    }

    public static ParametersBuilder defaults()
        throws NoSuchAlgorithmException
    {
        // sha1 hash algorithm + 6 tracks + 1MB segments
        return new ParametersBuilder().addHashAlgorithm( new Sha1HashAlgorithm() ).setMaximumTrackCount( 6 ).setMaximumSegmentLength(
            1073741824L );
    }
}
