package org.sonatype.spice.zapper.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.sonatype.spice.zapper.Identified;

public interface Codec
    extends Identified<CodecIdentifier>
{
    OutputStream encode( OutputStream outputStream )
        throws IOException;

    InputStream decode( InputStream inputStream )
        throws IOException;
}
