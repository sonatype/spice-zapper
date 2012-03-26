package eu.flatwhite.zapper.storage.fs;

import java.io.File;

import eu.flatwhite.zapper.Hash;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.Range;
import eu.flatwhite.zapper.ZFileSegment;
import eu.flatwhite.zapper.internal.AbstractZFile;

public class FileZFile
    extends AbstractZFile
{
    private final File file;

    public FileZFile( final File file, final Path path )
    {
        super( path, 0, file.length(), file.lastModified() );
        this.file = file;
    }

    public File getFile()
    {
        return file;
    }

    @Override
    public ZFileSegment getSegment( final Range range )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Hash calculateHash( Identifier alg )
    {
        return null;
    }
}
