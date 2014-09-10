package org.sonatype.spice.zapper.internal.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.PayloadSupplier;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.Transfer;

/**
 * Client using "charger" that handles multi-thread invocations. Obviously, this is not needed if the actual underlying
 * transport is asynchronous for example.
 * 
 * @author cstamas
 */
public abstract class AbstractChargerClient<T extends AbstractChargerTrack>
    extends AbstractClient<T>
{
    public AbstractChargerClient( final Parameters parameters, final String remoteUrl )
    {
        super( parameters, remoteUrl );
    }

    public void close()
    {
      // nop
    }

    @Override
    protected void doUpload( final Transfer transfer, final Protocol protocol, final int trackCount )
        throws IOException
    {
        final ExecutorService executorService = Executors.newFixedThreadPool(getParameters().getMaximumTrackCount());
        final PayloadSupplier payloadSupplier = transfer.getPayloadSupplier();
        final List<Callable<State>> tracks = new ArrayList<Callable<State>>( trackCount );
        for ( int i = 0; i < trackCount; i++ )
        {
            tracks.add( createCallable( transfer.getNextTrackIdentifier(), transfer, protocol, payloadSupplier ) );
        }

        try
        {
          // execute all tracks
          final List<Future<State>> futures = executorService.invokeAll(tracks);
          // and block until all done
          for (Future<State> future : futures) {
            future.get();
          }
        }
        catch ( Exception e )
        {
            final IOException ee = new IOException( "Failure:" + e.toString() );
            ee.initCause( e );
            throw ee;
        } finally {
            executorService.shutdownNow();
        }
    }

    // ==

    protected abstract Callable<State> createCallable( final TrackIdentifier trackIdentifier, final Transfer transfer,
                                                       final Protocol protocol, final PayloadSupplier payloadSupplier );
}
