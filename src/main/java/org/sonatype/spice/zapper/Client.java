package org.sonatype.spice.zapper;

import java.io.IOException;

public interface Client
{
  /**
   * Returns the base URL of this handle.
   */
  String getRemoteUrl();

  /**
   * Uploads all that listable source lists.
   */
  void upload(IOSourceListable listableSource)
      throws IOException;

  /**
   * Uploads given paths from the source.
   */
  void upload(IOSource source, Path... paths)
      throws IOException;

  /**
   * Downloads given paths into the target.
   */
  void download(IOTarget target, Path... paths)
      throws IOException;

  /**
   * Should be called as last step, to make client able to cleanup resources if needed.
   */
  void close();
}
