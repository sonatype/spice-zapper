package org.sonatype.spice.zapper.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.Hash;

/**
 * Message payload holds small array of bytes, that will be sent over the wire. It is unaware of the content.
 *
 * @author cstamas
 */
public class MessagePayload
    extends AbstractPayload
    implements Payload
{
  private final byte[] message;

  public MessagePayload(final TransferIdentifier transferIdentifier, final Path path, final byte[] message,
                        final Hash hash, final List<Codec> codecs)
  {
    super(transferIdentifier, path, hash, codecs);
    this.message = Arrays.copyOf(message, message.length);
  }

  public long getLength() {
    return message.length;
  }

  public InputStream getContent()
      throws IOException
  {
    return new ByteArrayInputStream(message);
  }

  // ==

  public byte[] getMessage() {
    return message;
  }

}
