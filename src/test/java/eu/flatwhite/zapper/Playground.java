package eu.flatwhite.zapper;

import java.io.File;
import java.util.List;

import org.junit.Test;

import eu.flatwhite.zapper.fs.DirectoryIOSource;
import eu.flatwhite.zapper.hash.Sha1HashAlgorithm;
import eu.flatwhite.zapper.internal.PathImpl;
import eu.flatwhite.zapper.internal.RangeImpl;
import eu.flatwhite.zapper.internal.StringIdentifier;

public class Playground
{
    @Test
    public void simple()
        throws Exception
    {
        final IOSourceListable directory =
            new DirectoryIOSource( new File( "/Users/cstamas/Worx/flatwhite/zapper/target/classes" ),
                new Sha1HashAlgorithm() );

        List<ZFile> zfiles = directory.listFiles();

        for ( ZFile zfile : zfiles )
        {
            System.out.println( zfile );

        }
        /*
         * final ZFile zfile = new FileZFile( new File(
         * "/Users/cstamas/Worx/flatwhite/zapper/target/zapper-1.0.0-SNAPSHOT.jar" ), new PathImpl( "/foo/bar" ) );
         * System.out.println( zfile.getHash( Sha1HashIdentifier.ID ) ); System.out.println( zfile.getIdentifier() );
         * System.out.println( zfile.getOffset() ); System.out.println( zfile.getLength() ); System.out.println(
         * zfile.getPath() ); System.out.println( zfile.getLastModifiedTimestamp() ); final Range firstFiveBytes = new
         * RangeImpl( new StringIdentifier( "firstFiveBytes" ), 0l, 32l ); ZFileSegment seg1 = zfile.getSegment(
         * firstFiveBytes ); System.out.println( seg1.getHash( Sha1HashIdentifier.ID ) ); System.out.println(
         * seg1.getIdentifier() ); System.out.println( seg1.getOffset() ); System.out.println( seg1.getLength() );
         * DigesterUtils.dump( System.out, seg1.getSegmentContent() );
         */
    }

}
