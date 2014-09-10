package org.sonatype.spice.zapper;

import org.sonatype.spice.zapper.codec.NoopCodecSelector;
import org.sonatype.spice.zapper.hash.HashAlgorithm;

public interface Parameters
{
  /**
   * Hash algorithm to use for content verification.
   */
  HashAlgorithm getHashAlgorithm();

  /**
   * Returns the {@link CodecSelector} to be used, never should return {@code null}. See {@link NoopCodecSelector},
   * that is used by default in default implementation (unless overridden).
   */
  CodecSelector getCodecSelector();

  /**
   * How many parallel tracks ("connections") might exist during uploads/downloads. This is a hard maximum, that does
   * not have to be reached, but will never be more than this value. Naturally, having value of 1 would mean
   * "sequential upload" in this sense, and is a valid value.
   */
  int getMaximumTrackCount();

  /**
   * Gets the maximum segment length. Not all protocols uses this, for example "whole-file" does not use this value.
   */
  long getMaximumSegmentLength();
}
