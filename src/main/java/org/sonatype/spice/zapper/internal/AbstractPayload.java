package org.sonatype.spice.zapper.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sonatype.spice.zapper.Path;
import org.sonatype.spice.zapper.codec.Codec;
import org.sonatype.spice.zapper.hash.Hash;

public abstract class AbstractPayload
    implements Payload
{
    private final TransferIdentifier transferIdentifier;

    private final Path path;

    private final Hash hash;

    private final List<Codec> codecs;

    protected AbstractPayload( final TransferIdentifier transferIdentifier, final Path path, final Hash hash,
                               final List<Codec> codecs )
    {
        this.transferIdentifier = Check.notNull( transferIdentifier, TransferIdentifier.class );
        this.path = Check.notNull( path, Path.class );
        this.hash = Check.notNull( hash, Hash.class );
        final ArrayList<Codec> cds = new ArrayList<Codec>();
        if ( codecs != null )
        {
            cds.addAll( codecs );
        }
        this.codecs = Collections.unmodifiableList( cds );
    }

    @Override
    public TransferIdentifier getTransferIdentifier()
    {
        return transferIdentifier;
    }

    @Override
    public Path getPath()
    {
        return path;
    }

    @Override
    public Hash getHash()
    {
        return hash;
    }

    @Override
    public List<Codec> getCodecs()
    {
        return codecs;
    }
}
