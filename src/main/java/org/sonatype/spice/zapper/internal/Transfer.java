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
import java.util.concurrent.atomic.AtomicInteger;

import org.sonatype.spice.zapper.ZFile;
import org.sonatype.spice.zapper.internal.transport.TrackIdentifier;

public class Transfer
    extends AbstractIdentified<TransferIdentifier>
{
  private final List<ZFile> zfiles;

  private final List<Segment> segments;

  private final List<SegmentPayload> payloads;

  private final long totalSize;

  private final AtomicInteger segmentCounter = new AtomicInteger(1);

  private final AtomicInteger trackCounter = new AtomicInteger(1);

  public Transfer(final String transferIdentifier, final List<ZFile> zfiles) {
    super(new TransferIdentifier(transferIdentifier));
    this.zfiles = Collections.unmodifiableList(Check.notNull(zfiles, List.class));
    this.segments = new ArrayList<Segment>();
    this.payloads = new ArrayList<SegmentPayload>();
    long ts = 0;
    for (ZFile zfile : zfiles) {
      ts += zfile.getLength();
    }
    this.totalSize = ts;
  }

  public SegmentIdentifier getNextSegmentIdentifier() {
    return new SegmentIdentifier("S" + String.valueOf(segmentCounter.getAndIncrement()));
  }

  public TrackIdentifier getNextTrackIdentifier() {
    return new TrackIdentifier("T" + String.valueOf(trackCounter.getAndIncrement()));
  }

  public List<ZFile> getZfiles() {
    return zfiles;
  }

  public long getTotalSize() {
    return totalSize;
  }

  public List<Segment> getSegments() {
    return Collections.unmodifiableList(segments);
  }

  public void setSegments(final List<Segment> segments) {
    this.segments.clear();
    this.segments.addAll(segments);
  }

  public List<SegmentPayload> getPayloads() {
    return Collections.unmodifiableList(payloads);
  }

  public void setPayloads(List<SegmentPayload> payloads) {
    this.payloads.clear();
    this.payloads.addAll(payloads);
  }

  public PayloadSupplier getPayloadSupplier() {
    return new PayloadSupplierImpl(getPayloads());
  }
}
