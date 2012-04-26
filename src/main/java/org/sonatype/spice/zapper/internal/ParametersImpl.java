package org.sonatype.spice.zapper.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.codec.CodecIdentifier;
import org.sonatype.spice.zapper.hash.HashAlgorithm;
import org.sonatype.spice.zapper.hash.HashAlgorithmIdentifier;


public class ParametersImpl
    implements Parameters
{
    private final Map<HashAlgorithmIdentifier, HashAlgorithm> hashAlgorithms;

    private final Map<CodecIdentifier, Codec> codecs;

    private final int maximumTrackCount;

    private final long maximumSegmentLength;

    public ParametersImpl( final Map<HashAlgorithmIdentifier, HashAlgorithm> hashAlgorithms,
                           final Map<CodecIdentifier, Codec> codecs, final int maximumTrackCount,
                           final long maximumSegmentLength )
    {
        this.hashAlgorithms =
            Collections.unmodifiableMap( new HashMap<HashAlgorithmIdentifier, HashAlgorithm>( hashAlgorithms ) );
        this.codecs = Collections.unmodifiableMap( new HashMap<CodecIdentifier, Codec>( codecs ) );
        this.maximumTrackCount = maximumTrackCount;
        this.maximumSegmentLength = maximumSegmentLength;
    }

    @Override
    public Map<HashAlgorithmIdentifier, HashAlgorithm> getHashAlgorithms()
    {
        return hashAlgorithms;
    }

    @Override
    public Map<CodecIdentifier, Codec> getCodecs()
    {
        return codecs;
    }

    @Override
    public int getMaximumTrackCount()
    {
        return maximumTrackCount;
    }

    @Override
    public long getMaximumSegmentLength()
    {
        return maximumSegmentLength;
    }
}
