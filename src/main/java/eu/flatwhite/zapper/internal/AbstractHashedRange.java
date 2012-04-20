package eu.flatwhite.zapper.internal;

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

    protected AbstractHashedRange( final long offset, final long length, final Hash hash )
    {
        super( offset, length );
        this.hash = Check.notNull( hash, "Hash is null!" );
    }

    @Override
    public Hash getHash()
    {
        return hash;
    }

    // ==

    @Override
    public String toString()
    {
        return super.toString() + "(hash=" + getHash() + ")";
    }
}
