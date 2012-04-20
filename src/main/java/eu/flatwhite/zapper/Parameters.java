package eu.flatwhite.zapper;

import java.util.Map;

import eu.flatwhite.zapper.codec.Codec;
import eu.flatwhite.zapper.codec.CodecIdentifier;
import eu.flatwhite.zapper.hash.HashAlgorithm;
import eu.flatwhite.zapper.hash.HashAlgorithmIdentifier;

public interface Parameters
{
    /**
     * Map of available hash algorithms.
     * 
     * @return
     */
    Map<HashAlgorithmIdentifier, HashAlgorithm> getHashAlgorithms();

    /**
     * Map of available codecs.
     * 
     * @return
     */
    Map<CodecIdentifier, Codec> getCodecs();

    /**
     * How many parallel sessions ("connections") might exist during uploads/downloads. This is a hard maximum, that
     * does not have to be reached, but will never be more than this value. Naturally, having value of 1 would mean
     * "sequential upload" in this sense, and is a valid value.
     * 
     * @return
     */
    int getMaximumSessionCount();

    /**
     * Gets the maximum segment length.
     * 
     * @return
     */
    long getMaximumSegmentLength();
}
