package eu.flatwhite.zapper;

public interface Range
{
    long getOffset();

    long getLength();

    boolean matches( Range range );

    boolean contains( Range range );

    boolean overlaps( Range range );
}
