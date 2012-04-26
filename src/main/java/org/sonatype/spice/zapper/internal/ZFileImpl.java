package org.sonatype.spice.zapper.internal;

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.ZFile;
import org.sonatype.spice.zapper.hash.Hash;

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
