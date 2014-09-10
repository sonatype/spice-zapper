/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
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
