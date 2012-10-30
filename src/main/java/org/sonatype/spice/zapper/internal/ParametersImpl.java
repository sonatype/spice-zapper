package org.sonatype.spice.zapper.internal;

import org.sonatype.spice.zapper.CodecSelector;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.hash.HashAlgorithm;

public class ParametersImpl
    implements Parameters
{
    private final HashAlgorithm hashAlgorithm;

    private final CodecSelector codecSelector;

    private final int maximumTrackCount;

    private final long maximumSegmentLength;

    public ParametersImpl( final HashAlgorithm hashAlgorithm, final CodecSelector codecSelector,
                           final int maximumTrackCount, final long maximumSegmentLength )
    {
        this.hashAlgorithm = Check.notNull( hashAlgorithm, HashAlgorithm.class );
        this.codecSelector = Check.notNull( codecSelector, CodecSelector.class );
        this.maximumTrackCount =
            Check.argument( maximumTrackCount > 0, maximumTrackCount, "maximumTrackCount must be positive!" );
        this.maximumSegmentLength =
            Check.argument( maximumSegmentLength > 0, maximumSegmentLength, "maximumSegmentLength must be positive!" );
    }

    public HashAlgorithm getHashAlgorithm()
    {
        return hashAlgorithm;
    }

    public CodecSelector getCodecSelector()
    {
        return codecSelector;
    }

    public int getMaximumTrackCount()
    {
        return maximumTrackCount;
    }

    public long getMaximumSegmentLength()
    {
        return maximumSegmentLength;
    }
}
