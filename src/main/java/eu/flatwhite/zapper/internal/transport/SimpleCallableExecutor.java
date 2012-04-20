package eu.flatwhite.zapper.internal.transport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.sonatype.sisu.charger.CallableExecutor;
import org.sonatype.sisu.charger.internal.ChargeWrapper;

public class SimpleCallableExecutor
    implements CallableExecutor
{
    private final ExecutorService executorService;

    public SimpleCallableExecutor( final int size )
    {
        this.executorService = Executors.newFixedThreadPool( size );
    }

    @Override
    public <T> Future<T> submit( ChargeWrapper<T> task )
    {
        return executorService.submit( task );
    }

    public void shutdown()
    {
        executorService.shutdownNow();
    }
}
