package eu.flatwhite.zapper.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.flatwhite.zapper.Identified;

public interface Codec
    extends Identified
{
    OutputStream encode( OutputStream outputStream )
        throws IOException;

    InputStream decode( InputStream inputStream )
        throws IOException;
}
