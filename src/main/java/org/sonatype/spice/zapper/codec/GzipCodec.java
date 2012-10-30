package org.sonatype.spice.zapper.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.sonatype.spice.zapper.internal.AbstractIdentified;

public class GzipCodec
    extends AbstractIdentified<CodecIdentifier>
    implements Codec
{
    public static final CodecIdentifier ID = new CodecIdentifier( "gzip" );

    public GzipCodec()
    {
        super( ID );
    }

    public OutputStream encode( final OutputStream outputStream )
        throws IOException
    {
        return new GZIPOutputStream( outputStream );
    }

    public InputStream decode( InputStream inputStream )
        throws IOException
    {
        return new GZIPInputStream( inputStream );
    }
}
