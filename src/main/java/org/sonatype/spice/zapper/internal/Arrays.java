/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
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
