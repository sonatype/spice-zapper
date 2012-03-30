package eu.flatwhite.zapper.internal;

import java.util.List;

import eu.flatwhite.zapper.Identified;
import eu.flatwhite.zapper.Identifier;

public class Transfer
    implements Identified
{
    private final Identifier transferId;

    private final long transferTotalSize;

    private List<Track> tracks;

}
