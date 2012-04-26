package org.sonatype.spice.zapper.internal.wholefile;

import java.util.ArrayList;
import java.util.List;

import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.PayloadCreator;
import org.sonatype.spice.zapper.internal.Segment;
import org.sonatype.spice.zapper.internal.SegmentPayload;
import org.sonatype.spice.zapper.internal.TransferIdentifier;


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
