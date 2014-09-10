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
import java.util.Iterator;
import java.util.List;

public class PayloadSupplierImpl
    implements PayloadSupplier
{
  private final List<SegmentPayload> payloads;

  private final Iterator<SegmentPayload> payloadIterator;

  public PayloadSupplierImpl(final List<SegmentPayload> payloads) {
    this.payloads = new ArrayList<SegmentPayload>(Check.notNull(payloads, "Payload list is null!").size());
    this.payloads.addAll(payloads);
    this.payloadIterator = payloads.iterator();
  }

  public synchronized SegmentPayload getNextPayload() {
    if (payloadIterator.hasNext()) {
      return payloadIterator.next();
    }
    else {
      return null;
    }
  }
}
