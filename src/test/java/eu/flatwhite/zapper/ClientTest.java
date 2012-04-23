package eu.flatwhite.zapper;

import java.io.File;

import org.junit.Test;

import com.ning.http.client.Realm;
import com.ning.http.client.Realm.AuthScheme;

import eu.flatwhite.zapper.client.ahc.AhcClientBuilder;
import eu.flatwhite.zapper.fs.DirectoryIOSource;

public class ClientTest
{
    @Test
    public void upload()
        throws Exception
    {
        final Parameters parameters = ParametersBuilder.defaults().build();

        final Realm realm =
            new Realm.RealmBuilder().setPrincipal( "admin" ).setPassword( "admin123" ).setUsePreemptiveAuth( true ).setScheme(
                AuthScheme.BASIC ).build();
        final AhcClientBuilder builder =
            new AhcClientBuilder( parameters, "http://localhost:8081/nexus/content/repositories/snapshots/" ).withRealm( realm );
        final Client client = builder.build();
        final IOSourceListable directory =
            new DirectoryIOSource( new File( "/Users/cstamas/Worx/flatwhite/zapper/target/classes" ) );

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
