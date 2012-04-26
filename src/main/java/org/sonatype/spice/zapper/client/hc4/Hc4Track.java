package org.sonatype.spice.zapper.client.hc4;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.PayloadSupplier;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.transport.AbstractChargerTrack;
import org.sonatype.spice.zapper.internal.transport.State;


public class Hc4Track
    extends AbstractChargerTrack
{
    private final Hc4Client hc4Client;

    public Hc4Track( final Protocol protocol, final int trackNo, final PayloadSupplier payloadSupplier,
                     final Hc4Client hc4Client )
    {
        super( protocol, trackNo, payloadSupplier );
        this.hc4Client = hc4Client;
    }

    @Override
    public State call()
        throws Exception
    {
        Payload payload = getPayloadSupplier().getNextPayload();
        while ( payload != null )
        {
            final HttpResponse response = hc4Client.upload( payload, this );
            final StatusLine statusLine = response.getStatusLine();
            EntityUtils.consume( response.getEntity() );
            if ( !( statusLine.getStatusCode() > 199 && statusLine.getStatusCode() < 299 ) )
            {
                throw new IOException( String.format( "Unexpected server response: %s %s", statusLine.getStatusCode(),
                    statusLine.getReasonPhrase() ) );
            }
            payload = getPayloadSupplier().getNextPayload();
        }
        return State.SUCCESS;
    }
}
