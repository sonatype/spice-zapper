package eu.flatwhite.zapper.client.ahc;

import com.ning.http.client.AsyncHttpClient;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.transport.AbstractChargerClient;

public class AhcClient
    extends AbstractChargerClient
{
    private final AsyncHttpClient asyncHttpClient;

    private final String remoteUrl;

    public AhcClient( final Parameters parameters, final String remoteUrl, final AsyncHttpClient asyncHttpClient )
    {
        super( parameters );
        this.asyncHttpClient = Check.notNull( asyncHttpClient, AsyncHttpClient.class );
        this.remoteUrl = Check.notNull( remoteUrl, "Remote URL is null!" );
    }

    @Override
    public void close()
    {
        asyncHttpClient.close();
    }

    // ==

    @Override
    protected AhcCallable createCallable( final Identifier identifier, final PayloadSupplier payloadSupplier )
    {
        return new AhcCallable( identifier, asyncHttpClient, payloadSupplier, remoteUrl );
    }

}
