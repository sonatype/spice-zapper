package eu.flatwhite.zapper.client;

import java.io.IOException;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.IOSourceListable;
import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Path;

public interface Client
{
    /**
     * Uploads all that listable source lists.
     * 
     * @param listableSource
     * @throws IOException
     */
    void upload( IOSourceListable listableSource )
        throws IOException;

    /**
     * Uploads given paths from the source.
     * 
     * @param source
     * @param paths
     * @throws IOException
     */
    void upload( IOSource source, Path... paths )
        throws IOException;

    /**
     * Downloads given paths into the target.
     * 
     * @param target
     * @param paths
     * @throws IOException
     */
    void download( IOTarget target, Path... paths )
        throws IOException;

    /**
     * Should be called as last step, to make client able to cleanup resources if needed.
     */
    void close();
}
