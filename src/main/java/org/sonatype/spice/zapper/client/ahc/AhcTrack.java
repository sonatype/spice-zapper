package org.sonatype.spice.zapper.client.ahc;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.PayloadSupplier;
import org.sonatype.spice.zapper.internal.transport.Track;
import org.sonatype.spice.zapper.internal.transport.TrackIdentifier;

import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

public class AhcTrack
    extends Track
    implements Runnable
{
    private final AhcClient ahcClient;

    private final PayloadSupplier payloadSupplier;

    private final CountDownLatch countDownLatch;

    private IOException exception;

    public AhcTrack( final TrackIdentifier trackIdentifier, final AhcClient ahcClient,
                     final PayloadSupplier payloadSupplier )
    {
        super( trackIdentifier );
        this.ahcClient = ahcClient;
        this.payloadSupplier = payloadSupplier;
        this.countDownLatch = new CountDownLatch( 1 );
        upload();
    }

    protected IOException getException()
    {
        return exception;
    }

    protected void setDone( final IOException t )
    {
        if ( t != null )
        {
            this.exception = t;
        }
        this.countDownLatch.countDown();
    }

    protected void waitUntilDone()
    {
        try
        {
            this.countDownLatch.await();
        }
        catch ( InterruptedException e )
        {
            // nothing
        }
    }

    protected void upload()
    {
        Payload payload = payloadSupplier.getNextPayload();
        if ( payload != null )
        {
            try
            {
                ahcClient.upload( payload, this );
            }
            catch ( IOException e )
            {
                setDone( e );
            }
        }
        else
        {
            // done cleanly
            setDone( null );
        }
    }
    
    private ListenableFuture<Response> listenableFuture;

    public void setListenableFuture( ListenableFuture<Response> listenableFuture )
    {
        this.listenableFuture = listenableFuture;
    }

    @Override
    public void run()
    {
        try
        {
            final Response response = listenableFuture.get();
            if ( response.getStatusCode() > 199 && response.getStatusCode() < 300 )
            {
                // got for next payload
                upload();
            }
            else
            {
                // fail
                setDone( new IOException( String.format( "Unexpected server response: %s %s", response.getStatusCode(),
                    response.getStatusText() ) ) );
            }
        }
        catch ( InterruptedException e )
        {
            setDone( new IOException( "IO interrupted!", e ) );
        }
        catch ( ExecutionException e )
        {
            final Throwable t = e.getCause();
            if ( t instanceof IOException )
            {
                setDone( (IOException) t );
            }
            else
            {
                setDone( new IOException( "ExecutionException: " + t.getMessage(), t ) );
            }
        }
    }
}
