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
package org.sonatype.spice.zapper.hash;

import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.StringIdentifier;

public class HashAlgorithmIdentifier
    extends StringIdentifier
{
  private final int hashSizeInBytes;

  public HashAlgorithmIdentifier(final String stringValue, final int hashSizeInBytes) {
    super(stringValue);
    this.hashSizeInBytes = Check.argument(hashSizeInBytes > 0, hashSizeInBytes, "Hash size not positive!");
  }

  public int getHashSize() {
    return hashSizeInBytes;
  }
}
