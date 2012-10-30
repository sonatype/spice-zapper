package org.sonatype.spice.zapper.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PayloadSupplierImpl
    implements PayloadSupplier
{
    private final List<SegmentPayload> payloads;

    private final Iterator<SegmentPayload> payloadIterator;

    public PayloadSupplierImpl( final List<SegmentPayload> payloads )
    {
        this.payloads = new ArrayList<SegmentPayload>( Check.notNull( payloads, "Payload list is null!" ).size() );
        this.payloads.addAll( payloads );
        this.payloadIterator = payloads.iterator();
    }

    public synchronized SegmentPayload getNextPayload()
    {
        if ( payloadIterator.hasNext() )
        {
            return payloadIterator.next();
        }
        else
        {
            return null;
        }
    }
}
