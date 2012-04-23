package eu.flatwhite.zapper.internal;

import java.io.IOException;
import java.io.InputStream;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.codec.Codec;

public class SegmentPayload
    implements Payload
{
    private final Identifier transferIdentifier;

    private final String url;

    private final Segment segment;

    private final IOSource ioSource;

    public SegmentPayload( final Identifier transferIdentifier, final String url, final Segment segment,
                           final IOSource ioSource )
    {
        this.transferIdentifier = transferIdentifier;
        this.url = url;
        this.segment = segment;
        this.ioSource = ioSource;
    }

    @Override
    public Identifier getTransferIdentifier()
    {
        return transferIdentifier;
    }

    @Override
    public String getUrl()
    {
        return url;
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
