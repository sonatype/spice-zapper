package eu.flatwhite.zapper.internal;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * A utility class to calculate various digests on Strings. Useful for some simple content checks.
 * 
 * @author cstamas
 */
public class DigesterUtils
{
    public static final String ALG_SHA256 = "SHA-256";

    public static final String ALG_SHA1 = "SHA-1";

    public static final String ALG_MD5 = "MD5";

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
//                    ps.print( " :" + line + ": " );
//                    ps.print( new String( Arrays.copyOf( buffer, numRead ) ) );
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
     * Hex Encodes the digest value.
     * 
     * @param digest
     * @return the digest as string in hex encoded form
     */
    public static String getDigestAsString( final byte[] digest )
    {
        return new String( encodeHex( digest ) );
    }

    /**
     * Hex decodes the digest string.
     * 
     * @param s
     * @return
     */
    public static byte[] getStringDigestAsByteArray( final String s )
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for ( int i = 0; i < len; i += 2 )
        {
            data[i / 2] =
                (byte) ( ( Character.digit( s.charAt( i ), 16 ) << 4 ) + Character.digit( s.charAt( i + 1 ), 16 ) );
        }
        return data;
    }

    // Streams

    /**
     * Calculates a digest for the passed in InputStream. The passed in InputStream is consumed and is closed when
     * method returns.
     * 
     * @param alg
     * @param is
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getDigest( final String alg, final InputStream is )
        throws NoSuchAlgorithmException, IOException
    {
        try
        {
            byte[] buffer = new byte[1024];
            MessageDigest md = MessageDigest.getInstance( alg );
            int numRead;

            do
            {
                numRead = is.read( buffer );
                if ( numRead > 0 )
                {
                    md.update( buffer, 0, numRead );
                }
            }
            while ( numRead != -1 );

            return md.digest();
        }
        finally
        {
            close( is );
        }
    }

    /**
     * Calculates a digest for the passed in InputStream. The passed in InputStream is consumed and is closed when
     * method returns.
     * 
     * @param alg
     * @param is
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getDigestAsString( final String alg, final InputStream is )
        throws NoSuchAlgorithmException, IOException
    {
        return getDigestAsString( getDigest( alg, is ) );
    }

    // SHA1
    /**
     * Calculates a SHA1 digest for the passed in byte array.
     * 
     * @param content the byte array you want SHA1 checksum.
     * @return
     */
    public static byte[] getSha1Digest( final byte[] content )
        throws IOException
    {
        try
        {
            InputStream is = new ByteArrayInputStream( content );

            return getDigest( ALG_SHA1, is );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // will not happen
            return null;
        }
    }

    /**
     * Calculates a SHA1 digest for the passed in string.
     * 
     * @param content the string you want SHA1 checksum.
     * @return
     */
    public static byte[] getSha1Digest( final String content )
        throws IOException
    {
        try
        {
            InputStream is = new ByteArrayInputStream( content.getBytes( CHARSET_UTF8 ) );

            return getDigest( ALG_SHA1, is );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // will not happen
            return null;
        }
    }

    /**
     * Calculates a SHA1 digest for a stream. The stream is consumed and is closed when this method returns.
     * 
     * @param is
     * @return
     */
    public static byte[] getSha1Digest( final InputStream is )
        throws IOException
    {
        try
        {
            return getDigest( ALG_SHA1, is );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // will not happen
            return null;
        }
    }

    /**
     * Calculates a SHA1 digest for a file.
     * 
     * @param file
     * @return
     */
    public static byte[] getSha1Digest( File file )
        throws IOException
    {
        FileInputStream fis = null;

        try
        {
            fis = new FileInputStream( file );

            return getDigest( ALG_SHA1, fis );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // will not happen
            return null;
        }
        catch ( FileNotFoundException e )
        {
            // will not happen
            return null;
        }
        finally
        {
            close( fis );
        }
    }

    // MD5

    /**
     * Calculates a MD5 digest for a string.
     * 
     * @param content
     * @return
     */
    public static byte[] getMd5Digest( String content )
        throws IOException
    {
        try
        {
            InputStream fis = new ByteArrayInputStream( content.getBytes( CHARSET_UTF8 ) );

            return getDigest( ALG_MD5, fis );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // will not happen
            return null;
        }
    }

    /**
     * Calculates a MD5 digest for a stream. The stream is consumed and is closed when method returns.
     * 
     * @param is
     * @return
     */
    public static byte[] getMd5Digest( InputStream is )
        throws IOException
    {
        try
        {
            return getDigest( ALG_MD5, is );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // will not happen
            return null;
        }
    }

    /**
     * Calculates a MD5 digest for a file.
     * 
     * @param file
     * @return
     */
    public static byte[] getMd5Digest( File file )
        throws IOException
    {
        FileInputStream fis = null;

        try
        {
            fis = new FileInputStream( file );

            return getDigest( ALG_MD5, fis );
        }
        catch ( NoSuchAlgorithmException e )
        {
            // will not happen
            return null;
        }
        catch ( FileNotFoundException e )
        {
            // will not happen
            return null;
        }
        finally
        {
            close( fis );
        }
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
    public static char[] encodeHex( byte[] data )
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
