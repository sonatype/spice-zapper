package eu.flatwhite.zapper.client.hc4;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreProtocolPNames;

import eu.flatwhite.zapper.AbstractClientTest;
import eu.flatwhite.zapper.Client;
import eu.flatwhite.zapper.Parameters;

public class Hc4ClientTest
    extends AbstractClientTest
{

    @Override
    protected Client getClient( Parameters parameters, String remoteUrl )
    {
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register( new Scheme( "http", 80, PlainSocketFactory.getSocketFactory() ) );
        schemeRegistry.register( new Scheme( "https", 443, SSLSocketFactory.getSocketFactory() ) );

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager( schemeRegistry );
        cm.setMaxTotal( 200 );
        cm.setDefaultMaxPerRoute( parameters.getMaximumTrackCount() );
        final HttpClient httpClient = new DefaultHttpClient( cm );
        httpClient.getParams().setParameter( CoreProtocolPNames.USER_AGENT, "Zapper/1.0-HC4" );
        return new Hc4Client( parameters, remoteUrl, httpClient );
    }
}
