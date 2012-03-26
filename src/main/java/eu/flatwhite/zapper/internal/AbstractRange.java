package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Range;

public abstract class AbstractRange
    implements Range
{
    private final Identifier identifier;

    private final long offset;

    private final long length;

    protected AbstractRange( final Identifier identifier, final long offset, final long length )
    {
        if ( identifier == null )
        {
            throw new NullPointerException( "Identifier is null!" );
        }
        if ( offset < 0 )
        {
            throw new IllegalArgumentException( "Offest is less than zero!" );
        }
        if ( length < 1 )
        {
            throw new IllegalArgumentException( "Length is greater than zero!" );
        }

        this.identifier = identifier;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public Identifier getIdentifier()
    {
        return identifier;
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
}
