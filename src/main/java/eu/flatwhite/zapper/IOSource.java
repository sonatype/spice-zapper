package eu.flatwhite.zapper;

import java.util.Collection;

public interface IOSource
{
    Collection<ZFile> enumerate();
}
