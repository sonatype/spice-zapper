package org.sonatype.spice.zapper.client.ahc;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import org.sonatype.spice.zapper.AggregatingIOException;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.Transfer;
import org.sonatype.spice.zapper.internal.transport.AbstractClient;
import org.sonatype.spice.zapper.internal.transport.State;
import org.sonatype.spice.zapper.internal.transport.TrackIdentifier;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.Realm;
import com.ning.http.client.Response;

public class AhcClient
    extends AbstractClient<AhcTrack>
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
    protected void doUpload( final Transfer transfer, final Protocol protocol, final int trackCount )
        throws IOException
    {
        final AhcTrack[] tracks = new AhcTrack[trackCount];
        for ( int i = 0; i < trackCount; i++ )
        {
            tracks[i] =
                new AhcTrack( new TrackIdentifier( "T" + String.valueOf( i ) ), this, transfer.getPayloadSupplier() );
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

    public State upload( final Payload payload, final AhcTrack ahcTrack )
        throws IOException
    {
        final String url = getRemoteUrl() + payload.getPath().stringValue();
        final BoundRequestBuilder requestBuilder =
            asyncHttpClient.preparePut( url ).setBody( new ZapperBodyGenerator( payload ) ).setHeader(
                "X-Zapper-Transfer-ID", payload.getTransferIdentifier().stringValue() );
        if ( realm != null )
        {
            requestBuilder.setRealm( realm );
        }
        if ( proxyServer != null )
        {
            requestBuilder.setProxyServer( proxyServer );
        }
        ListenableFuture<Response> future = asyncHttpClient.executeRequest( requestBuilder.build() );
        ahcTrack.setListenableFuture( future );
        future.addListener( ahcTrack, this );
        return State.SUCCESS;
    }

    public State upload( final Payload payload )
        throws IOException
    {
        final String url = getRemoteUrl() + payload.getPath().stringValue();
        final BoundRequestBuilder requestBuilder =
            asyncHttpClient.preparePut( url ).setBody( new ZapperBodyGenerator( payload ) ).setHeader(
                "X-Zapper-Transfer-ID", payload.getTransferIdentifier().stringValue() );
        if ( realm != null )
        {
            requestBuilder.setRealm( realm );
        }
        if ( proxyServer != null )
        {
            requestBuilder.setProxyServer( proxyServer );
        }
        ListenableFuture<Response> future = asyncHttpClient.executeRequest( requestBuilder.build() );
        Response response;
        try
        {
            response = future.get();
        }
        catch ( InterruptedException e )
        {
            return State.FAILURE;
        }
        catch ( ExecutionException e )
        {
            throw new IOException( "Execution failed!", e );
        }
        if ( !( response.getStatusCode() > 199 && response.getStatusCode() < 299 ) )
        {
            throw new IOException( String.format( "Unexpected server response: %s %s", response.getStatusCode(),
                response.getStatusText() ) );
        }
        return State.SUCCESS;
    }

    @Override
    public void execute( final Runnable command )
    {
        command.run();
    }
}
