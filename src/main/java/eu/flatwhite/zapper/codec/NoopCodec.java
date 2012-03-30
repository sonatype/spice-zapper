package eu.flatwhite.zapper.codec;

import java.io.InputStream;
import java.io.OutputStream;

import eu.flatwhite.zapper.internal.AbstractIdentified;
import eu.flatwhite.zapper.internal.StringIdentifier;

public class NoopCodec
    extends AbstractIdentified
    implements Codec
{
    public NoopCodec()
    {
        super( new StringIdentifier( "noop" ) );
    }

    @Override
    public OutputStream encode( OutputStream outputStream )
    {
        return outputStream;
    }

    @Override
    public InputStream decode( InputStream inputStream )
    {
        return inputStream;
    }
}
