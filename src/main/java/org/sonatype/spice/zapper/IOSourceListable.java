package org.sonatype.spice.zapper;

import java.io.IOException;
import java.util.List;

/**
 * Listable source.
 *
 * @author cstamas
 */
public interface IOSourceListable
    extends IOSource
{
  List<ZFile> listFiles()
      throws IOException;
}
