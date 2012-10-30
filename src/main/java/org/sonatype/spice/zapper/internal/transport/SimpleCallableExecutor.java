package org.sonatype.spice.zapper.internal.transport;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.sonatype.sisu.charger.CallableExecutor;

public class SimpleCallableExecutor
    implements CallableExecutor
{
    private final ExecutorService executorService;

    public SimpleCallableExecutor( final int size )
    {
        this.executorService = Executors.newFixedThreadPool( size );
    }

    public <T> Future<T> submit( Callable<T> task )
    {
        return executorService.submit( task );
    }

    public void shutdown()
    {
        executorService.shutdownNow();
    }
}
