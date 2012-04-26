package eu.flatwhite.zapper.client.hc4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.AbstractHttpEntity;

import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.Payload;

public class ZapperEntity
    extends AbstractHttpEntity
{
    private final Payload payload;

    public ZapperEntity( final Payload payload )
    {
        this.payload = Check.notNull( payload, Payload.class );
    }

    @Override
    public boolean isRepeatable()
    {
        return true;
    }

    @Override
    public long getContentLength()
    {
        return payload.getLength();
    }

    @Override
    public InputStream getContent()
        throws IOException, IllegalStateException
    {
        return payload.getContent();
    }

    private final int BUFFER_SIZE = 2048;

    @Override
    public void writeTo( final OutputStream outstream )
        throws IOException
    {
        final InputStream instream = getContent();
        try
        {
            byte[] buffer = new byte[BUFFER_SIZE];
            int l;
            if ( getContentLength() < 0 )
            {
                // consume until EOF
                while ( ( l = instream.read( buffer ) ) != -1 )
                {
                    outstream.write( buffer, 0, l );
                }
            }
            else
            {
                // consume no more than length
                long remaining = getContentLength();
                while ( remaining > 0 )
                {
                    l = instream.read( buffer, 0, (int) Math.min( BUFFER_SIZE, remaining ) );
                    if ( l == -1 )
                    {
                        break;
                    }
                    outstream.write( buffer, 0, l );
                    remaining -= l;
                }
            }
        }
        finally
        {
            instream.close();
        }
    }

    @Override
    public boolean isStreaming()
    {
        return false;
    }
}
