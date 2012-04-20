package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identified;
import eu.flatwhite.zapper.Identifier;

public class AbstractIdentified<I extends Identifier>
    implements Identified<I>
{
    private final I identifier;

    public AbstractIdentified( final I identifier )
    {
        this.identifier = Check.notNull( identifier, "Identifier is null!" );
    }

    @Override
    public I getIdentifier()
    {
        return identifier;
    }
}
