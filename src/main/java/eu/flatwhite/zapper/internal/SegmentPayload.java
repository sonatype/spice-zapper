package eu.flatwhite.zapper.internal;

import java.io.IOException;
import java.io.InputStream;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.codec.Codec;

public class SegmentPayload
    implements Payload
{
    private final Path path;

    private final Segment segment;

    private final IOSource ioSource;

    public SegmentPayload( final Path path, final Segment segment, final IOSource ioSource )
    {
        this.path = path;
        this.segment = segment;
        this.ioSource = ioSource;
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
