package org.sonatype.spice.zapper.internal;

import java.util.ArrayList;
import java.util.List;

import org.sonatype.spice.zapper.ZFile;


public class ZFileSegmentCreator
    implements SegmentCreator
{
    private final long maxSegmentSize;

    public ZFileSegmentCreator( final long maxSegmentSize )
    {
        this.maxSegmentSize = maxSegmentSize;
    }

    @Override
    public List<Segment> createSegments( final TransferIdentifier transferId, final List<ZFile> zfiles )
    {
        final ArrayList<Segment> segments = new ArrayList<Segment>( zfiles.size() );
        for ( ZFile zfile : zfiles )
        {
            if ( zfile.getLength() < maxSegmentSize )
            {
                segments.add( createSegment( 0, zfile.getLength(), zfile ) );
            }
            else
            {
                long offset = 0;
                long length = maxSegmentSize;
                while ( true )
                {
                    length = Math.min( length, zfile.getLength() - offset );
                    if ( length == 0 )
                    {
                        break;
                    }
                    segments.add( createSegment( offset, length, zfile ) );
                    offset += length;
                }
            }
        }
        return segments;
    }

    protected Segment createSegment( final long offset, final long length, final ZFile zfile )
    {
        return new Segment( offset, length, zfile );
    }
}
