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
