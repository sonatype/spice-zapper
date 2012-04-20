package eu.flatwhite.zapper.client.ahc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.ning.http.client.Body;
import com.ning.http.client.BodyGenerator;

import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.Payload;

public class ZapperBodyGenerator
    implements BodyGenerator
{
    private final Payload payload;

    public ZapperBodyGenerator( final Payload payload )
    {
        this.payload = Check.notNull( payload, Payload.class );
    }

    @Override
    public Body createBody()
        throws IOException
    {
        return new ZapperBody( payload );
    }

    public static class ZapperBody
        implements Body
    {
        private final Payload payload;

        private final ReadableByteChannel readableByteChannel;

        public ZapperBody( final Payload payload )
            throws IOException
        {
            this.payload = payload;
            this.readableByteChannel = Channels.newChannel( payload.getContent() );
        }

        @Override
        public long getContentLength()
        {
            return payload.getLength();
        }

        @Override
        public long read( final ByteBuffer buffer )
            throws IOException
        {
            return readableByteChannel.read( buffer );
        }

        @Override
        public void close()
            throws IOException
        {
            readableByteChannel.close();
        }
    }
}
