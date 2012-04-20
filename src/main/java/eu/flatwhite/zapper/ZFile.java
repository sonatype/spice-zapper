package eu.flatwhite.zapper;

import eu.flatwhite.zapper.hash.Hashed;

public interface ZFile
    extends Identified<Path>, Range, Hashed
{
    long getLastModifiedTimestamp();
}
