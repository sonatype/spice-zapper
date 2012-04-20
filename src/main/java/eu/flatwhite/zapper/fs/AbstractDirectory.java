package eu.flatwhite.zapper.fs;

import java.io.File;
import java.io.IOException;

import eu.flatwhite.zapper.Path;

public class AbstractDirectory
{
    private final File root;

    public AbstractDirectory( final File root )
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
    }

    public File getRoot()
    {
        return root;
    }

    // ==

    protected File getFile( final Path path )
        throws IOException
    {
        final File result = new File( getRoot(), path.stringValue() ).getAbsoluteFile();

        if ( !result.getAbsolutePath().startsWith( root.getAbsolutePath() ) )
        {
            throw new IOException( String.format( "Path %s was about to escape sandbox %s!", path,
                root.getAbsolutePath() ) );
        }

        return result;
    }
}
