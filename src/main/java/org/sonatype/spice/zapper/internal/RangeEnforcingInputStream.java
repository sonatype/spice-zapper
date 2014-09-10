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
