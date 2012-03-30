package eu.flatwhite.zapper.internal;

import java.util.List;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.ZFileRange;
import eu.flatwhite.zapper.codec.Codec;

public class TrackSegment
    extends AbstractRange
{
    private final Identifier transferId;

    private final Identifier trackId;

    private final ZFile zFile;

    private final List<Codec> segmentFilters;

    public TrackSegment( final Identifier transferId, final Identifier trackId, final ZFileRange zFileRange,
                         final List<Codec> segmentFilters )
    {
        super( zFileRange );
        this.transferId = transferId;
        this.trackId = trackId;
        this.zFile = zFileRange.getZFile();
        this.segmentFilters = segmentFilters;
    }

    public Identifier getTransferId()
    {
        return transferId;
    }

    public Identifier getTrackId()
    {
        return trackId;
    }

    public ZFile getZFile()
    {
        return zFile;
    }

    public List<Codec> getSegmentFilters()
    {
        return segmentFilters;
    }
}
