package org.sonatype.spice.zapper.internal;

import java.util.ArrayList;
import java.util.List;

import org.sonatype.spice.zapper.ZFile;

/**
 * Segment creator that chops files into given size, or creates a segment carrying whole file if {@code maxSegmentSize}
 * is bigger than the file.
 *
 * @author cstamas
 */
public abstract class ZFileSegmentCreator
    implements SegmentCreator
{
  private final long maxSegmentSize;

  public ZFileSegmentCreator(final long maxSegmentSize) {
    this.maxSegmentSize = maxSegmentSize;
  }

  public int createSegments(final Transfer transfer) {
    final List<ZFile> zfiles = transfer.getZfiles();
    final ArrayList<Segment> segments = new ArrayList<Segment>(zfiles.size());
    for (ZFile zfile : zfiles) {
      if (zfile.getLength() < maxSegmentSize) {
        segments.add(createSegment(transfer, 0, zfile.getLength(), zfile));
      }
      else {
        long offset = 0;
        long length = maxSegmentSize;
        while (true) {
          length = Math.min(length, zfile.getLength() - offset);
          if (length == 0) {
            break;
          }
          segments.add(createSegment(transfer, offset, length, zfile));
          offset += length;
        }
      }
    }
    transfer.setSegments(segments);
    return segments.size();
  }

  protected Segment createSegment(final Transfer transfer, final long offset, final long length, final ZFile zfile) {
    return new Segment(offset, length, zfile, transfer.getNextSegmentIdentifier());
  }
}
