package org.sonatype.spice.zapper.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.Hashed;

/**
 * Payload is one "nurb" of content that is sent over the wire.
 *
 * @author cstamas
 */
public interface Payload
    extends Hashed
{
  TransferIdentifier getTransferIdentifier();

  Path getPath();

  long getLength();

  List<Codec> getCodecs();

  InputStream getContent()
      throws IOException;
}
