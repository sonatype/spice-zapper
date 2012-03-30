package eu.flatwhite.zapper.hash;

import java.io.FilterOutputStream;
import java.io.OutputStream;

public abstract class HashingOutputStream
    extends FilterOutputStream
{
    protected HashingOutputStream( final OutputStream out )
    {
        super( out );
    }

    public abstract Hash getHash();
}
