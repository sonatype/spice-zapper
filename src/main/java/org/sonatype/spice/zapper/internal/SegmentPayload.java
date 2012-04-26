package org.sonatype.spice.zapper.internal;

import java.io.IOException;
import java.io.InputStream;

import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;


public class SegmentPayload
    implements Payload
{
    private final TransferIdentifier transferIdentifier;

    private final Path path;

    private final Segment segment;

    private final IOSource ioSource;

    public SegmentPayload( final TransferIdentifier transferIdentifier, final Path path, final Segment segment,
                           final IOSource ioSource )
    {
        this.transferIdentifier = transferIdentifier;
        this.path = path;
        this.segment = segment;
        this.ioSource = ioSource;
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
        InputStream content = ioSource.readSegment( segment.getZFile().getIdentifier(), segment );
        for ( Codec codec : segment.getSegmentFilters() )
        {
            content = codec.encode( content );
        }
        return content;
    }

}
