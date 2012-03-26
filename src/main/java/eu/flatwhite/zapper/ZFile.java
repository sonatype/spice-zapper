package eu.flatwhite.zapper;

public interface ZFile
    extends Range, Hashed
{
    Path getPath();

    long getLastModifiedTimestamp();

    ZFileSegment getSegment( Range range );
}
