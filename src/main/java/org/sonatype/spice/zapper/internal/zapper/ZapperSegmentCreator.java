package org.sonatype.spice.zapper.internal.zapper;

import org.sonatype.spice.zapper.internal.ZFileSegmentCreator;

public class ZapperSegmentCreator
    extends ZFileSegmentCreator
{
    public ZapperSegmentCreator( final long maxSegmentSize )
    {
        super( maxSegmentSize );
    }
}
