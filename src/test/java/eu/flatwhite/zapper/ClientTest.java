package eu.flatwhite.zapper;

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

import com.ning.http.client.Realm;
import com.ning.http.client.Realm.AuthScheme;

import eu.flatwhite.zapper.client.ahc.AhcClientBuilder;
import eu.flatwhite.zapper.fs.DirectoryIOSource;

public class ClientTest
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

        DeployHandler deployHandler = new DeployHandler();

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
    public void upload()
        throws Exception
    {
        final Parameters parameters = ParametersBuilder.defaults().build();

        final Realm realm =
            new Realm.RealmBuilder().setPrincipal( "admin" ).setPassword( "admin123" ).setUsePreemptiveAuth( true ).setScheme(
                AuthScheme.BASIC ).build();
        final AhcClientBuilder builder =
            new AhcClientBuilder( parameters, "http://localhost:" + port + "/" ).withRealm( realm );
        final Client client = builder.build();
        final IOSourceListable directory = new DirectoryIOSource( new File( "target/classes" ) );

        try
        {
            client.upload( directory );
        }
        finally
        {
            client.close();
        }
    }
}
