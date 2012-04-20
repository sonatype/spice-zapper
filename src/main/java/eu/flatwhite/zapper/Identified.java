package eu.flatwhite.zapper;

public interface Identified<I extends Identifier>
{
    I getIdentifier();
}
