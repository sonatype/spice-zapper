package eu.flatwhite.zapper.client.ahc;

import java.io.IOException;

public class AggregatingIOException
    extends IOException
{
    private static final long serialVersionUID = 4686084672180030357L;

    private final IOException[] trackExceptions;

    public AggregatingIOException( final String message, final IOException[] trackExceptions )
    {
        super( message );
        this.trackExceptions = trackExceptions;
    }

    protected IOException[] getTrackExceptions()
    {
        return trackExceptions;
    }
}
