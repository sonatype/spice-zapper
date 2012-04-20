package eu.flatwhite.zapper.internal.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.IOSourceListable;
import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.client.Client;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.Protocol;
import eu.flatwhite.zapper.internal.protocol.WholeZFileProtocol;

public abstract class AbstractClient
    implements Client
{
    private final Parameters parameters;

    private final Protocol protocol;

    public AbstractClient( final Parameters parameters )
    {
        this.parameters = Check.notNull( parameters, Parameters.class );
        this.protocol = handshake();
    }

    @Override
    public void upload( IOSourceListable listableSource )
        throws IOException
    {
        upload( listableSource, listableSource.listFiles() );
    }

    @Override
    public void upload( IOSource source, Path... paths )
        throws IOException
    {
        final ArrayList<ZFile> zfiles = new ArrayList<ZFile>();
        for ( Path path : paths )
        {
            zfiles.add( source.createZFile( path ) );
        }
        upload( source, zfiles );
    }

    @Override
    public void download( IOTarget target, Path... paths )
        throws IOException
    {
        throw new UnsupportedOperationException( "Not implemented!" );
    }

    // ==

    protected Parameters getParameters()
    {
        return parameters;
    }

    protected Protocol getProtocol()
    {
        return protocol;
    }

    // ==

    protected Protocol handshake()
    {
        // safest, we will see later for real handshake
        return new WholeZFileProtocol();
    }

    protected abstract void upload( final IOSource source, final List<ZFile> zfiles )
        throws IOException;
}
