package org.sonatype.spice.zapper.internal;

import org.sonatype.spice.zapper.Identified;
import org.sonatype.spice.zapper.Identifier;

public class AbstractIdentified<I extends Identifier>
    implements Identified<I>
{
    private final I identifier;

    public AbstractIdentified( final I identifier )
    {
        this.identifier = Check.notNull( identifier, "Identifier is null!" );
    }

    public I getIdentifier()
    {
        return identifier;
    }
}
