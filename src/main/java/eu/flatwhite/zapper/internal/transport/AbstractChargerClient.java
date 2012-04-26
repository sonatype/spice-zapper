package eu.flatwhite.zapper.internal.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.sonatype.sisu.charger.ChargeFuture;
import org.sonatype.sisu.charger.Charger;
import org.sonatype.sisu.charger.ExceptionHandler;
import org.sonatype.sisu.charger.internal.AllArrivedChargeStrategy;
import org.sonatype.sisu.charger.internal.DefaultCharger;

import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.Protocol;

/**
 * Client using "charger" that handles multi-thread invocations. Obviously, this is not needed if the actual underlying
 * transport is asynchronous for example.
 * 
 * @author cstamas
 */
public abstract class AbstractChargerClient
    extends AbstractClient
{
    private final Charger charger;

    private final SimpleCallableExecutor executor;

    public AbstractChargerClient( final Parameters parameters, final String remoteUrl )
    {
        super( parameters, remoteUrl );
        this.charger = new DefaultCharger();
        this.executor = new SimpleCallableExecutor( parameters.getMaximumTrackCount() );
    }

    @Override
    public void close()
    {
        executor.shutdown();
    }

    @Override
    protected void doUpload( final Protocol protocol, final int trackCount, final PayloadSupplier payloadSupplier )
        throws IOException
    {
        final List<Callable<State>> tracks = new ArrayList<Callable<State>>( trackCount );
        for ( int i = 0; i < trackCount; i++ )
        {
            tracks.add( createCallable( protocol, i, payloadSupplier ) );
        }

        final ChargeFuture<State> chargeFuture =
            charger.submit( tracks, getExceptionHandler(), new AllArrivedChargeStrategy<State>(), executor );

        try
        {
            chargeFuture.getResult();
        }
        catch ( IOException e )
        {
            if ( e.getCause() == null )
            {
                throw new IOException( "IO failure", e );
            }
            throw e;
        }
        catch ( Exception e )
        {
            throw new IOException( e );
        }
    }

    // ==

    protected abstract Callable<State> createCallable( final Protocol protocol, final int trackNo,
                                                       final PayloadSupplier payloadSupplier );

    protected static ExceptionHandler NON_HANDLING_EXCEPTION_HANDLER = new ExceptionHandler()
    {
        @Override
        public boolean handle( Exception ex )
        {
            return false;
        }
    };

    protected ExceptionHandler getExceptionHandler()
    {
        return NON_HANDLING_EXCEPTION_HANDLER;
    }
}
