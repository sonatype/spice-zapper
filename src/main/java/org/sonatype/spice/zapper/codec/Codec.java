package org.sonatype.spice.zapper.codec;

import java.io.IOException;
import java.io.InputStream;

import org.sonatype.spice.zapper.Identified;


public interface Codec
    extends Identified<CodecIdentifier>
{
    InputStream encode( InputStream inputStream )
        throws IOException;

    InputStream decode( InputStream inputStream )
        throws IOException;
}
