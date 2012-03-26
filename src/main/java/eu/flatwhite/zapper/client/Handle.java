package eu.flatwhite.zapper.client;

import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;

public interface Handle
{
    void upload( ZFile... files );

    void download( Path... paths );
}
