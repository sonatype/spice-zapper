package eu.flatwhite.zapper.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import eu.flatwhite.zapper.internal.AbstractIdentified;
import eu.flatwhite.zapper.internal.StringIdentifier;

public class GzipCodec
    extends AbstractIdentified
    implements Codec
{
    public GzipCodec()
    {
        super( new StringIdentifier( "gzip" ) );
    }

    @Override
    public OutputStream encode( OutputStream outputStream )
        throws IOException
    {
        return new GZIPOutputStream( outputStream );
    }

    @Override
    public InputStream decode( InputStream inputStream )
        throws IOException
    {
        return new GZIPInputStream( inputStream );
    }
}
