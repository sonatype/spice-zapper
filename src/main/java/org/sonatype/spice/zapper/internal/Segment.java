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
