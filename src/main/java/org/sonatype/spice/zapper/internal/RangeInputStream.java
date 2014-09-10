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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sonatype.spice.zapper.Range;


/**
 * A filter stream that makes a sub-range of underlying stream appear on input only.
 *
 * @author cstamas
 */
public class RangeInputStream
    extends FilterInputStream
{
  private final Range range;

  private long allowedToRead;

  private boolean allowedToClose;

  public RangeInputStream(final InputStream wrappedStream, final Range range, final boolean doSkip,
                          final boolean allowedToClose)
      throws IOException
  {
    super(wrappedStream);
    if (range == null) {
      throw new NullPointerException("Range is null!");
    }

    this.range = range;
    this.allowedToRead = range.getLength();
    this.allowedToClose = allowedToClose;
    if (doSkip && range.getOffset() > 0) {
      super.skip(range.getOffset());
    }
  }

  public Range getRange() {
    return range;
  }

  @Override
  public int available()
      throws IOException
  {
    return mathMin(super.available(), allowedToRead);
  }

  @Override
  public int read()
      throws IOException
  {
    if (allowedToRead <= 0) {
      return -1;
    }
    final int result = super.read();
    if (result >= 0) {
      --allowedToRead;
    }
    return result;
  }

  @Override
  public int read(byte b[], int off, int len)
      throws IOException
  {
    if (allowedToRead <= 0) {
      return -1;
    }
    int lenToRead = mathMin(len, allowedToRead);
    final int result = super.read(b, off, lenToRead);
    if (result >= 0) {
      allowedToRead -= result;
    }
    return result;
  }

  @Override
  public long skip(final long n)
      throws IOException
  {
    final long result = super.skip(Math.min(n, allowedToRead));
    if (result >= 0) {
      allowedToRead -= result;
    }
    return result;
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

  protected long getAllowedToRead() {
    return allowedToRead;
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
