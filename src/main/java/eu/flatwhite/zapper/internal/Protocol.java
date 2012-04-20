package eu.flatwhite.zapper.internal;

/**
 * Whole file -- most compatible Ranges -- segmented uploads by ranges Zapper -- segmented uploads
 * 
 * @author cstamas
 */
public interface Protocol
{
    SegmentCreator getSegmentCreator();

    PayloadCreator getPayloadCreator();
}
