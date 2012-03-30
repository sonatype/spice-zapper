package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.hash.Hash;

public class ZFileImpl
    extends AbstractHashedRange
    implements ZFile
{
    private final Path path;

    private final long lastModified;

    public ZFileImpl( final Identifier identifier, final Path path, final long length, final long lastModified,
                         final Hash hash )
    {
        super( identifier, 0, length, hash );
        this.path = Check.notNull( path, "Path is null!" );
        this.lastModified = lastModified;
    }

    @Override
    public Path getPath()
    {
        return path;
    }

    @Override
    public long getLastModifiedTimestamp()
    {
        return lastModified;
    }
}
