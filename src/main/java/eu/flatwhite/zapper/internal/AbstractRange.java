package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Range;

public abstract class AbstractRange
    implements Range
{
    private final long offset;

    private final long length;

    protected AbstractRange( final Range range )
    {
        this( range.getOffset(), range.getLength() );
    }

    protected AbstractRange( final long offset, final long length )
    {
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

    @Override
    public boolean overlaps( final Range range )
    {
        final long myStart = getOffset();
        final long myEnd = myStart + getLength(); // we know myStart < myEnd
        final long hisStart = range.getOffset();
        final long hisEnd = hisStart + range.getLength(); // we know hisStart < hisEnd

        // we check for "is before" or "is after" and negate the result
        return !( myEnd < hisStart || myStart > hisEnd );
    }

    // ==

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "(offset=" + getOffset() + ", length=" + getLength() + ")";
    }
}
