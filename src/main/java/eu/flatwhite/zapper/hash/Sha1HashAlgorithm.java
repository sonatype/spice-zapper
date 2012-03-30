package eu.flatwhite.zapper.hash;

import java.security.NoSuchAlgorithmException;

import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.internal.StringIdentifier;

public class Sha1HashAlgorithm
    extends AbstractMessageDigestHashAlgorithm
    implements HashAlgorithm
{
    public static final Identifier ID = new StringIdentifier( "SHA-1" );

    public Sha1HashAlgorithm()
        throws NoSuchAlgorithmException
    {
        super( ID );
    }

    @Override
    public int getHashSize()
    {
        return 20;
    }
}
