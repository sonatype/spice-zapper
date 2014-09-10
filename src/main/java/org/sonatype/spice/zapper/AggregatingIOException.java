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
package org.sonatype.spice.zapper;

import java.io.IOException;

/**
 * IOException that carries the statuses/exceptions per each track.
 *
 * @author cstamas
 */
public class AggregatingIOException
    extends IOException
{
  private static final long serialVersionUID = 4686084672180030357L;

  private final IOException[] trackExceptions;

  public AggregatingIOException(final String message, final IOException[] trackExceptions) {
    super(buildMessage(message, trackExceptions));
    this.trackExceptions = trackExceptions;
  }

  /**
   * Returns exceptions thrown by tracks. The returned array might have {@code null} elements if track did not hit any
   * exception!
   */
  protected IOException[] getTrackExceptions() {
    return trackExceptions;
  }

  static String buildMessage(final String message, final IOException[] trackExceptions) {
    final StringBuilder sb = new StringBuilder(message).append("\n");

    int trackNo = 1;
    for (IOException e : trackExceptions) {
      sb.append(" ").append(trackNo++).append(". ");
      if (e == null) {
        sb.append("Finished OK");
      }
      else {
        sb.append(e.getMessage()).append("\n");
      }
    }

    return sb.toString();
  }
}
