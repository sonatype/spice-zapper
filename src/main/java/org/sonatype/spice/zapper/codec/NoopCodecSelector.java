package org.sonatype.spice.zapper.codec;

import java.util.Collections;
import java.util.List;

import org.sonatype.spice.zapper.CodecSelector;
import org.sonatype.spice.zapper.ZFile;

/**
 * {@link CodecSelector} that never selects any {@link Codec}.
 *
 * @author cstamas
 */
public class NoopCodecSelector
    implements CodecSelector
{
  public List<Codec> selectCodecs(final ZFile zfile) {
    return Collections.emptyList();
  }
}
