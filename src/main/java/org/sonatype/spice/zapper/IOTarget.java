package org.sonatype.spice.zapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Used when downloading, as "target" for arrived content.
 *
 * @author cstamas
 */
public interface IOTarget
{
  /**
   * Invoked before any segment is received, as very first step to prepare for receive. Depending on implementation,
   * this method might do nothing, might just check for free space or even might allocate the needed space and do
   * other book keeping things.
   *
   * @param zfile the file to be received.
   */
  void initializeZFile(ZFile zfile)
      throws IOException;

  /**
   * Returns an output stream for writing file bytes belonging to given range of passed in ZFile. Depending on the
   * underlying implementation, this might be a stream writing to segment's final place, or to some temporary place if
   * this implementation handles the "assembling" of segments into file after all of them are received.
   */
  long writeSegment(ZFile zfile, Range range, InputStream in)
      throws IOException;

  /**
   * Invoked after all segments are successfully written to target.
   *
   * @param zfile the file that is received.
   */
  void finalizeZFile(ZFile zfile)
      throws IOException;

  /**
   * Invoked at very end of transmission to perform some cleanup if needed.
   */
  void close(boolean successful)
      throws IOException;
}
