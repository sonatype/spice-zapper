package eu.flatwhite.zapper.internal;

import java.util.Arrays;

import eu.flatwhite.zapper.Hash;
import eu.flatwhite.zapper.Identifier;

public class HashImpl
    implements Hash
{
    private final Identifier algorithm;

    private final byte[] hash;

    public HashImpl( final Identifier algorithm, final byte[] hash )
    {
        if ( algorithm == null )
        {
            throw new NullPointerException( "Algorithm is null!" );
        }
        if ( hash == null )
        {
            throw new NullPointerException( "Hash byte array is null!" );
        }
        if ( hash.length < 1 )
        {
            throw new IllegalArgumentException( "Hash arrat is empty!" );
        }

        this.algorithm = algorithm;
        this.hash = hash;
    }

    @Override
    public Identifier getAlgorithm()
    {
        return algorithm;
    }

    @Override
    public byte[] getHash()
    {
        return Arrays.copyOf( hash, hash.length );
    }

    @Override
    public String stringValue()
    {
        return DigesterUtils.getDigestAsString( getHash() );
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
        if ( !getAlgorithm().stringValue().equals( other.getAlgorithm().stringValue() ) )
            return false;
        if ( !Arrays.equals( getHash(), other.getHash() ) )
            return false;
        return true;
    }
}
