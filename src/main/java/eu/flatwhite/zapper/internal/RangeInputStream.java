package eu.flatwhite.zapper.internal;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import eu.flatwhite.zapper.Range;

public class RangeInputStream
    extends FilterInputStream
{
    private final Range range;

    private long allowedToRead;

    private boolean allowedToClose;

    public RangeInputStream( final InputStream wrappedStream, final Range range )
        throws IOException
    {
        this( wrappedStream, range, false, false );
    }

    public RangeInputStream( final InputStream wrappedStream, final Range range, final boolean doSkip,
                             final boolean allowedToClose )
        throws IOException
    {
        super( wrappedStream );
        if ( range == null )
        {
            throw new NullPointerException( "Range is null!" );
        }

        this.range = range;
        this.allowedToRead = range.getLength();
        this.allowedToClose = allowedToClose;
        if ( doSkip && range.getOffset() > 0 )
        {
            super.skip( range.getOffset() );
        }
    }

    public Range getRange()
    {
        return range;
    }

    @Override
    public int available()
        throws IOException
    {
        return mathMin( super.available(), allowedToRead );
    }

    @Override
    public int read()
        throws IOException
    {
        if ( allowedToRead <= 0 )
        {
            return -1;
        }
        final int result = super.read();
        if ( result >= 0 )
        {
            --allowedToRead;
        }
        return result;
    }

    @Override
    public int read( byte b[], int off, int len )
        throws IOException
    {
        if ( allowedToRead <= 0 )
        {
            return -1;
        }
        int lenToRead = mathMin( len, allowedToRead );
        final int result = super.read( b, off, lenToRead );
        if ( result >= 0 )
        {
            allowedToRead -= result;
        }
        return result;
    }

    @Override
    public long skip( final long n )
        throws IOException
    {
        final long result = super.skip( Math.min( n, allowedToRead ) );
        if ( result >= 0 )
        {
            allowedToRead -= result;
        }
        return result;
    }

    @Override
    public void close()
        throws IOException
    {
        if ( allowedToClose )
        {
            super.close();
        }
    }

    // ==

    protected int mathMin( int a, long b )
    {
        if ( b < Integer.MAX_VALUE )
        {
            return Math.min( a, (int) b );
        }
        else
        {
            return a;
        }
    }
}
