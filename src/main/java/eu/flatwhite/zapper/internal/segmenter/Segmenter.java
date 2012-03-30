package eu.flatwhite.zapper.internal.segmenter;

import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.ZFile;

public interface Segmenter
{
    Recipe segment( Parameters params, ZFile... files );
}
