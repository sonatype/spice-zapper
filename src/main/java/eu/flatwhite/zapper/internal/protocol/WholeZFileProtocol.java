package eu.flatwhite.zapper.internal.protocol;

import eu.flatwhite.zapper.internal.PayloadCreator;
import eu.flatwhite.zapper.internal.Protocol;
import eu.flatwhite.zapper.internal.SegmentCreator;
import eu.flatwhite.zapper.internal.payload.WholeZFilePayloadCreator;
import eu.flatwhite.zapper.internal.segmenter.WholeZFileSegmentCreator;

public class WholeZFileProtocol
    implements Protocol
{
    @Override
    public SegmentCreator getSegmentCreator()
    {
        return new WholeZFileSegmentCreator();
    }

    @Override
    public PayloadCreator getPayloadCreator()
    {
        return new WholeZFilePayloadCreator();
    }
}
