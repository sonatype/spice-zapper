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

import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.sonatype.spice.zapper.internal.AbstractIdentified;


public abstract class AbstractMessageDigestHashAlgorithm
    extends AbstractIdentified<HashAlgorithmIdentifier>
    implements HashAlgorithm
{
  public AbstractMessageDigestHashAlgorithm(final HashAlgorithmIdentifier identifier)
      throws NoSuchAlgorithmException
  {
    super(identifier);
    MessageDigest.getInstance(getIdentifier().stringValue());
  }

  public Hash hash(final byte[] buffer) {
    return new Hash(getIdentifier(), getMessageDigest().digest(buffer));
  }

  public HashingInputStream hashInput(final InputStream input) {
    return new HashingInputStream(new DigestInputStream(input, getMessageDigest()))
    {
      @Override
      public Hash getHash() {
        return new Hash(AbstractMessageDigestHashAlgorithm.this.getIdentifier(),
            ((DigestInputStream) in).getMessageDigest().digest());
      }
    };
  }

  public HashingOutputStream hashOutput(final OutputStream output) {
    return new HashingOutputStream(new DigestOutputStream(output, getMessageDigest()))
    {
      @Override
      public Hash getHash() {
        return new Hash(AbstractMessageDigestHashAlgorithm.this.getIdentifier(),
            ((DigestOutputStream) out).getMessageDigest().digest());
      }
    };
  }

  // ==

  protected MessageDigest getMessageDigest() {
    try {
      return MessageDigest.getInstance(getIdentifier().stringValue());
    }
    catch (NoSuchAlgorithmException e) {
      // we know this will not happen, see constructor, we tried this already once
      throw new IllegalStateException("No suitable MessageDigest available!", e);
    }
  }
}
