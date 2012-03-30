package eu.flatwhite.zapper;

import eu.flatwhite.zapper.hash.Hashed;

public interface ZFile
    extends Range, Hashed
{
    Path getPath();

    long getLastModifiedTimestamp();
}
