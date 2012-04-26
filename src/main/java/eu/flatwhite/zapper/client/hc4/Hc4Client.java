package eu.flatwhite.zapper.client.hc4;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;

import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.Protocol;
import eu.flatwhite.zapper.internal.transport.AbstractChargerClient;
import eu.flatwhite.zapper.internal.transport.State;

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
