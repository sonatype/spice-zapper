package eu.flatwhite.zapper.internal.wholefile;

import eu.flatwhite.zapper.internal.ZFileSegmentCreator;

/**
 * {@link ZFileSegmentCreator} with threshold so big that it will always actually create segments that are complete
 * files.
 * 
 * @author cstamas
 */
public class WholeZFileSegmentCreator
    extends ZFileSegmentCreator
{
    public WholeZFileSegmentCreator()
    {
        super( Long.MAX_VALUE );
    }
}
