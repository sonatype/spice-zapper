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
package org.sonatype.spice.zapper.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import org.sonatype.spice.zapper.IOTarget;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.Range;
import org.sonatype.spice.zapper.ZFile;
import org.sonatype.spice.zapper.internal.Check;


public class DirectoryIOTarget
    extends AbstractDirectory
    implements IOTarget
{
  private final Map<Path, File> files;

  public DirectoryIOTarget(final File root)
      throws IOException
  {
    super(root);
    files = new HashMap<Path, File>();
  }

  public void close(final boolean successful)
      throws IOException
  {
    if (!successful) {
      for (File file : files.values()) {
        file.delete();
      }
    }
  }

  public void initializeZFile(final ZFile zfile)
      throws IOException
  {
    final File file = getFile(zfile.getIdentifier());
    final RandomAccessFile raf = new RandomAccessFile(file, "rw");
    raf.setLength(zfile.getLength());
    raf.close();
    files.put(zfile.getIdentifier(), file);
  }

  public long writeSegment(final ZFile zfile, final Range range, final InputStream in)
      throws IOException
  {
    final File file = files.get(zfile.getIdentifier());
    final FileRange fileRange = new FileRange(file, range);
    final long result =
        fileRange.getFileChannel().transferFrom(Channels.newChannel(in), range.getOffset(), range.getLength());
    fileRange.close();
    return result;
  }

  public void finalizeZFile(ZFile zfile)
      throws IOException
  {
    final File file = files.get(zfile.getIdentifier());
    file.setLastModified(zfile.getLastModifiedTimestamp());
  }

  public static class FileRange
  {
    private final RandomAccessFile randomAccessFile;

    private final Range range;

    public FileRange(final File file, final Range range)
        throws IOException
    {
      this.randomAccessFile = new RandomAccessFile(file, "rw");
      this.range = Check.notNull(range, "Range is null");
      this.randomAccessFile.seek(range.getOffset());
    }

    public FileChannel getFileChannel() {
      return randomAccessFile.getChannel();
    }

    public Range getRange() {
      return range;
    }

    public void close()
        throws IOException
    {
      randomAccessFile.close();
    }
  }
}