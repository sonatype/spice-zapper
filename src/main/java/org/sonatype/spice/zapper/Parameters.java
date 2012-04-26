package org.sonatype.spice.zapper;

import java.util.Map;

import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.codec.CodecIdentifier;
import org.sonatype.spice.zapper.hash.HashAlgorithm;
import org.sonatype.spice.zapper.hash.HashAlgorithmIdentifier;


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
     * How many parallel tracks ("connections") might exist during uploads/downloads. This is a hard maximum, that does
     * not have to be reached, but will never be more than this value. Naturally, having value of 1 would mean
     * "sequential upload" in this sense, and is a valid value.
     * 
     * @return
     */
    int getMaximumTrackCount();

    /**
     * Gets the maximum segment length. Not all protocols uses this, for example "whole-file" does not use this value.
     * 
     * @return
     */
    long getMaximumSegmentLength();
}
