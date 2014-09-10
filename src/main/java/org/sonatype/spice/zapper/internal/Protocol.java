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
