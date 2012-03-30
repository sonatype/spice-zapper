package eu.flatwhite.zapper.hash;

import java.io.InputStream;
import java.io.OutputStream;

import eu.flatwhite.zapper.Identified;

public interface HashAlgorithm
    extends Identified
{
    int getHashSize();

    Hash hash( byte[] buffer );

    HashingInputStream hashInput( InputStream input );

    HashingOutputStream hashOutput( OutputStream output );
}
