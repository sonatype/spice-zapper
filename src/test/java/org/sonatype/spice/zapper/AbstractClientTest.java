package org.sonatype.spice.zapper;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonatype.spice.zapper.fs.DirectoryIOSource;

public abstract class AbstractClientTest
{
    private Server server;

    private int port;

    public int getRandomPort()
        throws IOException
    {
        ServerSocket socket = new ServerSocket( 0 );
        try
        {
            return socket.getLocalPort();
        }
        finally
        {
            socket.close();
        }
    }

    @Before
    public void startJetty()
        throws Exception
    {
        port = getRandomPort();
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort( port );
        server.addConnector( connector );

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed( true );
        resourceHandler.setWelcomeFiles( new String[] { "index.html" } );
        resourceHandler.setResourceBase( new File( "target/test-classes/server" ).getAbsolutePath() );

        DeployHandler deployHandler = new DeployHandler( null );

        HandlerList handlers = new HandlerList();
        handlers.setHandlers( new Handler[] { resourceHandler, deployHandler, new DefaultHandler() } );
        server.setHandler( handlers );

        server.start();
    }

    @After
    public void stopJetty()
        throws Exception
    {
        if ( server != null )
        {
            server.stop();
            server = null;
        }
    }

    @Test
    public void uploadTest()
        throws Exception
    {
        // we run it twice to avoid any "warmup" problems
        final long r1 = timedUpload();
        final long r2 = timedUpload();
        System.out.println( "Done in " + ( ( r1 + r2 ) / 2 ) + " ms." );
    }

    // ==

    protected long timedUpload()
        throws Exception
    {
        final Parameters parameters = ParametersBuilder.defaults().build();

        final Client client = getClient( parameters, "http://localhost:" + port + "/" );
        final IOSourceListable directory = new DirectoryIOSource( new File( "target/classes" ) );

        final long started = System.currentTimeMillis();
        try
        {
            client.upload( directory );
        }
        finally
        {
            client.close();
        }
        return System.currentTimeMillis() - started;
    }

    // ==

    protected abstract Client getClient( final Parameters parameters, final String remoteUrl );
}
