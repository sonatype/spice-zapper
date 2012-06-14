package org.sonatype.spice.zapper.internal.zapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.sonatype.spice.zapper.IOSource;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.hash.Hash;
import org.sonatype.spice.zapper.hash.HashAlgorithm;
import org.sonatype.spice.zapper.hash.HashUtils;
import org.sonatype.spice.zapper.internal.Segment;
import org.sonatype.spice.zapper.internal.SegmentPayload;
import org.sonatype.spice.zapper.internal.TransferIdentifier;
import org.sonatype.spice.zapper.internal.protobuf.protos.ZapperProtos;

import com.google.protobuf.ByteString;

public class ZapperPayload
    extends SegmentPayload
{
    private final byte[] header;

    private final byte[] footer;
    
    private final Hash envelopeHash;

    public ZapperPayload( final TransferIdentifier transferIdentifier, final Path path, final Segment segment,
                          final IOSource ioSource, final HashAlgorithm hashAlgorithm )
        throws IOException
    {
        super( transferIdentifier, path, segment, ioSource, hashAlgorithm );
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
        final org.sonatype.spice.zapper.internal.protobuf.protos.ZapperProtos.SegmentHeader.Builder builder =
            ZapperProtos.SegmentHeader.newBuilder();
        builder.setFileId( getSegment().getZFile().getIdentifier().stringValue() );
        builder.setSegmentId( getIdentifier().stringValue() );
        builder.setSegmentOffset( getSegment().getOffset() );
        builder.setSegmentLength( getSegment().getLength() );

        // build and persist it
        return builder.build().toByteArray();
    }

    protected byte[] createSegmentFooter()
    {
        final org.sonatype.spice.zapper.internal.protobuf.protos.ZapperProtos.SegmentFooter.Builder builder =
            ZapperProtos.SegmentFooter.newBuilder();

        final Hash bodyHash = super.getHash();
        org.sonatype.spice.zapper.internal.protobuf.protos.ZapperProtos.Hash.Builder hashBuilder =
            org.sonatype.spice.zapper.internal.protobuf.protos.ZapperProtos.Hash.newBuilder();
        hashBuilder.setHashAlg( bodyHash.getHashAlgorithmIdentifier().stringValue() );
        hashBuilder.setHashBytes( ByteString.copyFrom( bodyHash.byteValue() ) );
        builder.addHashes( hashBuilder.build() );

        // build and persist it
        return builder.build().toByteArray();
    }
}
