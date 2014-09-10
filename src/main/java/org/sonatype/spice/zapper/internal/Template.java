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
 * A simple "template" just to defer the evaluation until it is actually needed. See {@link Check} for it's use, but is
 * not limited to it.
 *
 * @author cstamas
 */
public class Template
{
  private final String format;

  private final Object[] args;

  public Template(final String format, final Object... args) {
    if (null == format) {
      throw new NullPointerException("Format is null!");
    }
    this.format = format;
    this.args = args;
  }

  public String evaluate() {
    return String.format(format, args);
  }

  public String getFormat() {
    return format;
  }

  public Object[] getArgs() {
    return args;
  }

  @Override
  public String toString() {
    return evaluate();
  }
}
