package org.sonatype.spice.zapper.client.hc4;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.PayloadSupplier;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.SegmentPayload;
import org.sonatype.spice.zapper.internal.Transfer;
import org.sonatype.spice.zapper.internal.transport.AbstractChargerClient;
import org.sonatype.spice.zapper.internal.transport.State;
import org.sonatype.spice.zapper.internal.transport.TrackIdentifier;

public class Hc4Client
    extends AbstractChargerClient<Hc4Track>
{
    private final HttpClient httpClient;

    public Hc4Client( final Parameters parameters, final String remoteUrl, final HttpClient httpClient )
    {
        super( parameters, remoteUrl );
        this.httpClient = Check.notNull( httpClient, HttpClient.class );
    }

    @Override
    public void close()
    {
        httpClient.getConnectionManager().shutdown();
        super.close();
    }

    // ==

    @Override
    protected Callable<State> createCallable( final TrackIdentifier trackIdentifier, final Transfer transfer,
                                              final Protocol protocol, final PayloadSupplier payloadSupplier )
    {
        return new Hc4Track( trackIdentifier, payloadSupplier, this );
    }

    // ==

    @Override
    public State upload( final Payload payload, final Hc4Track track )
        throws IOException
    {
        final String url = getRemoteUrl() + payload.getPath().stringValue();
        final HttpPut put = new HttpPut( url );
        if ( payload instanceof SegmentPayload )
        {
            put.setEntity( new ZapperEntity( payload, getParameters().getCodecSelector().selectCodecs(
                SegmentPayload.class.cast( payload ).getSegment().getZFile() ) ) );
        }
        else
        {
            put.setEntity( new ZapperEntity( payload ) );
        }
        put.addHeader( "X-Zapper-Transfer-ID", payload.getTransferIdentifier().stringValue() );
        if ( track != null )
        {
            put.addHeader( "X-Zapper-Track-ID", track.getIdentifier().stringValue() );
        }
        final HttpResponse response = httpClient.execute( put );
        final StatusLine statusLine = response.getStatusLine();
        EntityUtils.consume( response.getEntity() );
        if ( !( statusLine.getStatusCode() > 199 && statusLine.getStatusCode() < 299 ) )
        {
            throw new IOException( String.format( "Unexpected server response: %s %s", statusLine.getStatusCode(),
                statusLine.getReasonPhrase() ) );
        }
        return State.SUCCESS;
    }

    @Override
    public State upload( final Payload payload )
        throws IOException
    {
        return upload( payload, null );
    }
}
