package eu.flatwhite.zapper;

import org.junit.Test;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;

public class AhcTest
{
    //@Test
    public void isThisABug()
        throws Exception
    {
        final AsyncHttpClientConfig.Builder configBuilder = new AsyncHttpClientConfig.Builder();
        configBuilder.setCompressionEnabled( true );
        //configBuilder.setRequestCompressionLevel( 6 );
        final AsyncHttpClient client = new AsyncHttpClient( configBuilder.build() );

        try
        {
            final Response response = client.prepareGet( "http://www.google.com/" ).execute().get();
            System.out.println( response.getStatusCode() + " " + response.getStatusText() );
        }
        finally
        {
            client.close();
        }
    }

}
