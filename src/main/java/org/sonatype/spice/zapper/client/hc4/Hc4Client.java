package org.sonatype.spice.zapper.client.hc4;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.PayloadSupplier;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.transport.AbstractChargerClient;
import org.sonatype.spice.zapper.internal.transport.State;


public class Hc4Client
    extends AbstractChargerClient
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
    protected Callable<State> createCallable( final Protocol protocol, final int trackNo,
                                              final PayloadSupplier payloadSupplier )
    {
        return new Hc4Track( protocol, trackNo, payloadSupplier, this );
    }

    // ==

    protected HttpResponse upload( final Payload payload, final Hc4Track track )
        throws IOException
    {
        final String url = getRemoteUrl() + payload.getPath().stringValue();
        final HttpPut put = new HttpPut( url );
        put.setEntity( new ZapperEntity( payload ) );
        put.addHeader( "X-Zapper-Transfer-ID", payload.getTransferIdentifier().stringValue() );
        return httpClient.execute( put );
    }
}
