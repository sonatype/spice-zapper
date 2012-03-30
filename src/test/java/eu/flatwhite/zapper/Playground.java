package eu.flatwhite.zapper;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import eu.flatwhite.zapper.fs.FileZFile;
import eu.flatwhite.zapper.internal.DigesterUtils;
import eu.flatwhite.zapper.internal.PathImpl;
import eu.flatwhite.zapper.internal.RangeImpl;
import eu.flatwhite.zapper.internal.Sha1HashIdentifier;
import eu.flatwhite.zapper.internal.StringIdentifier;

public class Playground
{
    @Test
    public void simple()
        throws IOException
    {
        final ZFile zfile =
            new FileZFile( new File( "/Users/cstamas/Worx/flatwhite/zapper/target/zapper-1.0.0-SNAPSHOT.jar" ),
                new PathImpl( "/foo/bar" ) );

        System.out.println( zfile.getHash( Sha1HashIdentifier.ID ) );
        System.out.println( zfile.getIdentifier() );
        System.out.println( zfile.getOffset() );
        System.out.println( zfile.getLength() );
        System.out.println( zfile.getPath() );
        System.out.println( zfile.getLastModifiedTimestamp() );

        final Range firstFiveBytes = new RangeImpl( new StringIdentifier( "firstFiveBytes" ), 0l, 32l );
        ZFileSegment seg1 = zfile.getSegment( firstFiveBytes );
        
        System.out.println( seg1.getHash( Sha1HashIdentifier.ID ) );
        System.out.println( seg1.getIdentifier() );
        System.out.println( seg1.getOffset() );
        System.out.println( seg1.getLength() );
        DigesterUtils.dump( System.out, seg1.getSegmentContent() );
    }

}
