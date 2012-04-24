package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identified;

/**
 * Whole file -- most compatible Ranges -- segmented uploads by ranges Zapper -- segmented uploads
 * 
 * @author cstamas
 */
public interface Protocol
    extends Identified<ProtocolIdentifier>
{
    TransferIdentifier getTransferId();

    SegmentCreator getSegmentCreator();

    PayloadCreator getPayloadCreator();
}
