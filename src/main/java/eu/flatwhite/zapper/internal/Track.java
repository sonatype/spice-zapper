package eu.flatwhite.zapper.internal;

import java.util.List;

import eu.flatwhite.zapper.Identified;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.codec.Codec;

public class Track
    implements Identified
{
    private final Identifier transferId;

    private final Identifier trackId;

    private final int tracksegmentCount;

    private final List<Codec> trackFilters;

    private final List<TrackSegment> trackSegments;

    @Override
    public Identifier getIdentifier()
    {
        return trackId;
    }

    public Identifier getTransferId()
    {
        return transferId;
    }

    public int getTracksegmentCount()
    {
        return tracksegmentCount;
    }

    public List<Codec> getTrackFilters()
    {
        return trackFilters;
    }

    public List<TrackSegment> getTrackSegments()
    {
        return trackSegments;
    }
}
