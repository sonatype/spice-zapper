package eu.flatwhite.zapper.internal.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.protobuf.ByteString;

import eu.flatwhite.zapper.IO;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.codec.Codec;
import eu.flatwhite.zapper.hash.Hash;
import eu.flatwhite.zapper.hash.HashingOutputStream;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.NonClosingOutputStream;
import eu.flatwhite.zapper.internal.Track;
import eu.flatwhite.zapper.internal.TrackSegment;
import eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.SegmentFooter;
import eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.SegmentHeader;
import eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.TrackFooter;
import eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.TrackHeader;

public class TrackSender
{
    private final Parameters parameters;

    private final IO io;

    public TrackSender( final Parameters parameters, final IO io )
    {
        this.parameters = Check.notNull( parameters, "Parameters is null!" );
        this.io = Check.notNull( io, "IO is null!" );
    }

    public void send( final Track track, final OutputStream trackStream )
        throws IOException
    {
        // write track
        writeTrack( track, trackStream );

        // flush
        trackStream.flush();
    }

    // ==

    protected void writeTrack( final Track track, final OutputStream trackStream )
        throws IOException
    {
        // write raw header
        createTrackHeader( track ).writeDelimitedTo( trackStream );

        // we want to hash track body, whatever track codec is applied
        final HashingOutputStream trackDigestStream = parameters.getHashAlgorithm().hashOutput( trackStream );

        // apply track-level filters
        OutputStream trackOutputStream = new NonClosingOutputStream( trackDigestStream );
        for ( Codec codec : track.getTrackFilters() )
        {
            trackOutputStream = codec.encode( trackOutputStream );
        }

        // write segments one by one
        for ( TrackSegment segment : track.getTrackSegments() )
        {
            writeSegment( segment, trackOutputStream );
        }

        // needed for codecs (to do padding or whatever needed)
        trackOutputStream.close();

        // write raw footer with hash of the body
        createTrackFooter( track, trackDigestStream.getHash() ).writeDelimitedTo( trackStream );
    }

    protected void writeSegment( final TrackSegment segment, final OutputStream segmentStream )
        throws IOException
    {
        // write raw header
        createSegmentHeader( segment ).writeDelimitedTo( segmentStream );

        // we want to hash the actual body, whatever codec is applied
        final HashingOutputStream segmentDigestStream = parameters.getHashAlgorithm().hashOutput( segmentStream );

        // apply segment-level filters
        OutputStream segmentOutputStream = new NonClosingOutputStream( segmentDigestStream );
        for ( Codec codec : segment.getSegmentFilters() )
        {
            segmentOutputStream = codec.encode( segmentOutputStream );
        }

        // write the body to coded output
        copy( io.readFileSegment( segment.getZFile(), segment ), segmentOutputStream );

        // needed for codecs (to do padding or whatever needed)
        segmentOutputStream.close();

        // write raw footer with hash of the body
        createSegmentFooter( segment, segmentDigestStream.getHash() ).writeDelimitedTo( segmentStream );
    }

    // ==

    protected TrackHeader createTrackHeader( final Track track )
    {
        final TrackHeader.Builder trackHeaderBuilder =
            TrackHeader.newBuilder().setTransferId( track.getTransferId().stringValue() ).setTrackId(
                track.getIdentifier().stringValue() ).setSegmentCount( track.getTracksegmentCount() );
        for ( Codec filter : track.getTrackFilters() )
        {
            trackHeaderBuilder.addFilters( filter.getIdentifier().stringValue() );
        }
        return trackHeaderBuilder.build();
    }

    protected TrackFooter createTrackFooter( final Track track, final Hash trackHash )
    {
        final eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.Hash hash =
            eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.Hash.newBuilder().setHashAlg(
                trackHash.getAlgorithm().getIdentifier().stringValue() ).setHashBytes(
                ByteString.copyFrom( trackHash.getHash() ) ).build();
        final TrackFooter.Builder trackFooterBuilder = TrackFooter.newBuilder().addHashes( hash );

        return trackFooterBuilder.build();
    }

    protected SegmentHeader createSegmentHeader( final TrackSegment trackSegment )
    {
        final SegmentHeader.Builder segmentHeaderBuilder =
            SegmentHeader.newBuilder().setSegmentId( trackSegment.getIdentifier().stringValue() ).setSegmentLength(
                trackSegment.getLength() );
        for ( Codec filter : trackSegment.getSegmentFilters() )
        {
            segmentHeaderBuilder.addFilters( filter.getIdentifier().stringValue() );
        }

        return segmentHeaderBuilder.build();
    }

    protected SegmentFooter createSegmentFooter( final TrackSegment trackSegment, final Hash segmentHash )
    {
        final eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.Hash hash =
            eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.Hash.newBuilder().setHashAlg(
                segmentHash.getAlgorithm().getIdentifier().stringValue() ).setHashBytes(
                ByteString.copyFrom( segmentHash.getHash() ) ).build();

        final SegmentFooter.Builder segmentFooterBuilder = SegmentFooter.newBuilder().addHashes( hash );

        return segmentFooterBuilder.build();
    }

    public static void copy( final InputStream in, final OutputStream out )
        throws IOException
    {
        final byte[] buffer = new byte[8192];

        try
        {
            while ( true )
            {
                int amountRead = in.read( buffer );
                if ( amountRead == -1 )
                {
                    break;
                }
                out.write( buffer, 0, amountRead );
            }
        }
        finally
        {
            out.flush();
        }
    }
}
