package eu.flatwhite.zapper.internal.transport;

import java.io.IOException;
import java.io.InputStream;

import eu.flatwhite.zapper.IOTarget;
import eu.flatwhite.zapper.Parameters;
import eu.flatwhite.zapper.Range;
import eu.flatwhite.zapper.codec.Codec;
import eu.flatwhite.zapper.hash.Hash;
import eu.flatwhite.zapper.hash.HashAlgorithm;
import eu.flatwhite.zapper.hash.HashImpl;
import eu.flatwhite.zapper.hash.HashingInputStream;
import eu.flatwhite.zapper.internal.Check;
import eu.flatwhite.zapper.internal.RangeImpl;
import eu.flatwhite.zapper.internal.RangeInputStream;
import eu.flatwhite.zapper.internal.StringIdentifier;
import eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.SegmentFooter;
import eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.SegmentHeader;
import eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.TrackFooter;
import eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.TrackHeader;

public class TrackReceiver
{
    private final Parameters parameters;

    private final IOTarget iotarget;

    public TrackReceiver( final Parameters parameters, final IOTarget iotarget )
    {
        this.parameters = Check.notNull( parameters, "Parameters is null!" );
        this.iotarget = Check.notNull( iotarget, "IOTarget is null!" );
    }

    public void receive( final InputStream trackStream )
        throws IOException
    {
        // Needed for track hash
        final HashingInputStream trackDigestStream = parameters.getHashAlgorithm().hashInput( trackStream );

        // introduce "track input stream"
        InputStream trackInputStream = trackDigestStream;

        // read header
        final TrackHeader trackHeader = TrackHeader.parseDelimitedFrom( trackInputStream );

        // apply track-level filters
        for ( String codecId : trackHeader.getFiltersList() )
        {
            final Codec codec = parameters.getCodecs().get( new StringIdentifier( codecId ) );
            if ( codec != null )
            {
                trackInputStream = codec.decode( trackInputStream );
            }
            else
            {
                throw new IOException( String.format( "Codec %s needed by track %s but not available!", codecId,
                    trackHeader.getTrackId() ) );
            }
        }

        for ( int i = 0; i < trackHeader.getSegmentCount(); i++ )
        {
            final SegmentHeader segmentHeader =
                verifySegmentHeader( SegmentHeader.parseDelimitedFrom( trackInputStream ) );

            final Range segmentRange =
                new RangeImpl( new StringIdentifier( segmentHeader.getSegmentId() ), 0,
                    segmentHeader.getSegmentLength() );

            final HashingInputStream segmentDigestStream =
                parameters.getHashAlgorithm().hashInput( new RangeInputStream( trackInputStream, segmentRange ) );

            iotarget.writeSegment( segmentRange, segmentDigestStream );

            final SegmentFooter segmentFooter =
                verifySegmentFooter( SegmentFooter.parseDelimitedFrom( trackInputStream ),
                    segmentDigestStream.getHash() );

        }

        final TrackFooter trackFooter =
            verifyTrackFooter( TrackFooter.parseDelimitedFrom( trackInputStream ), trackDigestStream.getHash() );
    }

    // ==

    protected void readTrack( final InputStream trackStream )
        throws IOException
    {
        // read raw header
        final TrackHeader trackHeader = verifyTrackHeader( TrackHeader.parseDelimitedFrom( trackStream ) );

        // we want to hash track body, whatever track codec is applied
        final HashingInputStream trackDigestStream = parameters.getHashAlgorithm().hashInput( trackStream );

        // apply track-level filters
        InputStream trackInputStream = trackDigestStream;
        for ( String codecId : trackHeader.getFiltersList() )
        {
            final Codec codec = parameters.getCodecs().get( new StringIdentifier( codecId ) );
            if ( codec != null )
            {
                trackInputStream = codec.decode( trackInputStream );
            }
            else
            {
                throw new IOException( String.format( "Codec %s needed by track %s but not available!", codecId,
                    trackHeader.getTrackId() ) );
            }
        }

        // read segments one by one
        for ( int i = 0; i < trackHeader.getSegmentCount(); i++ )
        {
            readSegment( parameters, iotarget, trackInputStream );
        }

        // read raw footer with hash of the body
        final TrackFooter trackFooter =
            verifyTrackFooter( TrackFooter.parseDelimitedFrom( trackStream ), trackDigestStream.getHash() );
    }

    protected void readSegment( final Parameters parameters, final IOTarget iotarget, final InputStream segmentStream )
        throws IOException
    {
        // read raw header
        final SegmentHeader segmentHeader = verifySegmentHeader( SegmentHeader.parseDelimitedFrom( segmentStream ) );

        // we want to hash the actual body, whatever codec is applied
        // but we need to delimit the stream to expected size
        final Range segmentRange =
            new RangeImpl( new StringIdentifier( segmentHeader.getSegmentId() ), 0, segmentHeader.getSegmentLength() );
        final HashingInputStream segmentDigestStream =
            parameters.getHashAlgorithm().hashInput( new RangeInputStream( segmentStream, segmentRange ) );

        // apply segment-level filters
        InputStream segmentInputStream = segmentDigestStream;
        for ( String codecId : segmentHeader.getFiltersList() )
        {
            final Codec codec = parameters.getCodecs().get( new StringIdentifier( codecId ) );
            if ( codec != null )
            {
                segmentInputStream = codec.decode( segmentInputStream );
            }
            else
            {
                throw new IOException( String.format( "Codec %s needed by track %s but not available!", codecId,
                    segmentHeader.getSegmentId() ) );
            }
        }

        // read the body from decoded input
        iotarget.writeSegment( segmentRange, segmentInputStream );

        // read raw footer with hash of the body
        final SegmentFooter segmentFooter =
            verifySegmentFooter( SegmentFooter.parseDelimitedFrom( segmentStream ), segmentDigestStream.getHash() );
    }

    // ==

    protected Hash instantiateHash( Parameters parameters,
                                    eu.flatwhite.zapper.internal.protobuf.protos.ZapperProtos.Hash wireHash )
        throws IOException
    {
        final String algorithmId = wireHash.getHashAlg();
        final byte[] hash = wireHash.getHashBytes().toByteArray();

        final HashAlgorithm algorithm = parameters.getHashAlgorithm();
        if ( algorithm.getIdentifier().stringValue().equals( algorithmId ) )
        {
            return new HashImpl( algorithm, hash );
        }
        else
        {
            throw new IOException( String.format( "No suitable HashAlgorithm found for algorithId=%s", algorithmId ) );
        }
    }

    protected TrackHeader verifyTrackHeader( final TrackHeader trackHeader )
        throws IOException
    {
        assertThat( "zTrh".equals( trackHeader.getMagic() ), "Track header magic is invalid!" );
        return trackHeader;
    }

    protected TrackFooter verifyTrackFooter( final TrackFooter trackFooter, final Hash calculatedHash )
        throws IOException
    {
        assertThat( "zTrf".equals( trackFooter.getMagic() ), "Track footer magic is invalid!" );
        if ( trackFooter.getHashesCount() > 0 )
        {
            final Hash receivedHash = instantiateHash( parameters, trackFooter.getHashes( 0 ) );
            assertThat( calculatedHash.equals( receivedHash ),
                String.format( "Track hash mismatch! Received=%s Calculated=%s", receivedHash, calculatedHash ) );
        }
        return trackFooter;
    }

    protected SegmentHeader verifySegmentHeader( final SegmentHeader segmentHeader )
        throws IOException
    {
        assertThat( "zSeh".equals( segmentHeader.getMagic() ), "Segment header magic is invalid!" );
        return segmentHeader;
    }

    protected SegmentFooter verifySegmentFooter( final SegmentFooter segmentFooter, final Hash calculatedHash )
        throws IOException
    {
        assertThat( "zSef".equals( segmentFooter.getMagic() ), "Segment footer magic is invalid!" );
        if ( segmentFooter.getHashesCount() > 0 )
        {
            final Hash receivedHash = instantiateHash( parameters, segmentFooter.getHashes( 0 ) );
            assertThat( calculatedHash.equals( receivedHash ),
                String.format( "Segment hash mismatch! Received=%s Calculated=%s", receivedHash, calculatedHash ) );
        }
        return segmentFooter;
    }

    protected void assertThat( final boolean condition, final String message )
        throws IOException
    {
        if ( !condition )
        {
            throw new IOException( message );
        }
    }
}
