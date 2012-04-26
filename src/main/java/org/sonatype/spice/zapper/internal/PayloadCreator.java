package org.sonatype.spice.zapper.internal;

import java.util.List;

import org.sonatype.spice.zapper.IOSource;


public interface PayloadCreator
{
    List<Payload> createPayloads( TransferIdentifier transferId, IOSource source, List<Segment> segments, String remoteUrl );
}
