package org.sonatype.spice.zapper.client.hc4;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.Check;

public class Hc4ClientBuilder
{
    private Parameters parameters;

    private String remoteUrl;

    private HttpHost proxyServer;

    private CredentialsProvider credentialsProvider;

    private boolean preemptiveAuth;

    public Hc4ClientBuilder( final Parameters parameters, final String remoteUrl )
    {
        this.parameters = Check.notNull( parameters, Parameters.class );
        this.remoteUrl = Check.notNull( remoteUrl, "Remote URL is null!" );
        this.preemptiveAuth = false;
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

    public Hc4ClientBuilder withPreemptiveRealm( final CredentialsProvider credentialsProvider )
    {
        this.credentialsProvider = credentialsProvider;
        this.preemptiveAuth  = true;
        return this;
    }

    public Hc4Client build()
    {
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", SSLConnectionSocketFactory.getSystemSocketFactory()).build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(parameters.getMaximumTrackCount());

        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setConnectionManager(cm).setUserAgent("Zapper/1.0-HC4");

        if ( proxyServer != null )
        {
          httpClientBuilder.setProxy(proxyServer);
        }
        if ( credentialsProvider != null )
        {
          httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }

        return new Hc4Client( parameters, remoteUrl, httpClientBuilder.build(), preemptiveAuth ? credentialsProvider : null );
    }
}
