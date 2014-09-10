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
package org.sonatype.spice.zapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Content source when uploading.
 *
 * @author cstamas
 */
public interface IOSource
{
  /**
   * Creates a ZFile belonging to the given path.
   */
  ZFile createZFile(final Path path)
      throws IOException;

  /**
   * Returns an input stream for reading file bytes belonging to given range of passed in path.
   */
  InputStream readSegment(Path path, Range range)
      throws IOException;

  /**
   * Invoked at very end of transmission to perform some cleanup if needed.
   */
  void close(boolean successful)
      throws IOException;
}
