package org.sonatype.spice.zapper.internal;

import java.io.IOException;

import org.sonatype.spice.zapper.IOSource;

public interface PayloadCreator
{
    int createPayloads( Transfer transfer, IOSource source, String remoteUrl )
        throws IOException;
}
