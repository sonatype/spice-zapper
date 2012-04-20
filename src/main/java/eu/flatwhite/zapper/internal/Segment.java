package eu.flatwhite.zapper.internal;

import java.util.Arrays;
import java.util.List;

import eu.flatwhite.zapper.Range;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.codec.Codec;

public class Segment
    extends AbstractRange
{
    private final ZFile zfile;

    private final List<Codec> segmentFilters;

    public Segment( final ZFile zfile, final Codec... segmentFilters )
    {
        this( zfile, zfile, segmentFilters );
    }

    public Segment( final Range range, final ZFile zfile, final Codec... segmentFilters )
    {
        super( range );
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
