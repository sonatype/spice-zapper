package org.sonatype.spice.zapper.internal.transport;

import java.util.concurrent.Callable;

import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.PayloadSupplier;

public abstract class AbstractChargerTrack
    extends Track
    implements Callable<State>
{
    final PayloadSupplier payloadSupplier;

    public AbstractChargerTrack( final TrackIdentifier identifier, final PayloadSupplier payloadSupplier )
    {
        super( identifier );
        this.payloadSupplier = Check.notNull( payloadSupplier, PayloadSupplier.class );
    }

    protected PayloadSupplier getPayloadSupplier()
    {
        return payloadSupplier;
    }
}
