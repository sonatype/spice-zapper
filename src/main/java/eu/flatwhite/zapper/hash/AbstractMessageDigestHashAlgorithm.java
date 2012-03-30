package eu.flatwhite.zapper.hash;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.internal.AbstractIdentified;

public abstract class AbstractMessageDigestHashAlgorithm
    extends AbstractIdentified
    implements HashAlgorithm
{
    public AbstractMessageDigestHashAlgorithm( final Identifier identifier )
        throws NoSuchAlgorithmException
    {
        super( identifier );
        MessageDigest.getInstance( getIdentifier().stringValue() );
    }

    @Override
    public Hash hash( final byte[] buffer )
    {
        return new HashImpl( this, getMessageDigest().digest( buffer ) );
    }

    @Override
    public HashingInputStream hashInput( final InputStream input )
    {
        return new HashingInputStream( new DigestInputStream( input, getMessageDigest() ) )
        {
            @Override
            public Hash getHash()
            {
                return new HashImpl( AbstractMessageDigestHashAlgorithm.this,
                    ( (DigestInputStream) in ).getMessageDigest().digest() );
            }
        };
    }

    @Override
    public HashingOutputStream hashOutput( final OutputStream output )
    {
        return new HashingOutputStream( new DigestOutputStream( output, getMessageDigest() ) )
        {
            @Override
            public Hash getHash()
            {
                return new HashImpl( AbstractMessageDigestHashAlgorithm.this,
                    ( (DigestOutputStream) out ).getMessageDigest().digest() );
            }
        };
    }

    // ==

    protected MessageDigest getMessageDigest()
    {
        try
        {
            return MessageDigest.getInstance( getIdentifier().stringValue() );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // we know this will not happen, see constructor, we tried this already once
            throw new IllegalStateException( "No suitable MessageDigest available!", e );
        }
    }
}
