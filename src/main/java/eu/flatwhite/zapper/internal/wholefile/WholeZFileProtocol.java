package eu.flatwhite.zapper.internal.wholefile;

import eu.flatwhite.zapper.internal.AbstractIdentified;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.PayloadCreator;
import eu.flatwhite.zapper.internal.Protocol;
import eu.flatwhite.zapper.internal.ProtocolIdentifier;
import eu.flatwhite.zapper.internal.SegmentCreator;
import eu.flatwhite.zapper.internal.TransferIdentifier;

/**
 * Whole file protocol does not "cut" (segment) uploaded files, but instead sends them as whole. Usable when there is no
 * Zapper-aware client side, as this works with all protocols out of the box (ie. this is actually PUTs in HTTP or
 * separate file uploads in FTP world without any action needed on receiver end, with exception of the existence of
 * "usual" server, HTTP or FTP).
 * 
 * @author cstamas
 */
public class WholeZFileProtocol
    extends AbstractIdentified<ProtocolIdentifier>
    implements Protocol
{
    public static ProtocolIdentifier ID = new ProtocolIdentifier( "whole-zfile" );

    private final TransferIdentifier transferId;

    public WholeZFileProtocol( final TransferIdentifier transferId )
    {
        super( ID );
        this.transferId = Check.notNull( transferId, TransferIdentifier.class );
    }

    public TransferIdentifier getTransferId()
    {
        return transferId;
    }

    @Override
    public SegmentCreator getSegmentCreator()
    {
        return new WholeZFileSegmentCreator();
    }

    @Override
    public PayloadCreator getPayloadCreator()
    {
        return new WholeZFilePayloadCreator();
    }
}
