package eu.flatwhite.zapper.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Range;
import eu.flatwhite.zapper.ZFile;

public class DirectoryIO
    extends FileIO
    implements IOSource, IOTarget
{
    private final File root;

    private final boolean sandbox;

    public DirectoryIO( final File root )
        throws IOException
    {
        this( root, true );
    }

    public DirectoryIO( final File root, final boolean sandbox )
        throws IOException
    {
        if ( root.isDirectory() )
        {
            this.root = root.getAbsoluteFile();
        }
        else
        {
            throw new IOException( String.format( "Supplied file %s is a not an existing directory!",
                root.getAbsolutePath() ) );
        }
        this.sandbox = sandbox;
    }

    public File getRoot()
    {
        return root;
    }

    public boolean isSandbox()
    {
        return sandbox;
    }

    @Override
    public Collection<ZFile> enumerate()
    {
        throw new UnsupportedOperationException( "Not implemented" );
    }

    @Override
    public long freeSpace()
    {
        return root.getFreeSpace();
    }

    @Override
    public InputStream readSegment( final Identifier identifier )
        throws IOException
    {
        throw new UnsupportedOperationException( "Not implemented" );
    }

    @Override
    public void writeSegment( final Range range, final InputStream inputStream )
        throws IOException
    {
        throw new UnsupportedOperationException( "Not implemented" );
    }

    // ==

    @Override
    protected File getFile( ZFile zfile )
        throws IOException
    {
        final File result = new File( getRoot(), zfile.getPath().stringValue() ).getCanonicalFile();

        if ( isSandbox() )
        {
            if ( !result.getCanonicalPath().startsWith( root.getCanonicalPath() ) )
            {
                throw new IOException( String.format( "ZFile %s was about to escape sandbox %s!", zfile,
                    root.getAbsolutePath() ) );
            }
        }

        return result;
    }

}
