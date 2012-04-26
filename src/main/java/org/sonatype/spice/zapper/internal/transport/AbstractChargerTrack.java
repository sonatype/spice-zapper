package org.sonatype.spice.zapper.internal.transport;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.PayloadSupplier;
import org.sonatype.spice.zapper.internal.Protocol;


public abstract class AbstractChargerTrack
    implements Callable<State>
{
    private final Logger logger;

    private final Protocol protocol;

    private final int trackNo;

    final PayloadSupplier payloadSupplier;

    public AbstractChargerTrack( final Protocol protocol, final int trackNo, final PayloadSupplier payloadSupplier )
    {
        this.logger = LoggerFactory.getLogger( getClass() + "-" + trackNo );
        this.protocol = Check.notNull( protocol, Protocol.class );
        this.trackNo = trackNo;
        this.payloadSupplier = Check.notNull( payloadSupplier, PayloadSupplier.class );
    }

    protected Logger getLogger()
    {
        return logger;
    }

    protected Protocol getProtocol()
    {
        return protocol;
    }

    protected int getTrackNo()
    {
        return trackNo;
    }

    protected PayloadSupplier getPayloadSupplier()
    {
        return payloadSupplier;
    }
}
