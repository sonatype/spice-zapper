package org.sonatype.spice.zapper.internal;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NonClosingInputStream
    extends FilterInputStream
{
    public NonClosingInputStream( final InputStream in )
    {
        super( in );
    }

    @Override
    public void close()
        throws IOException
    {
        // not closing it
    }
}
