package org.sonatype.spice.zapper.internal;

import java.lang.reflect.Array;

/**
 * Port of Java6 Array.copyOf.
 *
 * @author cstamas
 * @since 1.2
 */
public final class Arrays
{
  /**
   * Arrays.copyOf(...) back-port. <br>
   * Copies the specified array, truncating or padding with zeros (if necessary) so the copy has the specified length.
   * For all indices that are valid in both the original array and the copy, the two arrays will contain identical
   * values. For any indices that are valid in the copy but not the original, the copy will contain (byte)0. Such
   * indices will exist if and only if the specified length is greater than that of the original array.
   *
   * @param source the source array.
   * @param len    the length of the new array copy.
   * @return a new array that has the same elements as the source.
   */
  public static final byte[] copyOf(final byte[] source, final int len) {
    final byte[] dest = (byte[]) Array.newInstance(source.getClass().getComponentType(), len);
    System.arraycopy(source, 0, dest, 0, len < source.length ? len : source.length);
    return dest;
  }
}
