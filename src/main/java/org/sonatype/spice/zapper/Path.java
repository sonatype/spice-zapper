package org.sonatype.spice.zapper;

import org.sonatype.spice.zapper.internal.Check;
import org.sonatype.spice.zapper.internal.StringIdentifier;

public class Path
    extends StringIdentifier
{
    public Path( final String stringValue )
    {
        super( stringValue );
        Check.argument( !stringValue.contains( "\\" ), "Path must contain only forward slashes! path=" + stringValue );
    }
}
