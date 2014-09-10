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
package org.sonatype.spice.zapper.internal.zapper;

import java.io.IOException;
import java.util.Collections;

import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.HashUtils;
import org.sonatype.spice.zapper.internal.AbstractIdentified;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.MessagePayload;
import org.sonatype.spice.zapper.internal.PayloadCreator;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.ProtocolIdentifier;
import org.sonatype.spice.zapper.internal.SegmentCreator;
import org.sonatype.spice.zapper.internal.Transfer;
import org.sonatype.spice.zapper.internal.transport.AbstractClient;

/**
 * Zapper protocol.
 *
 * @author cstamas
 */
public class ZapperProtocol
    extends AbstractIdentified<ProtocolIdentifier>
    implements Protocol
{
  public static ProtocolIdentifier ID = new ProtocolIdentifier("zapper");

  private final Parameters parameters;

  public ZapperProtocol(final Parameters parameters) {
    super(ID);
    this.parameters = Check.notNull(parameters, Parameters.class);
  }

  public Parameters getParameters() {
    return parameters;
  }

  public SegmentCreator getSegmentCreator() {
    return new ZapperSegmentCreator(getParameters().getMaximumSegmentLength());
  }

  public PayloadCreator getPayloadCreator() {
    return new ZapperPayloadCreator(getParameters());
  }

  public void beforeUpload(final Transfer transfer, final AbstractClient<?> client)
      throws IOException
  {
    final byte[] payload = new byte[0];
    final MessagePayload message =
        new MessagePayload(transfer.getIdentifier(), new Path("beforeUpload"), payload, HashUtils.getDigest(
            parameters.getHashAlgorithm(), payload), Collections.<Codec>emptyList());
    client.upload(message);
  }

  public void afterUpload(final Transfer transfer, final AbstractClient<?> client)
      throws IOException
  {
    final byte[] payload = new byte[0];
    final MessagePayload message =
        new MessagePayload(transfer.getIdentifier(), new Path("afterUpload"), payload, HashUtils.getDigest(
            parameters.getHashAlgorithm(), payload), Collections.<Codec>emptyList());
    client.upload(message);
  }
}
