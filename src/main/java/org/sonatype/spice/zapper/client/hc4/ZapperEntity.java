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
package org.sonatype.spice.zapper.client.hc4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.Payload;

import org.apache.http.entity.AbstractHttpEntity;

public class ZapperEntity
    extends AbstractHttpEntity
{
  private final Payload payload;

  private final List<Codec> codecs;

  public ZapperEntity(final Payload payload) {
    this(payload, Collections.<Codec>emptyList());
  }

  public ZapperEntity(final Payload payload, final List<Codec> codecs) {
    this.payload = Check.notNull(payload, Payload.class);
    this.codecs = Check.notNull(codecs, Codec.class);
  }

  public boolean isRepeatable() {
    return true;
  }

  public long getContentLength() {
    if (codecs.isEmpty()) {
      return payload.getLength();
    }
    else {
      return -1;
    }
  }

  public InputStream getContent()
      throws IOException, IllegalStateException
  {
    return payload.getContent();
  }

  private final int BUFFER_SIZE = 2048;

  public void writeTo(final OutputStream _outstream)
      throws IOException
  {
    final InputStream instream = getContent();
    OutputStream outstream = _outstream;
    for (Codec codec : codecs) {
      outstream = codec.encode(outstream);
    }

    try {
      byte[] buffer = new byte[BUFFER_SIZE];
      int l;
      if (getContentLength() < 0) {
        // consume until EOF
        while ((l = instream.read(buffer)) != -1) {
          outstream.write(buffer, 0, l);
        }
      }
      else {
        // consume no more than length
        long remaining = getContentLength();
        while (remaining > 0) {
          l = instream.read(buffer, 0, (int) Math.min(BUFFER_SIZE, remaining));
          if (l == -1) {
            break;
          }
          outstream.write(buffer, 0, l);
          remaining -= l;
        }
      }
    }
    finally {
      outstream.close();
      instream.close();
    }
  }

  public boolean isStreaming() {
    return false;
  }
}
