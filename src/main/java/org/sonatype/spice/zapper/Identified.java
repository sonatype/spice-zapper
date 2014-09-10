package org.sonatype.spice.zapper;

public interface Identified<I extends Identifier>
{
  I getIdentifier();
}
