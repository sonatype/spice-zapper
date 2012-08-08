package org.sonatype.spice.zapper.internal.zapper;

import java.io.IOException;
import java.util.Collections;

import org.sonatype.spice.zapper.Parameters;
import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.HashUtils;
import org.sonatype.spice.zapper.internal.AbstractIdentified;
import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.MessagePayload;
import org.sonatype.spice.zapper.internal.PayloadCreator;
import org.sonatype.spice.zapper.internal.Protocol;
import org.sonatype.spice.zapper.internal.ProtocolIdentifier;
import org.sonatype.spice.zapper.internal.SegmentCreator;
import org.sonatype.spice.zapper.internal.Transfer;
import org.sonatype.spice.zapper.internal.transport.AbstractClient;

/**
 * Zapper protocol.
 * 
 * @author cstamas
 */
public class ZapperProtocol
    extends AbstractIdentified<ProtocolIdentifier>
    implements Protocol
{
    public static ProtocolIdentifier ID = new ProtocolIdentifier( "zapper" );

    private final Parameters parameters;

    public ZapperProtocol( final Parameters parameters )
    {
        super( ID );
        this.parameters = Check.notNull( parameters, Parameters.class );
    }

    public Parameters getParameters()
    {
        return parameters;
    }

    @Override
    public SegmentCreator getSegmentCreator()
    {
        return new ZapperSegmentCreator( getParameters().getMaximumSegmentLength() );
    }

    @Override
    public PayloadCreator getPayloadCreator()
    {
        return new ZapperPayloadCreator( getParameters() );
    }

    @Override
    public void beforeUpload( final Transfer transfer, final AbstractClient<?> client )
        throws IOException
    {
        final byte[] payload = new byte[0];
        final MessagePayload message =
            new MessagePayload( transfer.getIdentifier(), new Path( "beforeUpload" ), payload, HashUtils.getDigest(
                parameters.getHashAlgorithm(), payload ), Collections.<Codec> emptyList() );
        client.upload( message );
    }

    @Override
    public void afterUpload( final Transfer transfer, final AbstractClient<?> client )
        throws IOException
    {
        final byte[] payload = new byte[0];
        final MessagePayload message =
            new MessagePayload( transfer.getIdentifier(), new Path( "afterUpload" ), payload, HashUtils.getDigest(
                parameters.getHashAlgorithm(), payload ), Collections.<Codec> emptyList() );
        client.upload( message );
    }
}
