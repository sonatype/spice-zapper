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

import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.Hashed;

public abstract class AbstractHashedRange
    extends AbstractRange
    implements Hashed
{
  private final Hash hash;

  protected AbstractHashedRange(final long offset, final long length, final Hash hash) {
    super(offset, length);
    this.hash = Check.notNull(hash, Hash.class);
  }

  public Hash getHash() {
    return hash;
  }

  // ==

  @Override
  public String toString() {
    return super.toString() + "(hash=" + getHash() + ")";
  }
}
