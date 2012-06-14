package org.sonatype.spice.zapper.internal.zapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.PayloadCreator;
import org.sonatype.spice.zapper.internal.Segment;
import org.sonatype.spice.zapper.internal.SegmentPayload;
import org.sonatype.spice.zapper.internal.Transfer;

/**
 * Zapper
 * 
 * @author cstamas
 */
public class ZapperPayloadCreator
    implements PayloadCreator
{
    private final Parameters parameters;

    public ZapperPayloadCreator( final Parameters parameters )
    {
        this.parameters = parameters;
    }

    @Override
    public int createPayloads( final Transfer transfer, final IOSource source, final String remoteUrl )
        throws IOException
    {
        final List<Segment> segments = transfer.getSegments();
        final ArrayList<SegmentPayload> payloads = new ArrayList<SegmentPayload>( segments.size() );
        for ( Segment segment : segments )
        {
            payloads.add( createPayload( transfer, segment, source, remoteUrl ) );
        }
        transfer.setPayloads( payloads );
        return payloads.size();
    }

    protected ZapperPayload createPayload( final Transfer transfer, final Segment segment, final IOSource source,
                                           final String remoteUrl )
        throws IOException
    {
        return new ZapperPayload( transfer.getIdentifier(), segment.getZFile().getIdentifier(), segment, source,
            parameters.getHashAlgorithm() );
    }
}
