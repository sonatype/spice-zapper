package eu.flatwhite.zapper;

public interface Range
    extends Identified
{
    long getOffset();

    long getLength();

    boolean matches( Range range );

    boolean contains( Range range );
}
