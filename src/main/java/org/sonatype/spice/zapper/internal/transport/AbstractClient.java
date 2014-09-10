package org.sonatype.spice.zapper.internal.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.sonatype.spice.zapper.Client;
import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.IOSourceListable;
import org.sonatype.spice.zapper.IOTarget;
import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.ZFile;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.Payload;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.Transfer;
import org.sonatype.spice.zapper.internal.wholefile.WholeZFileProtocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractClient<T extends Track>
    implements Client
{
  private final Logger logger;

  private final Parameters parameters;

  private final String remoteUrl;

  private final Protocol protocol;

  public AbstractClient(final Parameters parameters, final String remoteUrl) {
    this.logger = LoggerFactory.getLogger(getClass());
    this.parameters = Check.notNull(parameters, Parameters.class);
    this.remoteUrl = Check.notNull(remoteUrl, "Remote URL is null!");
    this.protocol = handshake();
  }

  public String getRemoteUrl() {
    return remoteUrl;
  }

  public void upload(IOSourceListable listableSource)
      throws IOException
  {
    upload(listableSource, listableSource.listFiles());
  }

  public void upload(IOSource source, Path... paths)
      throws IOException
  {
    final ArrayList<ZFile> zfiles = new ArrayList<ZFile>();
    for (Path path : paths) {
      zfiles.add(source.createZFile(path));
    }
    upload(source, zfiles);
  }

  public void download(IOTarget target, Path... paths)
      throws IOException
  {
    throw new UnsupportedOperationException("Not implemented!");
  }

  protected void upload(final IOSource source, final List<ZFile> _zfiles)
      throws IOException
  {
    final Transfer transfer = new Transfer(UUID.randomUUID().toString(), _zfiles);
    getLogger().info("Starting upload transfer ID \"{}\" (using protocol \"{}\")",
        transfer.getIdentifier().stringValue(), protocol.getIdentifier().stringValue());

    // segment it
    final int segmentCount = protocol.getSegmentCreator().createSegments(transfer);

    // track count
    final int trackCount = Math.min(getParameters().getMaximumTrackCount(), segmentCount);

    // payload the segments
    final int payloadCount = protocol.getPayloadCreator().createPayloads(transfer, source, getRemoteUrl());

    getLogger().info(
        "Uploading total of {} bytes (in {} files) as {} segments ({} payloads) over {} tracks.",
        transfer.getTotalSize(), transfer.getZfiles().size(), segmentCount, payloadCount, trackCount);

    final long started = System.currentTimeMillis();
    boolean success = false;
    try {
      protocol.beforeUpload(transfer, this);
      doUpload(transfer, protocol, trackCount);
      protocol.afterUpload(transfer, this);
      success = true;
    }
    finally {
      source.close(success);
    }

    getLogger().info("Upload finished in {} seconds.", (System.currentTimeMillis() - started) / 1000);
  }

  // ==

  protected Logger getLogger() {
    return logger;
  }

  protected Parameters getParameters() {
    return parameters;
  }

  // ==

  protected Protocol handshake() {
    // safest, we will see later for real handshake
    return new WholeZFileProtocol(getParameters());
  }

  /**
   * Uploads a payload.
   */
  public abstract State upload(Payload payload)
      throws IOException;

  /**
   * Uploads a payload on given track.
   */
  public abstract State upload(Payload payload, T track)
      throws IOException;

  /**
   * Performs actual operation. Either returns cleanly (which is considered as "success"), or should throw
   * {@link IOException} to mark "failure".
   */
  protected abstract void doUpload(final Transfer transfer, final Protocol protocol, final int trackCount)
      throws IOException;
}
