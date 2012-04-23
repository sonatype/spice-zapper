package eu.flatwhite.zapper.internal;

import java.io.IOException;
import java.io.InputStream;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Path;

public interface Payload
{
    Identifier getTransferIdentifier();
    
    Path getPath();
    
    long getLength();

    InputStream getContent()
        throws IOException;
}
