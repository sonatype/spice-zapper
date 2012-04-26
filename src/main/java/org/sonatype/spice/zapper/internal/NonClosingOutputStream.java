package org.sonatype.spice.zapper.internal;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NonClosingOutputStream
    extends FilterOutputStream
{
    public NonClosingOutputStream( final OutputStream out )
    {
        super( out );
    }

    @Override
    public void close()
        throws IOException
    {
        // not closing it
    }
}
