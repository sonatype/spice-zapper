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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.Hash;

/**
 * Message payload holds small array of bytes, that will be sent over the wire. It is unaware of the content.
 *
 * @author cstamas
 */
public class MessagePayload
    extends AbstractPayload
    implements Payload
{
  private final byte[] message;

  public MessagePayload(final TransferIdentifier transferIdentifier, final Path path, final byte[] message,
                        final Hash hash, final List<Codec> codecs)
  {
    super(transferIdentifier, path, hash, codecs);
    this.message = Arrays.copyOf(message, message.length);
  }

  public long getLength() {
    return message.length;
  }

  public InputStream getContent()
      throws IOException
  {
    return new ByteArrayInputStream(message);
  }

  // ==

  public byte[] getMessage() {
    return message;
  }

}
