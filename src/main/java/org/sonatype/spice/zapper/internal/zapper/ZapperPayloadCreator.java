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
import java.util.ArrayList;
import java.util.List;

import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.HashUtils;
import org.sonatype.spice.zapper.internal.PayloadCreator;
import org.sonatype.spice.zapper.internal.Segment;
import org.sonatype.spice.zapper.internal.SegmentPayload;
import org.sonatype.spice.zapper.internal.Transfer;

/**
 * Zapper
 *
 * @author cstamas
 */
public class ZapperPayloadCreator
    implements PayloadCreator
{
  private final Parameters parameters;

  public ZapperPayloadCreator(final Parameters parameters) {
    this.parameters = parameters;
  }

  public int createPayloads(final Transfer transfer, final IOSource source, final String remoteUrl)
      throws IOException
  {
    final List<Segment> segments = transfer.getSegments();
    final ArrayList<SegmentPayload> payloads = new ArrayList<SegmentPayload>(segments.size());
    for (Segment segment : segments) {
      payloads.add(createPayload(transfer, segment, source, remoteUrl));
    }
    transfer.setPayloads(payloads);
    return payloads.size();
  }

  protected ZapperPayload createPayload(final Transfer transfer, final Segment segment, final IOSource source,
                                        final String remoteUrl)
      throws IOException
  {
    // calculate segment's hash, we have to do this always, as we know the hash of whole file only
    final Hash segmentHash =
        HashUtils.getDigest(parameters.getHashAlgorithm(),
            source.readSegment(segment.getZFile().getIdentifier(), segment));

    return new ZapperPayload(transfer.getIdentifier(), segment.getZFile().getIdentifier(), segment, source,
        parameters.getHashAlgorithm(), segmentHash, parameters.getCodecSelector().selectCodecs(segment.getZFile()));
  }
}
