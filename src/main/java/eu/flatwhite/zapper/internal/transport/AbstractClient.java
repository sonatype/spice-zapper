package eu.flatwhite.zapper.internal.transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.flatwhite.zapper.Client;
import eu.flatwhite.zapper.IOSource;
import eu.flatwhite.zapper.IOSourceListable;
import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.Path;
import eu.flatwhite.zapper.ZFile;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.Payload;
import eu.flatwhite.zapper.internal.PayloadSupplier;
import eu.flatwhite.zapper.internal.PayloadSupplierImpl;
import eu.flatwhite.zapper.internal.Protocol;
import eu.flatwhite.zapper.internal.Segment;
import eu.flatwhite.zapper.internal.TransferIdentifier;
import eu.flatwhite.zapper.internal.wholefile.WholeZFileProtocol;

public abstract class AbstractClient
    implements Client
{
    private final Logger logger;

    private final Parameters parameters;

    private final String remoteUrl;

    public AbstractClient( final Parameters parameters, final String remoteUrl )
    {
        this.logger = LoggerFactory.getLogger( getClass() );
        this.parameters = Check.notNull( parameters, Parameters.class );
        this.remoteUrl = Check.notNull( remoteUrl, "Remote URL is null!" );
    }

    public String getRemoteUrl()
    {
        return remoteUrl;
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
        final TransferIdentifier transferId = new TransferIdentifier( UUID.randomUUID().toString() );
        final Protocol protocol = handshake( transferId );
        getLogger().info( "Starting upload transfer ID \"{}\" (using protocol \"{}\")", transferId.stringValue(),
            protocol.getIdentifier().stringValue() );

        final List<Segment> segments = protocol.getSegmentCreator().createSegments( transferId, zfiles );
        final int trackCount = Math.min( getParameters().getMaximumTrackCount(), segments.size() );

        final List<Payload> payloads =
            protocol.getPayloadCreator().createPayloads( transferId, source, segments, getRemoteUrl() );
        final PayloadSupplier payloadSupplier = new PayloadSupplierImpl( payloads );

        long totalSize = 0;
        for ( ZFile zfile : zfiles )
        {
            totalSize += zfile.getLength();
        }

        getLogger().info( "Uploading total of {} bytes (in {} files) as {} segments over {} tracks.",
            new Object[] { totalSize, zfiles.size(), segments.size(), trackCount } );

        final long started = System.currentTimeMillis();
        boolean success = false;
        try
        {
            doUpload( protocol, trackCount, payloadSupplier );
            success = true;
        }
        finally
        {
            source.close( success );
        }

        getLogger().info( "Upload finished in {} seconds.", ( System.currentTimeMillis() - started ) / 1000 );
    }

    // ==

    protected Logger getLogger()
    {
        return logger;
    }

    protected Parameters getParameters()
    {
        return parameters;
    }

    // ==

    protected Protocol handshake( final TransferIdentifier transferIdentifier )
    {
        // safest, we will see later for real handshake
        return new WholeZFileProtocol( transferIdentifier );
    }

    /**
     * Performs actual operation. Either returns cleanly (which is considered as "success"), or should throw
     * {@link IOException} to mark "failure".
     * 
     * @param protocol
     * @param trackCount
     * @param payloadSupplier
     * @throws IOException
     */
    protected abstract void doUpload( final Protocol protocol, final int trackCount,
                                      final PayloadSupplier payloadSupplier )
        throws IOException;
}
