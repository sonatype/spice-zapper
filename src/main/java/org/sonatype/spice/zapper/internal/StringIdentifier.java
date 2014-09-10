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

import org.sonatype.spice.zapper.Identifier;

public class StringIdentifier
    implements Identifier
{
  private final String stringValue;

  public StringIdentifier(final String stringValue) {
    this.stringValue = Check.notNull(stringValue, "StringValue is null!");
  }

  public String stringValue() {
    return stringValue;
  }

  // ==

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + stringValue.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    StringIdentifier other = (StringIdentifier) obj;
    if (stringValue == null) {
      if (other.stringValue != null) {
        return false;
      }
    }
    else if (!stringValue.equals(other.stringValue)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " [stringValue=" + stringValue + "]";
  }
}
