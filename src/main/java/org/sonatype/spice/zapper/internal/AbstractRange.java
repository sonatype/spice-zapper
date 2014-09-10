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

import org.sonatype.spice.zapper.Range;

public abstract class AbstractRange
    implements Range
{
  private final long offset;

  private final long length;

  protected AbstractRange(final long offset, final long length) {
    this.offset = Check.argument(offset >= 0, offset, "Offset is less than 0!");
    this.length = Check.argument(length > 0, length, "Length is less than 1!");
  }

  public long getOffset() {
    return offset;
  }

  public long getLength() {
    return length;
  }

  public boolean matches(final Range range) {
    return (getOffset() == range.getOffset() && getLength() == range.getLength());
  }

  public boolean contains(final Range range) {
    return (getOffset() <= range.getOffset() && getLength() >= range.getLength());
  }

  public boolean overlaps(final Range range) {
    final long myStart = getOffset();
    final long myEnd = myStart + getLength(); // we know myStart < myEnd
    final long hisStart = range.getOffset();
    final long hisEnd = hisStart + range.getLength(); // we know hisStart < hisEnd

    // we check for "is before" or "is after" and negate the result
    return !(myEnd < hisStart || myStart > hisEnd);
  }

  // ==

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(offset=" + getOffset() + ", length=" + getLength() + ")";
  }
}
