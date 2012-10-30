package org.sonatype.spice.zapper.hash;

import java.util.Arrays;

import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.StringIdentifier;

public class Hash
    extends StringIdentifier
{
    private final HashAlgorithmIdentifier algorithm;

    private final byte[] hash;

    public Hash( final HashAlgorithmIdentifier algorithm, final byte[] hash )
    {
        super( HashUtils.encodeHexString( Check.notNull( hash, "Hash byte array is null!" ) ) );

        // we know hash array is not null here
        this.algorithm = Check.notNull( algorithm, "Algorithm is null!" );
        this.hash =
            Check.argument( hash.length == algorithm.getHashSize(), hash, "Hash array contains wrong count of bytes!" );
    }

    public HashAlgorithmIdentifier getHashAlgorithmIdentifier()
    {
        return algorithm;
    }

    public byte[] byteValue()
    {
        return org.sonatype.spice.zapper.internal.Arrays.copyOf( hash, hash.length );
    }

    // ==

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + algorithm.hashCode();
        result = prime * result + Arrays.hashCode( hash );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Hash other = (Hash) obj;
        if ( !getHashAlgorithmIdentifier().equals( other.getHashAlgorithmIdentifier() ) )
            return false;
        if ( !Arrays.equals( byteValue(), other.byteValue() ) )
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Hash [algorithm=" + algorithm + ", hash=" + stringValue() + "]";
    }
}
