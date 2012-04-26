package org.sonatype.spice.zapper.hash;

import org.sonatype.spice.zapper.Identifier;

public interface HashAlgorithmIdentifier
    extends Identifier
{
    int getHashSize();
}
