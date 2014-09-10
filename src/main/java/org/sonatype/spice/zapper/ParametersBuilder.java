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

import java.security.NoSuchAlgorithmException;

import org.sonatype.spice.zapper.codec.NoopCodecSelector;
import org.sonatype.spice.zapper.hash.HashAlgorithm;
import org.sonatype.spice.zapper.hash.Sha1HashAlgorithm;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.ParametersImpl;

public class ParametersBuilder
{
  private HashAlgorithm hashAlgorithm;

  private CodecSelector codecSelector;

  private int maximumTrackCount;

  private long maximumSegmentLength;

  private ParametersBuilder(final HashAlgorithm hashAlgorithm) {
    this.hashAlgorithm = Check.notNull(hashAlgorithm, HashAlgorithm.class);
    this.codecSelector = new NoopCodecSelector();
    this.maximumTrackCount = 6;
    this.maximumSegmentLength = 1073741824L; // 1MB
  }

  public ParametersBuilder setHashAlgorithm(final HashAlgorithm hashAlgorithm) {
    this.hashAlgorithm = Check.notNull(hashAlgorithm, HashAlgorithm.class);
    return this;
  }

  public ParametersBuilder setCodecSelector(final CodecSelector codecSelector) {
    this.codecSelector = Check.notNull(codecSelector, CodecSelector.class);
    return this;
  }

  public ParametersBuilder setMaximumTrackCount(final int maximumTrackCount) {
    this.maximumTrackCount =
        Check.argument(maximumTrackCount > 0, maximumTrackCount, "maximumTrackCount not positive!");
    return this;
  }

  public ParametersBuilder setMaximumSegmentLength(final long maximumSegmentLength) {
    // this could be stricter, like "is there any sense to have 1 byte large segments?"
    // maybe some sensible minimum like 1MB?
    this.maximumSegmentLength =
        Check.argument(maximumSegmentLength > 0, maximumSegmentLength, "maximumSegmentLength not positive!");
    return this;
  }

  public Parameters build() {
    return new ParametersImpl(hashAlgorithm, codecSelector, maximumTrackCount, maximumSegmentLength);
  }

  public static ParametersBuilder defaults()
      throws NoSuchAlgorithmException
  {
    // sha1 hash algorithm + 6 tracks + 1MB segments
    return new ParametersBuilder(new Sha1HashAlgorithm()).setMaximumTrackCount(6).setMaximumSegmentLength(
        1073741824L);
  }
}
