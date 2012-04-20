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

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.IOSourceListable;
import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.client.Client;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.StringIdentifier;
import eu.flatwhite.zapper.internal.WholeFilePayloadSupplier;

public abstract class AbstractClient
    implements Client
{
    private final Parameters parameters;

    private final Charger charger;

    public AbstractClient( final Parameters parameters )
    {
        this.parameters = Check.notNull( parameters, Parameters.class );
        this.charger = new DefaultCharger();
    }

    @Override
    public void upload( IOSourceListable listableSource )
        throws IOException
    {
        upload( listableSource, listableSource.listFiles() );
    }

    @Override
    public void upload( IOSource source, Path... paths )
        throws IOException
    {
        final ArrayList<ZFile> zfiles = new ArrayList<ZFile>();
        for ( Path path : paths )
        {
            zfiles.add( source.createZFile( path ) );
        }
        upload( source, zfiles );
    }

    @Override
    public void download( IOTarget target, Path... paths )
        throws IOException
    {
        throw new UnsupportedOperationException( "Not implemented!" );
    }

    protected void upload( final IOSource source, final List<ZFile> zfiles )
        throws IOException
    {
        final PayloadSupplier payloadSupplier = getPayloadSupplier( source, zfiles );
        final int trackCount = Math.max( parameters.getMaximumSessionCount(), zfiles.size() );
        final List<Callable<State>> tracks = new ArrayList<Callable<State>>( trackCount );
        final SimpleCallableExecutor simpleCallableExecutor =
            new SimpleCallableExecutor( parameters.getMaximumSessionCount() );
        for ( int i = 0; i < trackCount; i++ )
        {
            tracks.add( createCallable( new StringIdentifier( String.valueOf( i ) ), payloadSupplier ) );
        }

        ChargeFuture<State> chargeFuture =
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

    protected abstract AbstractTrack createCallable( final Identifier identifier, final PayloadSupplier payloadSupplier );

    protected PayloadSupplier getPayloadSupplier( final IOSource source, final List<ZFile> zfiles )
    {
        return new WholeFilePayloadSupplier( source, zfiles );
    }

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
