package org.sonatype.spice.zapper.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.HashAlgorithm;

/**
 * Message payload holds small array of bytes, that will be sent over the wire. It is unaware of the content.
 * 
 * @author cstamas
 */
public class MessagePayload
    implements Payload
{
    private final TransferIdentifier transferIdentifier;

    private final Path path;

    private final byte[] message;

    private final Hash hash;

    public MessagePayload( final TransferIdentifier transferIdentifier, final Path path, final byte[] message,
                           final HashAlgorithm hashAlgorithm )
    {
        this.transferIdentifier = transferIdentifier;
        this.path = path;
        this.message = Arrays.copyOf( message, message.length );
        this.hash = hashAlgorithm.hash( message );
    }

    @Override
    public TransferIdentifier getTransferIdentifier()
    {
        return transferIdentifier;
    }

    @Override
    public Path getPath()
    {
        return path;
    }

    @Override
    public long getLength()
    {
        return message.length;
    }

    @Override
    public InputStream getContent()
        throws IOException
    {
        return new ByteArrayInputStream( message );
    }

    @Override
    public Hash getHash()
    {
        return hash;
    }

    // ==

    public byte[] getMessage()
    {
        return message;
    }

}
