package org.sonatype.spice.zapper.internal.zapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fusesource.hawtbuf.Buffer;
import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.HashAlgorithm;
import org.sonatype.spice.zapper.hash.HashUtils;
import org.sonatype.spice.zapper.internal.Segment;
import org.sonatype.spice.zapper.internal.SegmentPayload;
import org.sonatype.spice.zapper.internal.TransferIdentifier;

import org.sonatype.spice.zapper.internal.hawtbuf.SegmentFooter;
import org.sonatype.spice.zapper.internal.hawtbuf.SegmentHeader;

public class ZapperPayload
    extends SegmentPayload
{
    private final byte[] header;

    private final byte[] footer;

    private final Hash envelopeHash;

    public ZapperPayload( final TransferIdentifier transferIdentifier, final Path path, final Segment segment,
                          final IOSource ioSource, final HashAlgorithm hashAlgorithm, final Hash segmentHash,
                          final List<Codec> codecs )
        throws IOException
    {
        super( transferIdentifier, path, segment, ioSource, segmentHash, codecs );
        this.header = createSegmentHeader();
        this.footer = createSegmentFooter();
        this.envelopeHash = HashUtils.getDigest( hashAlgorithm, getContent() );
    }

    @Override
    public long getLength()
    {
        return header.length + super.getLength() + footer.length;
    }

    @Override
    public Hash getHash()
    {
        return envelopeHash;
    }

    @Override
    public InputStream getContent()
        throws IOException
    {
        final ArrayList<InputStream> parts = new ArrayList<InputStream>( 3 );
        parts.add( new ByteArrayInputStream( header ) ); // segment header
        parts.add( super.getContent() ); // the segment body
        parts.add( new ByteArrayInputStream( footer ) ); // segment footer

        return new SequenceInputStream( Collections.enumeration( parts ) );
    }

    // ==

    protected byte[] createSegmentHeader()
    {
        SegmentHeader header = new SegmentHeader()
            .setFileId(getSegment().getZFile().getIdentifier().stringValue())
            .setSegmentId(getIdentifier().stringValue())
            .setSegmentOffset(getSegment().getOffset())
            .setSegmentLength(getSegment().getLength());

        return header.toFramedByteArray();
    }

    protected byte[] createSegmentFooter()
    {
        SegmentFooter footer = new SegmentFooter();

        final Hash bodyHash = super.getHash();

        footer.addHashes(new org.sonatype.spice.zapper.internal.hawtbuf.Hash()
            .setHashAlg(bodyHash.getHashAlgorithmIdentifier().stringValue())
            .setHashBytes(new Buffer(bodyHash.byteValue())));

        return footer.toFramedByteArray();
    }
}
