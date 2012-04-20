package eu.flatwhite.zapper.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Range;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.internal.Check;

public class DirectoryIOTarget
    extends AbstractDirectory
    implements IOTarget
{
    private final Map<Identifier, File> files;

    public DirectoryIOTarget( final File root )
        throws IOException
    {
        super( root );
        files = new HashMap<Identifier, File>();
    }

    @Override
    public void close( final boolean successful )
        throws IOException
    {
        if ( !successful )
        {
            for ( File file : files.values() )
            {
                file.delete();
            }
        }
    }

    @Override
    public long freeSpace()
    {
        return getRoot().getFreeSpace();
    }

    @Override
    public void initializeZFile( final ZFile zfile )
        throws IOException
    {
        final File file = getFile( zfile.getIdentifier() );
        final RandomAccessFile raf = new RandomAccessFile( file, "rw" );
        raf.setLength( zfile.getLength() );
        raf.close();
        files.put( zfile.getIdentifier(), file );
    }

    @Override
    public long writeSegment( final ZFile zfile, final Range range, final InputStream in )
        throws IOException
    {
        final File file = files.get( zfile.getIdentifier() );
        final FileRange fileRange = new FileRange( file, range );
        final long result =
            fileRange.getFileChannel().transferFrom( Channels.newChannel( in ), range.getOffset(), range.getLength() );
        fileRange.close();
        return result;
    }

    @Override
    public void finalizeZFile( ZFile zfile )
        throws IOException
    {
        final File file = files.get( zfile.getIdentifier() );
        file.setLastModified( zfile.getLastModifiedTimestamp() );
    }

    public static class FileRange
    {
        private final RandomAccessFile randomAccessFile;

        private final Range range;

        public FileRange( final File file, final Range range )
            throws IOException
        {
            this.randomAccessFile = new RandomAccessFile( file, "rw" );
            this.range = Check.notNull( range, "Range is null" );
            this.randomAccessFile.seek( range.getOffset() );
        }

        public FileChannel getFileChannel()
        {
            return randomAccessFile.getChannel();
        }

        public Range getRange()
        {
            return range;
        }

        public void close()
            throws IOException
        {
            randomAccessFile.close();
        }
    }
}