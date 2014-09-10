package org.sonatype.spice.zapper.client.hc4;

import java.io.IOException;

import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.PayloadSupplier;
import org.sonatype.spice.zapper.internal.transport.AbstractChargerTrack;
import org.sonatype.spice.zapper.internal.transport.State;
import org.sonatype.spice.zapper.internal.transport.TrackIdentifier;

public class Hc4Track
    extends AbstractChargerTrack
{
  private final Hc4Client hc4Client;

  public Hc4Track(final TrackIdentifier identifier, final PayloadSupplier payloadSupplier, final Hc4Client hc4Client) {
    super(identifier, payloadSupplier);
    this.hc4Client = hc4Client;
  }

  public State call()
      throws IOException
  {
    Payload payload = getPayloadSupplier().getNextPayload();
    while (payload != null) {
      hc4Client.upload(payload, this);
      payload = getPayloadSupplier().getNextPayload();
    }
    return State.SUCCESS;
  }
}
