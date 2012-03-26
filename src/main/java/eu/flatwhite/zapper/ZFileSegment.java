package eu.flatwhite.zapper;

import java.io.InputStream;

public interface ZFileSegment
    extends Range, Hashed 
{
    ZFile getFile();

    InputStream getInputStream();
}
