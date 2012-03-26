package eu.flatwhite.zapper.internal;

import eu.flatwhite.zapper.Identifier;

public class Sha1HashIdentifier
    implements Identifier
{
    public static final String KEY = "SHA-1";

    @Override
    public String stringValue()
    {
        return KEY;
    }
}
