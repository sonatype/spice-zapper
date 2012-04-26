package org.sonatype.spice.zapper;

import org.sonatype.spice.zapper.hash.Hashed;

public interface ZFile
    extends Identified<Path>, Range, Hashed
{
    long getLastModifiedTimestamp();
}
