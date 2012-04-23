package eu.flatwhite.zapper.internal;

import java.io.IOException;
import java.io.InputStream;

import eu.flatwhite.zapper.Identifier;

public interface Payload
{
    Identifier getTransferIdentifier();

    String getUrl();

    long getLength();

    InputStream getContent()
        throws IOException;
}
