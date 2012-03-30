package eu.flatwhite.zapper.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import eu.flatwhite.zapper.IO;
import eu.flatwhite.zapper.Range;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.internal.RangeInputStream;

public class FileIO
    implements IO
{
    @Override
    public InputStream readFileSegment( ZFile zfile, Range range )
        throws IOException
    {
        if ( zfile.matches( range ) )
        {
            return new FileInputStream( getFile( zfile ) );
        }
        else if ( zfile.contains( range ) )
        {
            return new RangeInputStream( new FileInputStream( getFile( zfile ) ), range );
        }
        else
        {
            throw new IOException( "Range falls out of the supplied ZFile!" );
        }
    }

    // ==

    protected File getFile( ZFile zfile )
        throws IOException
    {
        return new File( zfile.getPath().stringValue() );
    }
}
