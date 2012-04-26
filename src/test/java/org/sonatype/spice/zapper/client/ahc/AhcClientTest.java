package org.sonatype.spice.zapper.client.ahc;

import org.sonatype.spice.zapper.AbstractClientTest;
import org.sonatype.spice.zapper.Client;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.client.ahc.AhcClientBuilder;


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
