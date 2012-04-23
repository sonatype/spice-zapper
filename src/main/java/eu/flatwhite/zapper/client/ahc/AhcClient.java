package eu.flatwhite.zapper.client.ahc;

import java.io.IOException;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Request;
import com.ning.http.client.Response;

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
    private final Logger logger;

    private final AsyncHttpClient asyncHttpClient;

    private final String remoteUrl;

    public AhcClient( final Parameters parameters, final String remoteUrl, final AsyncHttpClient asyncHttpClient )
    {
        super( parameters );
        this.logger = LoggerFactory.getLogger( getClass() );
        this.asyncHttpClient = Check.notNull( asyncHttpClient, AsyncHttpClient.class );
        this.remoteUrl = Check.notNull( remoteUrl, "Remote URL is null!" );
    }

    @Override
    public void close()
    {
        asyncHttpClient.close();
    }

    // ==

    protected Logger getLogger()
    {
        return logger;
    }

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
            throw new AggregatingIOException( "Upload failed!", trackExceptions );
        }
    }

    protected ListenableFuture<Response> upload( final Payload payload, final AhcTrack ahcTrack )
        throws IOException
    {
        final String url = remoteUrl + payload.getPath().stringValue();
        final Request request =
            asyncHttpClient.preparePut( url ).setBody( new ZapperBodyGenerator( payload ) ).setHeader(
                "X-Zapper-Transfer-ID", payload.getTransferIdentifier().stringValue() ).build();
        return asyncHttpClient.executeRequest( request );
    }

    @Override
    public void execute( final Runnable command )
    {
        command.run();
    }
}
