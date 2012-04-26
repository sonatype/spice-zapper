package org.sonatype.spice.zapper.codec;

import org.sonatype.spice.zapper.internal.StringIdentifier;

public class CodecIdentifierImpl
    extends StringIdentifier
    implements CodecIdentifier
{
    public CodecIdentifierImpl( final String stringValue )
    {
        super( stringValue );
    }
}
