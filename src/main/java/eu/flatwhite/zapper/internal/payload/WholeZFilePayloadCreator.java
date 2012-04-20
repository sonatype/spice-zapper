package eu.flatwhite.zapper.internal.payload;

import java.util.ArrayList;
import java.util.List;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadCreator;
import eu.flatwhite.zapper.internal.Segment;
import eu.flatwhite.zapper.internal.SegmentPayload;

public class WholeZFilePayloadCreator
    implements PayloadCreator
{
    @Override
    public List<Payload> createPayloads( IOSource source, List<Segment> segments )
    {
        final ArrayList<Payload> payloads = new ArrayList<Payload>( segments.size() );
        for ( Segment segment : segments )
        {
            payloads.add( new SegmentPayload( segment.getZFile().getIdentifier(), segment, source ) );
        }
        return payloads;
    }
}
