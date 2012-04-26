package org.sonatype.spice.zapper.hash;

import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.StringIdentifier;

public class HashAlgorithmIdentifierImpl
    extends StringIdentifier
    implements HashAlgorithmIdentifier
{
    private final int hashSizeInBytes;

    public HashAlgorithmIdentifierImpl( final String stringValue, final int hashSizeInBytes )
    {
        super( stringValue );
        this.hashSizeInBytes = Check.argument( hashSizeInBytes > 0, hashSizeInBytes, "Hash size not positive!" );
    }

    @Override
    public int getHashSize()
    {
        return hashSizeInBytes;
    }
}
