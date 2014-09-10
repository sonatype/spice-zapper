package org.sonatype.spice.zapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Content source when uploading.
 *
 * @author cstamas
 */
public interface IOSource
{
  /**
   * Creates a ZFile belonging to the given path.
   */
  ZFile createZFile(final Path path)
      throws IOException;

  /**
   * Returns an input stream for reading file bytes belonging to given range of passed in path.
   */
  InputStream readSegment(Path path, Range range)
      throws IOException;

  /**
   * Invoked at very end of transmission to perform some cleanup if needed.
   */
  void close(boolean successful)
      throws IOException;
}
