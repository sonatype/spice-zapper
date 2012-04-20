package eu.flatwhite.zapper.client;

import java.io.IOException;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.IOSourceListable;
import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Path;

public interface Client
{
    void upload( IOSourceListable listableSource )
        throws IOException;

    void upload( IOSource source, Path... paths )
        throws IOException;

    void download( IOTarget target, Path... paths )
        throws IOException;

    void close();
}
