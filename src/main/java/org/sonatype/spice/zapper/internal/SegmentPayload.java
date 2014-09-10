package org.sonatype.spice.zapper.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.Hash;

/**
 * Segment payload is a {@link Payload} holding a {@link Segment}.
 *
 * @author cstamas
 */
public class SegmentPayload
    extends AbstractIdentified<SegmentIdentifier>
    implements Payload
{
  private final TransferIdentifier transferIdentifier;

  private final Path path;

  private final Segment segment;

  private final IOSource ioSource;

  private final Hash hash;

  private final List<Codec> codecs;

  public SegmentPayload(final TransferIdentifier transferIdentifier, final Path path, final Segment segment,
                        final IOSource ioSource, final Hash hash, final List<Codec> codecs)
      throws IOException
  {
    super(Check.notNull(segment, Segment.class).getIdentifier());
    this.transferIdentifier = Check.notNull(transferIdentifier, TransferIdentifier.class);
    this.path = Check.notNull(path, Path.class);
    this.segment = segment;
    this.ioSource = Check.notNull(ioSource, IOSource.class);
    this.hash = Check.notNull(hash, Hash.class);
    final ArrayList<Codec> cds = new ArrayList<Codec>();
    if (codecs != null) {
      cds.addAll(codecs);
    }
    this.codecs = Collections.unmodifiableList(cds);
  }

  public Segment getSegment() {
    return segment;
  }

  public TransferIdentifier getTransferIdentifier() {
    return transferIdentifier;
  }

  public Path getPath() {
    return path;
  }

  public long getLength() {
    return segment.getLength();
  }

  public InputStream getContent()
      throws IOException
  {
    return ioSource.readSegment(segment.getZFile().getIdentifier(), segment);
  }

  public Hash getHash() {
    return hash;
  }

  public List<Codec> getCodecs() {
    return codecs;
  }

  // ==

  protected IOSource getIoSource() {
    return ioSource;
  }
}
