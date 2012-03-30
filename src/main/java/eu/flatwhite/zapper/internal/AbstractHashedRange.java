package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Range;
import eu.flatwhite.zapper.hash.Hash;
import eu.flatwhite.zapper.hash.Hashed;

public abstract class AbstractHashedRange
    extends AbstractRange
    implements Hashed
{
    private final Hash hash;

    protected AbstractHashedRange( final Range range, final Hash hash )
    {
        super( range );
        this.hash = Check.notNull( hash, "Hash is null!" );
    }

    protected AbstractHashedRange( final Identifier identifier, final long offset, final long length, final Hash hash )
    {
        super( identifier, offset, length );
        this.hash = Check.notNull( hash, "Hash is null!" );
    }

    @Override
    public Hash getHash()
    {
        return hash;
    }
}
