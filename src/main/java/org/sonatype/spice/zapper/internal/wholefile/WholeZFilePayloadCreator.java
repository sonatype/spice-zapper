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
package org.sonatype.spice.zapper.internal.wholefile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.PayloadCreator;
import org.sonatype.spice.zapper.internal.Segment;
import org.sonatype.spice.zapper.internal.SegmentPayload;
import org.sonatype.spice.zapper.internal.Transfer;

/**
 * Creates Payloads that are actually whole ZFiles, without any extra fluff.
 *
 * @author cstamas
 */
public class WholeZFilePayloadCreator
    implements PayloadCreator
{
  private final Parameters parameters;

  public WholeZFilePayloadCreator(final Parameters parameters) {
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

  protected SegmentPayload createPayload(final Transfer transfer, final Segment segment, final IOSource source,
                                         final String remoteUrl)
      throws IOException
  {
    return new SegmentPayload(transfer.getIdentifier(), segment.getZFile().getIdentifier(), segment, source,
        segment.getZFile().getHash(), parameters.getCodecSelector().selectCodecs(segment.getZFile()));
  }
}
