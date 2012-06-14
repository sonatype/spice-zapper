package org.sonatype.spice.zapper.internal;

public interface PayloadSupplier
{
    SegmentPayload getNextPayload();
}
