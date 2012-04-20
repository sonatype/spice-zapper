package eu.flatwhite.zapper.internal.segmenter;

public class WholeZFileSegmentCreator
    extends ZFileSegmentCreator
{
    public WholeZFileSegmentCreator()
    {
        super( Long.MAX_VALUE );
    }
}
