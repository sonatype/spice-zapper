package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.hash.Hash;

public class ZFileImpl
    extends AbstractHashedRange
    implements ZFile
{
    private final Path path;

    private final long lastModified;

    public ZFileImpl( final Path path, final long length, final long lastModified, final Hash... hashes )
    {
        super( 0, length, hashes );
        this.path = Check.notNull( path, "Path is null!" );
        this.lastModified = lastModified;
    }

    @Override
    public Path getIdentifier()
    {
        return path;
    }

    @Override
    public long getLastModifiedTimestamp()
    {
        return lastModified;
    }

    // ==

    @Override
    public String toString()
    {
        return super.toString() + "(path=" + getIdentifier() + ")";
    }
}
