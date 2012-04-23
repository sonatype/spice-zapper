package eu.flatwhite.zapper.internal.wholefile;

import java.util.ArrayList;
import java.util.List;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.internal.AbstractIdentified;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadCreator;
import eu.flatwhite.zapper.internal.Segment;
import eu.flatwhite.zapper.internal.SegmentPayload;

/**
 * Creates Payloads that are actually whole ZFiles.
 * 
 * @author cstamas
 */
public class WholeZFilePayloadCreator
    extends AbstractIdentified<Identifier>
    implements PayloadCreator
{
    public WholeZFilePayloadCreator( final Identifier identifier )
    {
        super( identifier );
    }

    @Override
    public List<Payload> createPayloads( IOSource source, List<Segment> segments )
    {
        final ArrayList<Payload> payloads = new ArrayList<Payload>( segments.size() );
        for ( Segment segment : segments )
        {
            payloads.add( new SegmentPayload( getIdentifier(), segment.getZFile().getIdentifier(), segment, source ) );
        }
        return payloads;
    }
}
