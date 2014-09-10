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

import org.sonatype.spice.zapper.CodecSelector;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.hash.HashAlgorithm;

public class ParametersImpl
    implements Parameters
{
  private final HashAlgorithm hashAlgorithm;

  private final CodecSelector codecSelector;

  private final int maximumTrackCount;

  private final long maximumSegmentLength;

  public ParametersImpl(final HashAlgorithm hashAlgorithm, final CodecSelector codecSelector,
                        final int maximumTrackCount, final long maximumSegmentLength)
  {
    this.hashAlgorithm = Check.notNull(hashAlgorithm, HashAlgorithm.class);
    this.codecSelector = Check.notNull(codecSelector, CodecSelector.class);
    this.maximumTrackCount =
        Check.argument(maximumTrackCount > 0, maximumTrackCount, "maximumTrackCount must be positive!");
    this.maximumSegmentLength =
        Check.argument(maximumSegmentLength > 0, maximumSegmentLength, "maximumSegmentLength must be positive!");
  }

  public HashAlgorithm getHashAlgorithm() {
    return hashAlgorithm;
  }

  public CodecSelector getCodecSelector() {
    return codecSelector;
  }

  public int getMaximumTrackCount() {
    return maximumTrackCount;
  }

  public long getMaximumSegmentLength() {
    return maximumSegmentLength;
  }
}
