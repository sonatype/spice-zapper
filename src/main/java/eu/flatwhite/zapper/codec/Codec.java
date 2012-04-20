package eu.flatwhite.zapper.codec;

import java.io.IOException;
import java.io.InputStream;

import eu.flatwhite.zapper.Identified;

public interface Codec
    extends Identified<CodecIdentifier>
{
    InputStream encode( InputStream inputStream )
        throws IOException;

    InputStream decode( InputStream inputStream )
        throws IOException;
}
