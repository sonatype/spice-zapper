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

import org.sonatype.spice.zapper.Path;


public class AbstractDirectory
{
  private final File root;

  public AbstractDirectory(final File root)
      throws IOException
  {
    if (root.isDirectory()) {
      this.root = root.getAbsoluteFile();
    }
    else {
      throw new IOException(String.format("Supplied file %s is a not an existing directory!",
          root.getAbsolutePath()));
    }
  }

  public File getRoot() {
    return root;
  }

  // ==

  protected File getFile(final Path path)
      throws IOException
  {
    final File result = new File(getRoot(), path.stringValue()).getAbsoluteFile();

    if (!result.getAbsolutePath().startsWith(root.getAbsolutePath())) {
      throw new IOException(String.format("Path %s was about to escape sandbox %s!", path,
          root.getAbsolutePath()));
    }

    return result;
  }
}
