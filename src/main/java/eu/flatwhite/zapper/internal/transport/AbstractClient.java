package eu.flatwhite.zapper.internal.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eu.flatwhite.zapper.Client;
import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.IOSourceListable;
import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Identifier;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.PayloadSupplierImpl;
import eu.flatwhite.zapper.internal.Protocol;
import eu.flatwhite.zapper.internal.Segment;
import eu.flatwhite.zapper.internal.StringIdentifier;
import eu.flatwhite.zapper.internal.wholefile.WholeZFileProtocol;

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

    protected void upload( final IOSource source, final List<ZFile> zfiles )
        throws IOException
    {
        final Identifier transferId = new StringIdentifier( UUID.randomUUID().toString() );
        final Protocol protocol = getProtocol();
        final List<Segment> segments = protocol.getSegmentCreator( transferId ).createSegments( zfiles );
        final List<Payload> payloads = protocol.getPayloadCreator( transferId ).createPayloads( source, segments );
        final PayloadSupplier payloadSupplier = new PayloadSupplierImpl( payloads );
        final int trackCount = Math.min( getParameters().getMaximumSessionCount(), zfiles.size() );
        doUpload( protocol, trackCount, payloadSupplier );
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

    protected abstract void doUpload( final Protocol protocol, final int trackCount,
                                      final PayloadSupplier payloadSupplier )
        throws IOException;
}
