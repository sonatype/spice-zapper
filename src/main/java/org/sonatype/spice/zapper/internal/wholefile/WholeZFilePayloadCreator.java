package org.sonatype.spice.zapper.internal.wholefile;

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
 * Creates Payloads that are actually whole ZFiles, without any extra fluff.
 * 
 * @author cstamas
 */
public class WholeZFilePayloadCreator
    implements PayloadCreator
{
    private final Parameters parameters;

    public WholeZFilePayloadCreator( final Parameters parameters )
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

    protected SegmentPayload createPayload( final Transfer transfer, final Segment segment, final IOSource source,
                                            final String remoteUrl )
        throws IOException
    {
        return new SegmentPayload( transfer.getIdentifier(), segment.getZFile().getIdentifier(), segment, source,
            segment.getZFile().getHash(), parameters.getCodecSelector().selectCodecs( segment.getZFile() ) );
    }
}
