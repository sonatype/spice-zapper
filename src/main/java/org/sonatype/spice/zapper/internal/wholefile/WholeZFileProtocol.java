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

import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.AbstractIdentified;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.PayloadCreator;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.ProtocolIdentifier;
import org.sonatype.spice.zapper.internal.SegmentCreator;
import org.sonatype.spice.zapper.internal.Transfer;
import org.sonatype.spice.zapper.internal.transport.AbstractClient;

/**
 * Whole file protocol does not "cut" (segment) uploaded files, but instead sends them as whole. Usable when there is
 * no
 * Zapper-aware client side, as this works with all protocols out of the box (ie. this is actually PUTs in HTTP or
 * separate file uploads in FTP world without any action needed on receiver end, with exception of the existence of
 * "usual" server, HTTP or FTP).
 *
 * @author cstamas
 */
public class WholeZFileProtocol
    extends AbstractIdentified<ProtocolIdentifier>
    implements Protocol
{
  public static ProtocolIdentifier ID = new ProtocolIdentifier("whole-zfile");

  private final Parameters parameters;

  public WholeZFileProtocol(final Parameters parameters) {
    super(ID);
    this.parameters = Check.notNull(parameters, Parameters.class);
  }

  public Parameters getParameters() {
    return parameters;
  }

  public SegmentCreator getSegmentCreator() {
    return new WholeZFileSegmentCreator();
  }

  public PayloadCreator getPayloadCreator() {
    return new WholeZFilePayloadCreator(getParameters());
  }

  public void beforeUpload(final Transfer transfer, final AbstractClient<?> client) {
    // nop
  }

  public void afterUpload(final Transfer transfer, final AbstractClient<?> client) {
    // nop
  }
}
