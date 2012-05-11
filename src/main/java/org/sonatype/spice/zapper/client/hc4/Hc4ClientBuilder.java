package org.sonatype.spice.zapper.client.hc4;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreProtocolPNames;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.Check;

public class Hc4ClientBuilder
{
    private Parameters parameters;

    private String remoteUrl;

    private HttpHost proxyServer;

    private CredentialsProvider credentialsProvider;

    public Hc4ClientBuilder( final Parameters parameters, final String remoteUrl )
    {
        this.parameters = Check.notNull( parameters, Parameters.class );
        this.remoteUrl = Check.notNull( remoteUrl, "Remote URL is null!" );
    }

    public Hc4ClientBuilder withProxy( final HttpHost proxyServer )
    {
        this.proxyServer = proxyServer;
        return this;
    }

    public Hc4ClientBuilder withRealm( final CredentialsProvider credentialsProvider )
    {
        this.credentialsProvider = credentialsProvider;
        return this;
    }

    public Hc4Client build()
    {
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register( new Scheme( "http", 80, PlainSocketFactory.getSocketFactory() ) );
        schemeRegistry.register( new Scheme( "https", 443, SSLSocketFactory.getSocketFactory() ) );

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager( schemeRegistry );
        cm.setMaxTotal( 200 );
        cm.setDefaultMaxPerRoute( parameters.getMaximumTrackCount() );

        DefaultHttpClient httpClient = new DefaultHttpClient( cm );
        httpClient.getParams().setParameter( CoreProtocolPNames.USER_AGENT, "Zapper/1.0-HC4" );
        if ( proxyServer != null )
        {
            httpClient.getParams().setParameter( ConnRoutePNames.DEFAULT_PROXY, proxyServer );
        }
        if ( credentialsProvider != null )
        {
            httpClient.setCredentialsProvider( credentialsProvider );
        }

        return new Hc4Client( parameters, remoteUrl, httpClient );
    }
}
