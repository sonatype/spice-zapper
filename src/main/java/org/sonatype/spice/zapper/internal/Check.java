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

/**
 * @deprecated Use Guava Preconditions instead.
 */
@Deprecated
public class Check
{
  public static <T> T notNull(final T t, final Class<?> clazz) {
    return notNull(t, new Template("%s is null!", clazz.getSimpleName()));
  }

  public static <T> T notNull(final T t, final Object message) {
    if (null == t) {
      throw new NullPointerException(String.valueOf(message));
    }

    return t;
  }

  public static void argument(boolean condition, final Object message) {
    argument(condition, null, message);
  }

  public static <T> T argument(boolean condition, final T t, final Object message) {
    if (!condition) {
      throw new IllegalArgumentException(String.valueOf(message));
    }

    return t;
  }
}
