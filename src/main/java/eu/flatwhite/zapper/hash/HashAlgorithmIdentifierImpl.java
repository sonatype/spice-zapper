package eu.flatwhite.zapper.hash;

import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.StringIdentifier;

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
