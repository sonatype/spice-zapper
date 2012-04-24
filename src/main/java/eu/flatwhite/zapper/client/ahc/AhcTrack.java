package eu.flatwhite.zapper.client.ahc;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadSupplier;

public class AhcTrack
    implements Runnable
{
    private final AhcClient ahcClient;

    private final PayloadSupplier payloadSupplier;

    private final CountDownLatch countDownLatch;

    private ListenableFuture<Response> listenableFuture;

    private IOException exception;

    public AhcTrack( final Identifier identifier, final AhcClient ahcClient, final PayloadSupplier payloadSupplier )
    {
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
                this.listenableFuture = ahcClient.upload( payload, this );
                this.listenableFuture.addListener( this, ahcClient );
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

    @Override
    public void run()
    {
        try
        {
            final Response response = listenableFuture.get();
            if ( response.getStatusCode() > 199 && response.getStatusCode() < 300 )
            {
                upload();
            }
            else
            {
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
                setDone( new IOException( "ExecutionException", e.getCause() ) );
            }
        }
    }
}
