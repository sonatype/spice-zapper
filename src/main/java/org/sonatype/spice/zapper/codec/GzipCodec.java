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
package org.sonatype.spice.zapper.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.sonatype.spice.zapper.internal.AbstractIdentified;

public class GzipCodec
    extends AbstractIdentified<CodecIdentifier>
    implements Codec
{
  public static final CodecIdentifier ID = new CodecIdentifier("gzip");

  public GzipCodec() {
    super(ID);
  }

  public OutputStream encode(final OutputStream outputStream)
      throws IOException
  {
    return new GZIPOutputStream(outputStream);
  }

  public InputStream decode(InputStream inputStream)
      throws IOException
  {
    return new GZIPInputStream(inputStream);
  }
}
