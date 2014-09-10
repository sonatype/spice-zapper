package org.sonatype.spice.zapper.client.hc4;

import org.sonatype.spice.zapper.AbstractClientTest;
import org.sonatype.spice.zapper.Client;
import org.sonatype.spice.zapper.Parameters;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;


public class Hc4ClientTest
    extends AbstractClientTest
{
  @Override
  protected Client getClient(Parameters parameters, String remoteUrl) {
    return new Hc4ClientBuilder(parameters, remoteUrl).build();
  }
}
