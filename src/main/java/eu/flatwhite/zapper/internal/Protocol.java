package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identifier;

/**
 * Whole file -- most compatible Ranges -- segmented uploads by ranges Zapper -- segmented uploads
 * 
 * @author cstamas
 */
public interface Protocol
{
    SegmentCreator getSegmentCreator( Identifier transferId );

    PayloadCreator getPayloadCreator( Identifier transferId );
}
