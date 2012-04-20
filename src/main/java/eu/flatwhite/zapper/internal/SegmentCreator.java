package eu.flatwhite.zapper.internal;

import java.util.List;

import eu.flatwhite.zapper.ZFile;

public interface SegmentCreator
{
    List<Segment> createSegments( List<ZFile> zfiles );
}
