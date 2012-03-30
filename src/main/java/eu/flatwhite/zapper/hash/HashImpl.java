package eu.flatwhite.zapper.hash;

import java.util.Arrays;

import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.DigesterUtils;
import eu.flatwhite.zapper.internal.StringIdentifier;

public class HashImpl
    extends StringIdentifier
    implements Hash
{
    private final HashAlgorithm algorithm;

    private final byte[] hash;

    public HashImpl( final HashAlgorithm algorithm, final byte[] hash )
    {
        super( DigesterUtils.getDigestAsString( Check.notNull( hash, "Hash byte array is null!" ) ) );

        // we know hash array is not null here
        this.algorithm = Check.notNull( algorithm, "Algorithm is null!" );
        this.hash =
            Check.argument( hash.length == algorithm.getHashSize(), hash, "Hash array contains wrong count of bytes!" );
    }

    @Override
    public HashAlgorithm getAlgorithm()
    {
        return algorithm;
    }

    @Override
    public byte[] getHash()
    {
        return Arrays.copyOf( hash, hash.length );
    }

    // ==

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( algorithm == null ) ? 0 : algorithm.hashCode() );
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
        HashImpl other = (HashImpl) obj;
        if ( !getAlgorithm().getIdentifier().stringValue().equals( other.getAlgorithm().getIdentifier().stringValue() ) )
            return false;
        if ( !Arrays.equals( getHash(), other.getHash() ) )
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "HashImpl [algorithm=" + algorithm + ", hash=" + stringValue() + "]";
    }
}
