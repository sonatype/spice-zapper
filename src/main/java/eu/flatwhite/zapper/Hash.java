package eu.flatwhite.zapper;

public interface Hash
    extends Identifier
{
    Identifier getAlgorithm();

    byte[] getHash();
}
