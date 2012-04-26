package org.sonatype.spice.zapper.client.ahc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.Payload;

import com.ning.http.client.Body;
import com.ning.http.client.BodyGenerator;


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
