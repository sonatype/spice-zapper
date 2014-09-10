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

public interface Client
{
  /**
   * Returns the base URL of this handle.
   */
  String getRemoteUrl();

  /**
   * Uploads all that listable source lists.
   */
  void upload(IOSourceListable listableSource)
      throws IOException;

  /**
   * Uploads given paths from the source.
   */
  void upload(IOSource source, Path... paths)
      throws IOException;

  /**
   * Downloads given paths into the target.
   */
  void download(IOTarget target, Path... paths)
      throws IOException;

  /**
   * Should be called as last step, to make client able to cleanup resources if needed.
   */
  void close();
}
