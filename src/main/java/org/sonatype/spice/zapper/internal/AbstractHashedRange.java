package org.sonatype.spice.zapper.internal;

import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.Hashed;

public abstract class AbstractHashedRange
    extends AbstractRange
    implements Hashed
{
    private final Hash hash;

    protected AbstractHashedRange( final long offset, final long length, final Hash hash )
    {
        super( offset, length );
        this.hash = Check.notNull( hash, Hash.class );
    }

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
