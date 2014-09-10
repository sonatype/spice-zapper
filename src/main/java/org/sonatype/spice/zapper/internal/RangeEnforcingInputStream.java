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

import java.io.IOException;
import java.io.InputStream;

import org.sonatype.spice.zapper.Range;


/**
 * A filter stream that enforces that {@link RangeInputStream} is fully consumed.
 *
 * @author cstamas
 */
public class RangeEnforcingInputStream
    extends RangeInputStream
{
  public RangeEnforcingInputStream(final InputStream wrappedStream, final Range range)
      throws IOException
  {
    this(wrappedStream, range, false, false);
  }

  public RangeEnforcingInputStream(InputStream wrappedStream, Range range, boolean doSkip, boolean allowedToClose)
      throws IOException
  {
    super(wrappedStream, range, doSkip, allowedToClose);
  }

  @Override
  public int read()
      throws IOException
  {
    final int result = super.read();
    if (result == -1) {
      checkIsEndExpected();
    }
    return result;
  }

  @Override
  public int read(byte b[], int off, int len)
      throws IOException
  {
    final int result = super.read(b, off, len);
    if (result == -1) {
      checkIsEndExpected();
    }
    return result;
  }

  // ==

  protected void checkIsEndExpected()
      throws IOException
  {
    final long allowedToRead = getAllowedToRead();
    if (allowedToRead > 0) {
      throw new IOException(String.format("Unexpected end of stream, %s more bytes expected!", allowedToRead));
    }
  }
}
