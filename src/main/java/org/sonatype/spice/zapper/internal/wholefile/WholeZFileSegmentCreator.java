package org.sonatype.spice.zapper.internal.wholefile;

import org.sonatype.spice.zapper.internal.ZFileSegmentCreator;

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
