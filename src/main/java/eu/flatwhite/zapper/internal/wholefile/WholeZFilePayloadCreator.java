package eu.flatwhite.zapper.internal.wholefile;

import java.util.ArrayList;
import java.util.List;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadCreator;
import eu.flatwhite.zapper.internal.Segment;
import eu.flatwhite.zapper.internal.SegmentPayload;
import eu.flatwhite.zapper.internal.TransferIdentifier;

/**
 * Creates Payloads that are actually whole ZFiles.
 * 
 * @author cstamas
 */
public class WholeZFilePayloadCreator
    implements PayloadCreator
{
    @Override
    public List<Payload> createPayloads( final TransferIdentifier transferId, final IOSource source,
                                         final List<Segment> segments, final String remoteUrl )
    {
        final ArrayList<Payload> payloads = new ArrayList<Payload>( segments.size() );
        for ( Segment segment : segments )
        {
            payloads.add( new SegmentPayload( transferId, segment.getZFile().getIdentifier(), segment, source ) );
        }
        return payloads;
    }
}
