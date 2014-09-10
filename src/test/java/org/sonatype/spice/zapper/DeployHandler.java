package org.sonatype.spice.zapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.server.Dispatcher;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandler.Context;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.util.resource.Resource;

public class DeployHandler
    extends HandlerWrapper
{
    private final Resource baseResource;

    private ContextHandler context;

    public DeployHandler( final Resource baseResource )
    {
        this.baseResource = baseResource;
    }

    @Override
    public void doStart()
        throws Exception
    {
        Context scontext = ContextHandler.getCurrentContext();
        context = ( scontext == null ? null : scontext.getContextHandler() );
        super.doStart();
    }

    public Resource getResource( String path )
        throws MalformedURLException
    {
        if ( path == null || !path.startsWith( "/" ) )
            throw new MalformedURLException( path );

        Resource base = baseResource;
        if ( base == null )
        {
            if ( context == null )
            {
                return null;
            }
            base = context.getBaseResource();
            if ( base == null )
            {
                return null;
            }
        }

        try
        {
            path = URIUtil.canonicalPath( path );
            return base.addPath( path );
        }
        catch ( Exception e )
        {
            // ignore
        }

        return null;
    }

    protected String getResourcePath( HttpServletRequest request )
        throws MalformedURLException
    {
        String servletPath;
        String pathInfo;
        final Boolean included = request.getAttribute( Dispatcher.INCLUDE_REQUEST_URI ) != null;
        if ( included != null && included.booleanValue() )
        {
            servletPath = (String) request.getAttribute( Dispatcher.INCLUDE_SERVLET_PATH );
            pathInfo = (String) request.getAttribute( Dispatcher.INCLUDE_PATH_INFO );

            if ( servletPath == null && pathInfo == null )
            {
                servletPath = request.getServletPath();
                pathInfo = request.getPathInfo();
            }
        }
        else
        {
            servletPath = request.getServletPath();
            pathInfo = request.getPathInfo();
        }

        return URIUtil.addPaths( servletPath, pathInfo );
    }

    public void handle( String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        if ( baseRequest.isHandled() )
            return;

        if ( HttpMethods.PUT.equals( request.getMethod() ) )
        {
            System.out.println("PUT " + request.getRequestURI());
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
