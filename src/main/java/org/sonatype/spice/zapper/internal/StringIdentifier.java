package org.sonatype.spice.zapper.internal;

import org.sonatype.spice.zapper.Identifier;

public class StringIdentifier
    implements Identifier
{
    private final String stringValue;

    public StringIdentifier( final String stringValue )
    {
        this.stringValue = Check.notNull( stringValue, "StringValue is null!" );
    }

    public String stringValue()
    {
        return stringValue;
    }

    // ==

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + stringValue.hashCode();
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        StringIdentifier other = (StringIdentifier) obj;
        if ( stringValue == null )
        {
            if ( other.stringValue != null )
                return false;
        }
        else if ( !stringValue.equals( other.stringValue ) )
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " [stringValue=" + stringValue + "]";
    }
}
