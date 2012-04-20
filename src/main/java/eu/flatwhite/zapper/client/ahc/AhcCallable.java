package eu.flatwhite.zapper.client.ahc;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Request;
import com.ning.http.client.Response;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.transport.AbstractTrack;
import eu.flatwhite.zapper.internal.transport.State;

public class AhcCallable
    extends AbstractTrack
    implements Callable<State>
{
    private final AsyncHttpClient asyncHttpClient;

    private final PayloadSupplier payloadSupplier;

    private final String baseUrl;

    public AhcCallable( final Identifier identifier, final AsyncHttpClient asyncHttpClient,
                        final PayloadSupplier payloadSupplier, final String baseUrl )
    {
        super( identifier );
        this.asyncHttpClient = Check.notNull( asyncHttpClient, AsyncHttpClient.class );
        this.payloadSupplier = Check.notNull( payloadSupplier, PayloadSupplier.class );
        this.baseUrl = Check.notNull( baseUrl, "BaseURL is null!" );
    }

    @Override
    public State call()
        throws Exception
    {
        Payload payload = payloadSupplier.getNextPayload();

        while ( payload != null )
        {
            final String url = baseUrl + payload.getPath().stringValue();
            getLogger().info( url );
            final Request request =
                asyncHttpClient.preparePut( url ).setBody( new ZapperBodyGenerator( payload ) ).build();
            ListenableFuture<Response> requestFuture = asyncHttpClient.executeRequest( request );
            final Response response = requestFuture.get();
            if ( response.getStatusCode() > 299 )
            {
                throw new IOException( String.format( "Unexpected server response: %s %s", response.getStatusCode(),
                    response.getStatusText() ) );
            }

            payload = payloadSupplier.getNextPayload();
        }

        return State.SUCCESS;
    }
}
