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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.sonatype.spice.zapper.Range;


/**
 * A filter stream that makes a sub-range of underlying stream appear on input only.
 *
 * @author cstamas
 */
public class RangeOutputStream
    extends FilterOutputStream
{
  private final Range range;

  private long allowedToWrite;

  private boolean allowedToClose;

  public RangeOutputStream(final OutputStream wrappedStream, final Range range, final boolean allowedToClose)
      throws IOException
  {
    super(wrappedStream);
    if (range == null) {
      throw new NullPointerException("Range is null!");
    }

    this.range = range;
    this.allowedToWrite = range.getLength();
    this.allowedToClose = allowedToClose;
  }

  public Range getRange() {
    return range;
  }

  public void write(int b)
      throws IOException
  {
    if (allowedToWrite <= 0) {
      return;
    }
    super.write(b);
    --allowedToWrite;
  }

  public void write(byte b[], int off, int len)
      throws IOException
  {
    if (allowedToWrite <= 0) {
      return;
    }
    int lenToWrite = mathMin(len, allowedToWrite);
    super.write(b, off, lenToWrite);
    allowedToWrite -= lenToWrite;
  }

  @Override
  public void close()
      throws IOException
  {
    if (allowedToClose) {
      super.close();
    }
  }

  // ==

  protected long getAllowedToWrite() {
    return allowedToWrite;
  }

  protected int mathMin(int a, long b) {
    if (b < Integer.MAX_VALUE) {
      return Math.min(a, (int) b);
    }
    else {
      return a;
    }
  }
}
