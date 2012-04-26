package eu.flatwhite.zapper.client.ahc;

import eu.flatwhite.zapper.AbstractClientTest;
import eu.flatwhite.zapper.Client;
import eu.flatwhite.zapper.Parameters;

public class AhcClientTest
    extends AbstractClientTest
{
    @Override
    protected Client getClient( final Parameters parameters, final String remoteUrl )
    {
        // final Realm realm =
        // new Realm.RealmBuilder().setPrincipal( "admin" ).setPassword( "admin123" ).setUsePreemptiveAuth( true
        // ).setScheme(
        // AuthScheme.BASIC ).build();
        // final AhcClientBuilder builder = new AhcClientBuilder( parameters, remoteUrl ).withRealm( realm );
        final AhcClientBuilder builder = new AhcClientBuilder( parameters, remoteUrl );
        return builder.build();
    }
}
