package org.sonatype.spice.zapper.internal;

import java.io.IOException;
import java.io.InputStream;

import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.HashAlgorithm;
import org.sonatype.spice.zapper.hash.HashUtils;

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

    public SegmentPayload( final TransferIdentifier transferIdentifier, final Path path, final Segment segment,
                           final IOSource ioSource, final HashAlgorithm hashAlgorithm )
        throws IOException
    {
        super( Check.notNull( segment, Segment.class ).getIdentifier() );
        this.transferIdentifier = Check.notNull( transferIdentifier, TransferIdentifier.class );
        this.path = Check.notNull( path, Path.class );
        this.segment = segment;
        this.ioSource = Check.notNull( ioSource, IOSource.class );
        this.hash = HashUtils.getDigest( hashAlgorithm, getContent() );
    }

    public Segment getSegment()
    {
        return segment;
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
        return segment.getLength();
    }

    @Override
    public InputStream getContent()
        throws IOException
    {
        return ioSource.readSegment( segment.getZFile().getIdentifier(), segment );
    }

    @Override
    public Hash getHash()
    {
        return hash;
    }

    // ==

    protected IOSource getIoSource()
    {
        return ioSource;
    }
}
