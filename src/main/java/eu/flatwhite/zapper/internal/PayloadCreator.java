package eu.flatwhite.zapper.internal;

import java.util.List;

import eu.flatwhite.zapper.IOSource;

public interface PayloadCreator
{
    List<Payload> createPayloads( TransferIdentifier transferId, IOSource source, List<Segment> segments, String remoteUrl );
}
