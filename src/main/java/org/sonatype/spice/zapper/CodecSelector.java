package org.sonatype.spice.zapper;

import java.util.List;

import org.sonatype.spice.zapper.codec.Codec;

/**
 * Selects codec(s) to have applied to transported files.
 *
 * @author cstamas
 */
public interface CodecSelector
{
  /**
   * Returns the list of {@link Codec}s to apply to payload. Never returns {@code null}.
   */
  List<Codec> selectCodecs(ZFile zfile);
}
