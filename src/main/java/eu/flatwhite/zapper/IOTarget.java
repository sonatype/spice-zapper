package eu.flatwhite.zapper;

import java.io.IOException;
import java.io.InputStream;

public interface IOTarget
    extends IO
{
    long freeSpace();

    InputStream readSegment( Identifier identifier )
        throws IOException;

    void writeSegment( Range range, InputStream inputStream )
        throws IOException;
}
