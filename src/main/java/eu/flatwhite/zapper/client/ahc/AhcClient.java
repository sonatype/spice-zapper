package eu.flatwhite.zapper.client.ahc;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.Realm;
import com.ning.http.client.Response;

import eu.flatwhite.zapper.AggregatingIOException;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.Protocol;
import eu.flatwhite.zapper.internal.StringIdentifier;
import eu.flatwhite.zapper.internal.transport.AbstractClient;

public class AhcClient
    extends AbstractClient
    implements Executor
{
    private final AsyncHttpClient asyncHttpClient;

    private final Realm realm;

    private final ProxyServer proxyServer;

    public AhcClient( final Parameters parameters, final String remoteUrl, final AsyncHttpClient asyncHttpClient )
    {
        this( parameters, remoteUrl, asyncHttpClient, null, null );
    }

    public AhcClient( final Parameters parameters, final String remoteUrl, final AsyncHttpClient asyncHttpClient,
                      final Realm realm, final ProxyServer proxyServer )
    {
        super( parameters, remoteUrl );
        this.asyncHttpClient = Check.notNull( asyncHttpClient, AsyncHttpClient.class );
        this.realm = realm;
        this.proxyServer = proxyServer;
    }

    @Override
    public void close()
    {
        asyncHttpClient.close();
    }

    // ==

    @Override
    protected void doUpload( final Protocol protocol, final int trackCount, final PayloadSupplier payloadSupplier )
        throws IOException
    {
        final AhcTrack[] tracks = new AhcTrack[trackCount];
        for ( int i = 0; i < trackCount; i++ )
        {
            tracks[i] = new AhcTrack( new StringIdentifier( String.valueOf( i ) ), this, payloadSupplier );
        }
        final IOException[] trackExceptions = new IOException[trackCount];
        boolean success = true;
        for ( int i = 0; i < trackCount; i++ )
        {
            tracks[i].waitUntilDone();
            if ( tracks[i].getException() != null )
            {
                success = false;
                trackExceptions[i] = tracks[i].getException();
            }
        }

        if ( !success )
        {
            throw new AggregatingIOException( "Upload failed.", trackExceptions );
        }
    }

    protected ListenableFuture<Response> upload( final Payload payload, final AhcTrack ahcTrack )
        throws IOException
    {
        final BoundRequestBuilder requestBuilder =
            asyncHttpClient.preparePut( payload.getUrl() ).setBody( new ZapperBodyGenerator( payload ) ).setHeader(
                "X-Zapper-Transfer-ID", payload.getTransferIdentifier().stringValue() );
        if ( realm != null )
        {
            requestBuilder.setRealm( realm );
        }
        if ( proxyServer != null )
        {
            requestBuilder.setProxyServer( proxyServer );
        }
        return asyncHttpClient.executeRequest( requestBuilder.build() );
    }

    @Override
    public void execute( final Runnable command )
    {
        command.run();
    }
}
