package eu.flatwhite.zapper.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PayloadSupplierImpl
    implements PayloadSupplier
{
    private final List<Payload> payloads;

    private final Iterator<Payload> payloadIterator;

    public PayloadSupplierImpl( final List<Payload> payloads )
    {
        this.payloads = new ArrayList<Payload>( Check.notNull( payloads, "Payload list is null!" ).size() );
        this.payloads.addAll( payloads );
        this.payloadIterator = payloads.iterator();
    }

    @Override
    public Payload getNextPayload()
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
