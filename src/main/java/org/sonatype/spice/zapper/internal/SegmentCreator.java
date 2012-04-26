package org.sonatype.spice.zapper.internal;

import java.util.List;

import org.sonatype.spice.zapper.ZFile;


public interface SegmentCreator
{
    List<Segment> createSegments( TransferIdentifier transferId, List<ZFile> zfiles );
}
