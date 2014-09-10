package org.sonatype.spice.zapper.client.hc4;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.PayloadSupplier;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.SegmentPayload;
import org.sonatype.spice.zapper.internal.Transfer;
import org.sonatype.spice.zapper.internal.transport.AbstractChargerClient;
import org.sonatype.spice.zapper.internal.transport.State;
import org.sonatype.spice.zapper.internal.transport.TrackIdentifier;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class Hc4Client
    extends AbstractChargerClient<Hc4Track>
{
  private final CloseableHttpClient httpClient;

  private final CredentialsProvider preemptiveCredentialsProvider;

  public Hc4Client(final Parameters parameters,
                   final String remoteUrl,
                   final CloseableHttpClient httpClient,
                   final CredentialsProvider preemptiveCredentialsProvider)
  {
    super(parameters, remoteUrl);
    this.httpClient = Check.notNull(httpClient, CloseableHttpClient.class);
    this.preemptiveCredentialsProvider = preemptiveCredentialsProvider;
  }

  @Override
  public void close() {
    try {
      httpClient.close();
    }
    catch (IOException e) {
      getLogger().warn("Could not cleanly close httpClient", e);
    }
    super.close();
  }

  // ==

  @Override
  protected Callable<State> createCallable(final TrackIdentifier trackIdentifier, final Transfer transfer,
                                           final Protocol protocol, final PayloadSupplier payloadSupplier)
  {
    return new Hc4Track(trackIdentifier, payloadSupplier, this);
  }

  // ==

  @Override
  public State upload(final Payload payload, final Hc4Track track)
      throws IOException
  {
    final String url = getRemoteUrl() + payload.getPath().stringValue();
    final HttpPut put = new HttpPut(url);
    if (payload instanceof SegmentPayload) {
      put.setEntity(new ZapperEntity(payload, getParameters().getCodecSelector().selectCodecs(
          SegmentPayload.class.cast(payload).getSegment().getZFile())));
    }
    else {
      put.setEntity(new ZapperEntity(payload));
    }
    put.addHeader("X-Zapper-Transfer-ID", payload.getTransferIdentifier().stringValue());
    if (track != null) {
      put.addHeader("X-Zapper-Track-ID", track.getIdentifier().stringValue());
    }
    final HttpClientContext context = new HttpClientContext();
    if (preemptiveCredentialsProvider != null) {
      context.setCredentialsProvider(preemptiveCredentialsProvider);
      context.setAuthCache(new BasicAuthCache());
      context.getAuthCache().put(new HttpHost(put.getURI().getHost(), put.getURI().getPort(), put.getURI().getScheme()),
          new BasicScheme());
    }
    final HttpResponse response = httpClient.execute(put, context);
    final StatusLine statusLine = response.getStatusLine();
    EntityUtils.consume(response.getEntity());
    if (!(statusLine.getStatusCode() > 199 && statusLine.getStatusCode() < 299)) {
      throw new IOException(String.format("Unexpected server response: %s %s", statusLine.getStatusCode(),
          statusLine.getReasonPhrase()));
    }
    return State.SUCCESS;
  }

  @Override
  public State upload(final Payload payload)
      throws IOException
  {
    return upload(payload, null);
  }
}
