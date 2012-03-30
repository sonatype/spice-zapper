package eu.flatwhite.zapper;

import java.util.Map;

import eu.flatwhite.zapper.codec.Codec;
import eu.flatwhite.zapper.hash.HashAlgorithm;

public interface Parameters
{
    HashAlgorithm getHashAlgorithm();
    
    Map<Identifier, Codec> getCodecs();
    
    int getMaximumTrackCount();
    
    long getMaximumSegmentLength();
}
