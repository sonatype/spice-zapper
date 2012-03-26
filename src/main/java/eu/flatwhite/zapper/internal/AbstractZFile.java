package eu.flatwhite.zapper.internal;

import java.util.Map;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import eu.flatwhite.zapper.Hash;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;

public abstract class AbstractZFile
    extends AbstractRange
    implements ZFile
{
    private final long lastModified;

    private volatile Map<Identifier, Hash> hashes;

    protected AbstractZFile( final Path path, final long offset, final long length, final long lastModified )
    {
        super( path, offset, length );
        this.lastModified = lastModified;
        this.hashes = new ConcurrentHashMap<Identifier, Hash>();
    }

    @Override
    public synchronized Hash getHash( final Identifier alg )
    {
        if ( alg == null )
        {
            throw new NullPointerException( "Hash alg identifier is null!" );
        }

        if ( !hashes.containsKey( alg ) )
        {
            hashes.put( alg, calculateHash( alg ) );
        }

        return hashes.get( alg );
    }

    @Override
    public Path getPath()
    {
        return (Path) getIdentifier();
    }

    @Override
    public long getLastModifiedTimestamp()
    {
        return lastModified;
    }

    // ==

    protected abstract Hash calculateHash( final Identifier alg );
}
