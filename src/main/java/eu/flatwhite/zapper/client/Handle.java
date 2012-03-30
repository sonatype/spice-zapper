package eu.flatwhite.zapper.client;

import java.io.IOException;

import eu.flatwhite.zapper.IO;
import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;

public interface Handle
{
    void upload( IO io, ZFile... files )
        throws IOException;

    void upload( IOSource source )
        throws IOException;

    void download( IOTarget target, Path... paths )
        throws IOException;
}
