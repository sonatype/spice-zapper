package eu.flatwhite.zapper;

import java.io.IOException;
import java.util.List;

public interface IOSourceListable
    extends IOSource
{
    List<ZFile> listFiles()
        throws IOException;
}
