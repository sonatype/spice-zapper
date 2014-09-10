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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.Hash;

public abstract class AbstractPayload
    implements Payload
{
  private final TransferIdentifier transferIdentifier;

  private final Path path;

  private final Hash hash;

  private final List<Codec> codecs;

  protected AbstractPayload(final TransferIdentifier transferIdentifier, final Path path, final Hash hash,
                            final List<Codec> codecs)
  {
    this.transferIdentifier = Check.notNull(transferIdentifier, TransferIdentifier.class);
    this.path = Check.notNull(path, Path.class);
    this.hash = Check.notNull(hash, Hash.class);
    final ArrayList<Codec> cds = new ArrayList<Codec>();
    if (codecs != null) {
      cds.addAll(codecs);
    }
    this.codecs = Collections.unmodifiableList(cds);
  }

  public TransferIdentifier getTransferIdentifier() {
    return transferIdentifier;
  }

  public Path getPath() {
    return path;
  }

  public Hash getHash() {
    return hash;
  }

  public List<Codec> getCodecs() {
    return codecs;
  }
}
