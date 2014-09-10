package org.sonatype.spice.zapper;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import org.sonatype.sisu.litmus.testsupport.TestSupport;
import org.sonatype.spice.zapper.codec.GzipCodec;
import org.sonatype.spice.zapper.codec.MatchingCodecSelector;
import org.sonatype.spice.zapper.fs.DirectoryIOSource;

import com.google.common.collect.ImmutableList;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Support for client tests.
 */
public abstract class AbstractClientTest
    extends TestSupport
{
  private Server server;

  private int port;

  public int getRandomPort()
      throws IOException
  {
    ServerSocket socket = new ServerSocket(0);
    try {
      return socket.getLocalPort();
    }
    finally {
      socket.close();
    }
  }

  protected List<Handler> getHandlers() {
    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setWelcomeFiles(new String[]{"index.html"});
    resourceHandler.setResourceBase(new File("target/test-classes/server").getAbsolutePath());

    DeployHandler deployHandler = new DeployHandler(null);

    DefaultHandler defaultHandler = new DefaultHandler();

    return ImmutableList.<Handler>of(resourceHandler, deployHandler, defaultHandler);
  }

  @Before
  public void startJetty()
      throws Exception
  {
    port = getRandomPort();
    server = new Server();
    SelectChannelConnector connector = new SelectChannelConnector();
    connector.setPort(port);
    server.addConnector(connector);

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(getHandlers().toArray(new Handler[0]));
    server.setHandler(handlers);

    server.start();
  }

  @After
  public void stopJetty()
      throws Exception
  {
    if (server != null) {
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
    System.out.println("Done in " + ((r1 + r2) / 2) + " ms.");
  }

  // ==

  protected long timedUpload()
      throws Exception
  {
    final MatchingCodecSelector codecSelector = MatchingCodecSelector.builder().add(".*", new GzipCodec()).build();
    final Parameters parameters = ParametersBuilder.defaults().setCodecSelector(codecSelector).build();

    final Client client = getClient(parameters, "http://localhost:" + port + "/");
    final IOSourceListable directory = new DirectoryIOSource(new File("target/classes"));

    final long started = System.currentTimeMillis();
    try {
      client.upload(directory);
    }
    finally {
      client.close();
    }
    return System.currentTimeMillis() - started;
  }

  // ==

  protected abstract Client getClient(final Parameters parameters, final String remoteUrl);
}
