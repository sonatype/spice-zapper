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

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.ZFile;
import org.sonatype.spice.zapper.hash.Hash;

public class ZFileImpl
    extends AbstractHashedRange
    implements ZFile
{
  private final Path path;

  private final long lastModified;

  public ZFileImpl(final Path path, final long length, final long lastModified, final Hash hash) {
    super(0, length, hash);
    this.path = Check.notNull(path, "Path is null!");
    this.lastModified = lastModified;
  }

  public Path getIdentifier() {
    return path;
  }

  public long getLastModifiedTimestamp() {
    return lastModified;
  }

  // ==

  @Override
  public String toString() {
    return super.toString() + "(path=" + getIdentifier() + ")";
  }
}
