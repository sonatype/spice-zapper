package eu.flatwhite.zapper.internal.transport;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.internal.AbstractIdentified;

public abstract class AbstractChargerTrack
    extends AbstractIdentified<Identifier>
    implements Callable<State>
{
    private final Logger logger;

    public AbstractChargerTrack( final Identifier identifier )
    {
        super( identifier );
        this.logger = LoggerFactory.getLogger( getClass() + "-" + getIdentifier().stringValue() );
    }

    protected Logger getLogger()
    {
        return logger;
    }
}
