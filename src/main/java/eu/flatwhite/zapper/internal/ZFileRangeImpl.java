package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Range;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.ZFileRange;

public class ZFileRangeImpl
    extends AbstractRange
    implements ZFileRange
{
    private final ZFile zfile;

    public ZFileRangeImpl( final ZFile zfile, final Range range )
    {
        super( range );
        this.zfile = zfile;
    }

    @Override
    public ZFile getZFile()
    {
        return zfile;
    }
}
