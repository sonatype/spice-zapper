package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Range;

public class RangeImpl
    extends AbstractRange
{
    public RangeImpl( final Range range )
    {
        super( range );
    }

    public RangeImpl( final long offset, final long length )
    {
        super( offset, length );
    }
}
