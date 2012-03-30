package eu.flatwhite.zapper;

import java.io.IOException;
import java.io.InputStream;

public interface IO
{
    InputStream readFileSegment( ZFile zfile, Range range )
        throws IOException;
}
