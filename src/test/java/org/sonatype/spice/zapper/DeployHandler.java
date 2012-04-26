package org.sonatype.spice.zapper;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;

public class DeployHandler
    extends HandlerWrapper
{
    public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        if ( baseRequest.isHandled() )
            return;

        if ( HttpMethods.PUT.equals( request.getMethod() ) )
        {
            // let's make client push the content too
            consumeStream( request.getInputStream() );
            baseRequest.setHandled( true );
        }
    }

    // ==

    protected void consumeStream( final InputStream is )
        throws IOException
    {
        final byte[] buffer = new byte[2048];
        while ( is.read( buffer ) > -1 )
        {
            // nope
        }
        is.close();
    }
}
