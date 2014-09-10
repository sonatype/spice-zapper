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

import org.sonatype.spice.zapper.codec.NoopCodecSelector;
import org.sonatype.spice.zapper.hash.HashAlgorithm;

public interface Parameters
{
  /**
   * Hash algorithm to use for content verification.
   */
  HashAlgorithm getHashAlgorithm();

  /**
   * Returns the {@link CodecSelector} to be used, never should return {@code null}. See {@link NoopCodecSelector},
   * that is used by default in default implementation (unless overridden).
   */
  CodecSelector getCodecSelector();

  /**
   * How many parallel tracks ("connections") might exist during uploads/downloads. This is a hard maximum, that does
   * not have to be reached, but will never be more than this value. Naturally, having value of 1 would mean
   * "sequential upload" in this sense, and is a valid value.
   */
  int getMaximumTrackCount();

  /**
   * Gets the maximum segment length. Not all protocols uses this, for example "whole-file" does not use this value.
   */
  long getMaximumSegmentLength();
}
