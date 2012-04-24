package eu.flatwhite.zapper.client.ahc;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.Realm;

import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.internal.Check;

public class AhcClientBuilder
{
    private Parameters parameters;

    private String remoteUrl;

    private ProxyServer proxyServer;

    private Realm realm;

    public AhcClientBuilder( final Parameters parameters, final String remoteUrl )
    {
        this.parameters = Check.notNull( parameters, Parameters.class );
        this.remoteUrl = Check.notNull( remoteUrl, "Remote URL is null!" );
    }

    public AhcClientBuilder withProxy( final ProxyServer proxyServer )
    {
        this.proxyServer = proxyServer;
        return this;
    }

    public AhcClientBuilder withRealm( final Realm realm )
    {
        this.realm = realm;
        return this;
    }

    public AhcClient build()
    {
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        // make it match track count
        builder.setMaximumConnectionsPerHost( parameters.getMaximumTrackCount() );
        // enable compression
        builder.setCompressionEnabled( true );
        //builder.setRequestCompressionLevel( 6 );
        // set UA
        builder.setUserAgent( "Zapper/1.0" );
        final AsyncHttpClient asyncHttpClient = new AsyncHttpClient( builder.build() );
        return new AhcClient( parameters, remoteUrl, asyncHttpClient, realm, proxyServer );
    }
}
