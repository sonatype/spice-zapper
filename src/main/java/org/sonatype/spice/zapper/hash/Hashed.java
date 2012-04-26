package org.sonatype.spice.zapper.hash;

public interface Hashed
{
    Hash getHash( HashAlgorithmIdentifier hashAlgorithmIdentifier );
}
