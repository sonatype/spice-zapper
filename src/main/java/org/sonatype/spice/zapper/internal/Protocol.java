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

import java.io.IOException;

import org.sonatype.spice.zapper.Identified;
import org.sonatype.spice.zapper.internal.transport.AbstractClient;

/**
 * Whole file -- most compatible Ranges -- segmented uploads by ranges Zapper -- segmented uploads
 *
 * @author cstamas
 */
public interface Protocol
    extends Identified<ProtocolIdentifier>
{
  SegmentCreator getSegmentCreator();

  PayloadCreator getPayloadCreator();

  void beforeUpload(Transfer transfer, AbstractClient<?> client)
      throws IOException;

  void afterUpload(Transfer transfer, AbstractClient<?> client)
      throws IOException;
}
