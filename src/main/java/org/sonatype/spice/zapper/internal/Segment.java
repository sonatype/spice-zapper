package org.sonatype.spice.zapper.internal;

import org.sonatype.spice.zapper.Identified;
import org.sonatype.spice.zapper.ZFile;

/**
 * Segment is ZFile part (or whole, depending on range it holds).
 *
 * @author cstamas
 */
public class Segment
    extends AbstractRange
    implements Identified<SegmentIdentifier>
{
  private final SegmentIdentifier segmentIdentifier;

  private final ZFile zfile;

  public Segment(final long offset, final long length, final ZFile zfile, final SegmentIdentifier segmentIdentifier) {
    super(offset, length);
    this.zfile = Check.notNull(zfile, ZFile.class);
    this.segmentIdentifier = Check.notNull(segmentIdentifier, SegmentIdentifier.class);
  }

  public SegmentIdentifier getIdentifier() {
    return segmentIdentifier;
  }

  public ZFile getZFile() {
    return zfile;
  }
}
