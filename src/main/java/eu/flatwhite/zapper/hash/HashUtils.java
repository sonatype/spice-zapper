package eu.flatwhite.zapper.hash;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import eu.flatwhite.zapper.internal.Check;

/**
 * A utility class to calculate various digests on Strings. Useful for some simple content checks.
 * 
 * @author cstamas
 */
public class HashUtils
{
    public static final String CHARSET_UTF8_STRING = "UTF-8";

    public static final Charset CHARSET_UTF8 = Charset.forName( CHARSET_UTF8_STRING );

    /**
     * Handy method for UTs, to make a hex dump.
     * 
     * @param ps
     * @param content
     */
    public static void dump( final PrintStream ps, final InputStream content )
        throws IOException
    {
        try
        {
            byte[] buffer = new byte[16];
            int numRead;
            int line = 0;
            do
            {
                numRead = content.read( buffer );
                if ( numRead > 0 )
                {
                    ps.print( line + ": " );
                    ps.print( encodeHex( Arrays.copyOf( buffer, numRead ) ) );
                    // ps.print( " :" + line + ": " );
                    // ps.print( new String( Arrays.copyOf( buffer, numRead ) ) );
                    ps.println( " :" + line );
                    line++;
                }
            }
            while ( numRead != -1 );
        }
        finally
        {
            close( content );
        }
    }

    /**
     * Hex-encodes the byte buffer.
     * 
     * @param buffer the byte array to encode.
     * @return the encoded buffer as string in hex encoded form.
     */
    public static String encodeHexString( final byte[] buffer )
    {
        Check.notNull( buffer, "The buffer is null!" );
        return new String( encodeHex( buffer ) );
    }

    /**
     * Hex-decodes the string.
     * 
     * @param string the hex encoded string to decode.
     * @return the decoded buffer as byte array.
     */
    public static byte[] decodeHexString( final String string )
    {
        Check.notNull( string, "The string is null!" );
        Check.argument( string.length() % 2 == 0, "Encoded string must have even length!" );
        int len = string.length();
        byte[] data = new byte[len / 2];
        for ( int i = 0; i < len; i += 2 )
        {
            data[i / 2] =
                (byte) ( ( Character.digit( string.charAt( i ), 16 ) << 4 ) + Character.digit( string.charAt( i + 1 ),
                    16 ) );
        }
        return data;
    }

    // Streams

    /**
     * Calculates a digest for the passed in InputStream. The passed in InputStream is completely consumed and is closed
     * when method returns.
     * 
     * @param alg the algorithm to apply.
     * @param is the input stream for which we need hash.
     * @return the hash.
     * @throws IOException
     */
    public static Hash getDigest( final HashAlgorithm alg, final InputStream is )
        throws IOException
    {
        try
        {
            byte[] buffer = new byte[1024];
            HashingInputStream his = alg.hashInput( is );
            int numRead;
            do
            {
                numRead = his.read( buffer );
            }
            while ( numRead != -1 );
            return his.getHash();
        }
        finally
        {
            close( is );
        }
    }

    public static Hash getDigest( final HashAlgorithm alg, final File file )
        throws IOException
    {
        // the method above consumes and closes the stream
        return getDigest( alg, new FileInputStream( file ) );
    }

    public static Hash getDigest( final HashAlgorithm alg, final byte[] buffer )
    {
        try
        {
            return getDigest( alg, new ByteArrayInputStream( buffer ) );
        }
        catch ( IOException e )
        {
            // this will not happen, is only declared at InputStream API but we create an in-memory byte array backed
            throw new IllegalStateException( "Huh?", e );
        }
    }

    public static Hash getDigest( final HashAlgorithm alg, final String content )
    {
        return getDigest( alg, content.getBytes( CHARSET_UTF8 ) );
    }

    // ==

    /**
     * This method actually duplicates the IOUtils.close() method, but is here only to make this class
     * "self sufficient".
     * 
     * @param inputStream
     */
    private static void close( final Closeable closeable )
    {
        if ( closeable == null )
        {
            return;
        }

        try
        {
            closeable.close();
        }
        catch ( IOException ex )
        {
            // ignore
        }
    }

    // --

    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
        'f' };

    /**
     * Blatantly copied from commons-codec version 1.3
     * 
     * @param data
     * @return
     */
    private static char[] encodeHex( byte[] data )
    {
        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for ( int i = 0, j = 0; i < l; i++ )
        {
            out[j++] = DIGITS[( 0xF0 & data[i] ) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }

}
