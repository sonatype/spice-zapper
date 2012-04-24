package eu.flatwhite.zapper;

import java.io.IOException;

/**
 * IOException that carries the statuses/exceptions per each track.
 * 
 * @author cstamas
 */
public class AggregatingIOException
    extends IOException
{
    private static final long serialVersionUID = 4686084672180030357L;

    private final IOException[] trackExceptions;

    public AggregatingIOException( final String message, final IOException[] trackExceptions )
    {
        super( buildMessage( message, trackExceptions ) );
        this.trackExceptions = trackExceptions;
    }

    /**
     * Returns exceptions thrown by tracks. The returned array might have {@code null} elements if track did not hit any
     * exception!
     * 
     * @return
     */
    protected IOException[] getTrackExceptions()
    {
        return trackExceptions;
    }

    static String buildMessage( final String message, final IOException[] trackExceptions )
    {
        final StringBuilder sb = new StringBuilder( message ).append( "\n" );

        int trackNo = 1;
        for ( IOException e : trackExceptions )
        {
            sb.append( " " ).append( trackNo++ ).append( ". " );
            if ( e == null )
            {
                sb.append( "Finished OK" );
            }
            else
            {
                sb.append( e.getMessage() ).append( "\n" );
            }
        }

        return sb.toString();
    }
}
