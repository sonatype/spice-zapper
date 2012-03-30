package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Range;

public abstract class AbstractRange
    extends AbstractIdentified
    implements Range
{
    private final long offset;

    private final long length;

    protected AbstractRange( final Range range )
    {
        this( range.getIdentifier(), range.getOffset(), range.getLength() );
    }

    protected AbstractRange( final Identifier identifier, final long offset, final long length )
    {
        super( identifier );
        this.offset = Check.argument( offset >= 0, offset, "Offset is less than 0!" );
        this.length = Check.argument( length > 0, length, "Length is less than 1!" );
    }

    @Override
    public long getOffset()
    {
        return offset;
    }

    @Override
    public long getLength()
    {
        return length;
    }

    @Override
    public boolean matches( final Range range )
    {
        return ( getOffset() == range.getOffset() && getLength() == range.getLength() );
    }

    @Override
    public boolean contains( final Range range )
    {
        return ( getOffset() <= range.getOffset() && getLength() >= range.getLength() );
    }
}
