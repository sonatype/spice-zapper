package eu.flatwhite.zapper;

import java.io.IOException;
import java.io.InputStream;

public interface IOSource
{
    /**
     * Creates a ZFile belonging to the given path.
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public ZFile createZFile( final Path path )
        throws IOException;

    /**
     * Returns an input stream for reading file bytes belonging to given range of passed in path.
     * 
     * @param path
     * @param range
     * @return
     * @throws IOException
     */
    InputStream readSegment( Path path, Range range )
        throws IOException;

    /**
     * Invoked at very end of transmission to perform some cleanup if needed.
     * 
     * @param sucessful
     * @throws IOException
     */
    void close( boolean successful )
        throws IOException;
}
