package org.sonatype.spice.zapper.internal;

import java.io.IOException;
import java.io.InputStream;

import org.sonatype.spice.zapper.Path;


public interface Payload
{
    TransferIdentifier getTransferIdentifier();

    Path getPath();

    long getLength();

    InputStream getContent()
        throws IOException;
}
