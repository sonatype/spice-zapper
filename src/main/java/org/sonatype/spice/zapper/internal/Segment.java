package org.sonatype.spice.zapper.internal;

import java.util.Arrays;
import java.util.List;

import org.sonatype.spice.zapper.ZFile;
import org.sonatype.spice.zapper.codec.Codec;


public class Segment
    extends AbstractRange
{
    private final ZFile zfile;

    private final List<Codec> segmentFilters;

    public Segment( final long offset, final long length, final ZFile zfile, final Codec... segmentFilters )
    {
        super( offset, length );
        this.zfile = zfile;
        this.segmentFilters = Arrays.asList( segmentFilters );
    }

    public ZFile getZFile()
    {
        return zfile;
    }

    public List<Codec> getSegmentFilters()
    {
        return segmentFilters;
    }
}
