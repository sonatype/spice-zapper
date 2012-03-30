package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identified;
import eu.flatwhite.zapper.Identifier;

public class AbstractIdentified
    implements Identified
{
    private final Identifier identifier;

    public AbstractIdentified( final Identifier identifier )
    {
        this.identifier = Check.notNull( identifier, "Identifier is null!" );
    }

    @Override
    public Identifier getIdentifier()
    {
        return identifier;
    }
}
