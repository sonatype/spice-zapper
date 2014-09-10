package org.sonatype.spice.zapper.client.hc4;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sonatype.spice.zapper.AbstractClientTest;
import org.sonatype.spice.zapper.Client;
import org.sonatype.spice.zapper.Parameters;

import com.google.common.collect.ImmutableList;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


public class Hc4ClientPreemptiveAuthTest
    extends AbstractClientTest
{
  protected static final String USERNAME = "username";

  protected static final String PASSWORD = "secret";

  // This is USERNAME:PASSWORD Base64 encoded
  protected static final String BASE64_AUTH_VALUE = "dXNlcm5hbWU6c2VjcmV0";

  /**
   * Adding a handler that enforces that auth is sent preemptively with correct values.
   */
  protected List<Handler> getHandlers() {
    final List<Handler> handlers = super.getHandlers();

    final Handler authEnforcingHandler = new AbstractHandler()
    {
      public void handle(final String target, final Request baseRequest, final HttpServletRequest request,
                         final HttpServletResponse response)
          throws IOException, ServletException
      {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.trim().length() == 0) {
          // header MUST be present
          throw new IllegalArgumentException("Auth header must be present");
        }
        if (!authHeader.contains("Basic")) {
          // header MUST be for Basic auth
          throw new IllegalArgumentException("Auth header must be for Basic auth");
        }
        if (!authHeader.contains(BASE64_AUTH_VALUE)) {
          // header MUST be for USERNAME
          throw new IllegalArgumentException("Auth header must be for USERNAME");
        }
      }
    };

    return ImmutableList.<Handler>builder().add(authEnforcingHandler).addAll(handlers).build();
  }

  @Override
  protected Client getClient(Parameters parameters, String remoteUrl) {
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USERNAME, PASSWORD));
    return new Hc4ClientBuilder(parameters, remoteUrl).withPreemptiveRealm(credentialsProvider).build();
  }
}
