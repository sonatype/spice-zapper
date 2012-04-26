package org.sonatype.spice.zapper.hash;

import java.io.FilterInputStream;
import java.io.InputStream;

public abstract class HashingInputStream
    extends FilterInputStream
{
    protected HashingInputStream( final InputStream in )
    {
        super( in );
    }

    public abstract Hash getHash();
}
