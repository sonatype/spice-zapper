/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.spice.zapper.client.hc4;

import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.Check;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class Hc4ClientBuilder
{
  private Parameters parameters;

  private String remoteUrl;

  private HttpHost proxyServer;

  private CredentialsProvider credentialsProvider;

  private boolean preemptiveAuth;

  public Hc4ClientBuilder(final Parameters parameters, final String remoteUrl) {
    this.parameters = Check.notNull(parameters, Parameters.class);
    this.remoteUrl = Check.notNull(remoteUrl, "Remote URL is null!");
    this.preemptiveAuth = false;
  }

  public Hc4ClientBuilder withProxy(final HttpHost proxyServer) {
    this.proxyServer = proxyServer;
    return this;
  }

  public Hc4ClientBuilder withRealm(final CredentialsProvider credentialsProvider) {
    this.credentialsProvider = credentialsProvider;
    return this;
  }

  public Hc4ClientBuilder withPreemptiveRealm(final CredentialsProvider credentialsProvider) {
    this.credentialsProvider = credentialsProvider;
    this.preemptiveAuth = true;
    return this;
  }

  public Hc4Client build() {
    final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", PlainConnectionSocketFactory.getSocketFactory())
        .register("https", SSLConnectionSocketFactory.getSystemSocketFactory()).build();

    final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
    cm.setMaxTotal(200);
    cm.setDefaultMaxPerRoute(parameters.getMaximumTrackCount());

    final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setConnectionManager(cm)
        .setUserAgent("Zapper/1.0-HC4");

    if (proxyServer != null) {
      httpClientBuilder.setProxy(proxyServer);
    }
    if (credentialsProvider != null) {
      httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
    }

    return new Hc4Client(parameters, remoteUrl, httpClientBuilder.build(), preemptiveAuth ? credentialsProvider : null);
  }
}
