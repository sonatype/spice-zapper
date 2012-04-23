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

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.Protocol;
import eu.flatwhite.zapper.internal.StringIdentifier;

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

    public AbstractChargerClient( final Parameters parameters )
    {
        super( parameters );
        this.charger = new DefaultCharger();
    }

    @Override
    protected void doUpload( final Protocol protocol, final int trackCount, final PayloadSupplier payloadSupplier )
        throws IOException
    {
        final List<Callable<State>> tracks = new ArrayList<Callable<State>>( trackCount );
        final SimpleCallableExecutor simpleCallableExecutor = new SimpleCallableExecutor( trackCount );
        for ( int i = 0; i < trackCount; i++ )
        {
            tracks.add( createCallable( new StringIdentifier( String.valueOf( i ) ), payloadSupplier ) );
        }

        final ChargeFuture<State> chargeFuture =
            charger.submit( tracks, getExceptionHandler(), new AllArrivedChargeStrategy<State>(),
                simpleCallableExecutor );

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
        finally
        {
            simpleCallableExecutor.shutdown();
        }
    }

    // ==

    protected abstract Callable<State> createCallable( final Identifier identifier,
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
