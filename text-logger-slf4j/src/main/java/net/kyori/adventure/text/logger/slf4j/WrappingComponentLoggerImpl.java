/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.logger.slf4j;

import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.ext.LoggerWrapper;

final class WrappingComponentLoggerImpl extends LoggerWrapper implements ComponentLogger {
  private static final String FQCN = WrappingComponentLoggerImpl.class.getName();

  // TODO: maybe handle ComponentMessageThrowable logging somehow?

  private final Function<Component, String> serializer;

  WrappingComponentLoggerImpl(final Logger backing, final Function<Component, String> serializer) {
    super(backing, FQCN);
    this.serializer = serializer;
  }

  private String serialize(final Component input) {
    return this.serializer.apply(input);
  }

  private Object maybeSerialize(final @Nullable Object input) {
    if (input instanceof Component) {
      return this.serialize((Component) input);
    } else {
      return input;
    }
  }

  private Object[] maybeSerialize(final @Nullable Object@NotNull... args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof Component) {
        args[i] = this.serialize((Component) args[i]);
      }
    }

    return args;
  }

  @Override
  public void trace(final @NotNull Component msg) {
    if (!this.isTraceEnabled()) return;

    this.trace(this.serialize(msg));
  }

  @Override
  public void trace(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isTraceEnabled()) return;

    this.trace(this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void trace(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isTraceEnabled()) return;

    this.trace(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void trace(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isTraceEnabled()) return;

    this.trace(this.serialize(format), this.maybeSerialize(arguments));
  }

  @Override
  public void trace(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isTraceEnabled()) return;

    this.trace(this.serialize(msg), t);
  }

  @Override
  public void trace(final Marker marker, final @NotNull Component msg) {
    if (!this.isTraceEnabled(marker)) return;

    this.trace(marker, this.serialize(msg));
  }

  @Override
  public void trace(final Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isTraceEnabled(marker)) return;

    this.trace(marker, this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void trace(final Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isTraceEnabled(marker)) return;

    this.trace(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void trace(final Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isTraceEnabled(marker)) return;

    this.trace(marker, this.serialize(format), this.maybeSerialize(argArray));
  }

  @Override
  public void trace(final Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isTraceEnabled(marker)) return;

    this.trace(marker, this.serialize(msg), t);
  }

  @Override
  public void debug(final @NotNull Component msg) {
    if (!this.isDebugEnabled()) return;

    this.debug(this.serialize(msg));
  }

  @Override
  public void debug(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isDebugEnabled()) return;

    this.debug(this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void debug(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isDebugEnabled()) return;

    this.debug(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void debug(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isDebugEnabled()) return;

    this.debug(this.serialize(format), this.maybeSerialize(arguments));
  }

  @Override
  public void debug(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isDebugEnabled()) return;

    this.debug(this.serialize(msg), t);
  }

  @Override
  public void debug(final Marker marker, final @NotNull Component msg) {
    if (!this.isDebugEnabled(marker)) return;

    this.debug(marker, this.serialize(msg));
  }

  @Override
  public void debug(final Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isDebugEnabled(marker)) return;

    this.debug(marker, this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void debug(final Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isDebugEnabled(marker)) return;

    this.debug(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void debug(final Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isDebugEnabled(marker)) return;

    this.debug(marker, this.serialize(format), this.maybeSerialize(argArray));
  }

  @Override
  public void debug(final Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isDebugEnabled(marker)) return;

    this.debug(marker, this.serialize(msg), t);
  }

  @Override
  public void info(final @NotNull Component msg) {
    if (!this.isInfoEnabled()) return;

    this.info(this.serialize(msg));
  }

  @Override
  public void info(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isInfoEnabled()) return;

    this.info(this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void info(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isInfoEnabled()) return;

    this.info(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void info(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isInfoEnabled()) return;

    this.info(this.serialize(format), this.maybeSerialize(arguments));
  }

  @Override
  public void info(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isInfoEnabled()) return;

    this.info(this.serialize(msg), t);
  }

  @Override
  public void info(final Marker marker, final @NotNull Component msg) {
    if (!this.isInfoEnabled(marker)) return;

    this.info(marker, this.serialize(msg));
  }

  @Override
  public void info(final Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isInfoEnabled(marker)) return;

    this.info(marker, this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void info(final Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isInfoEnabled(marker)) return;

    this.info(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void info(final Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isInfoEnabled(marker)) return;

    this.info(marker, this.serialize(format), this.maybeSerialize(argArray));
  }

  @Override
  public void info(final Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isInfoEnabled(marker)) return;

    this.info(marker, this.serialize(msg), t);
  }

  @Override
  public void warn(final @NotNull Component msg) {
    if (!this.isWarnEnabled()) return;

    this.warn(this.serialize(msg));
  }

  @Override
  public void warn(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isWarnEnabled()) return;

    this.warn(this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void warn(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isWarnEnabled()) return;

    this.warn(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void warn(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isWarnEnabled()) return;

    this.warn(this.serialize(format), this.maybeSerialize(arguments));
  }

  @Override
  public void warn(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isWarnEnabled()) return;

    this.warn(this.serialize(msg), t);
  }

  @Override
  public void warn(final Marker marker, final @NotNull Component msg) {
    if (!this.isWarnEnabled(marker)) return;

    this.warn(marker, this.serialize(msg));
  }

  @Override
  public void warn(final Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isWarnEnabled(marker)) return;

    this.warn(marker, this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void warn(final Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isWarnEnabled(marker)) return;

    this.warn(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void warn(final Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isWarnEnabled(marker)) return;

    this.warn(marker, this.serialize(format), this.maybeSerialize(argArray));
  }

  @Override
  public void warn(final Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isWarnEnabled(marker)) return;

    this.warn(marker, this.serialize(msg), t);
  }

  @Override
  public void error(final @NotNull Component msg) {
    if (!this.isErrorEnabled()) return;

    this.error(this.serialize(msg));
  }

  @Override
  public void error(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isErrorEnabled()) return;

    this.error(this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void error(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isErrorEnabled()) return;

    this.error(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void error(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isErrorEnabled()) return;

    this.error(this.serialize(format), this.maybeSerialize(arguments));
  }

  @Override
  public void error(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isErrorEnabled()) return;

    this.error(this.serialize(msg), t);
  }

  @Override
  public void error(final Marker marker, final @NotNull Component msg) {
    if (!this.isErrorEnabled(marker)) return;

    this.error(marker, this.serialize(msg));
  }

  @Override
  public void error(final Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isErrorEnabled(marker)) return;

    this.error(marker, this.serialize(format), this.maybeSerialize(arg));
  }

  @Override
  public void error(final Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isErrorEnabled(marker)) return;

    this.error(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
  }

  @Override
  public void error(final Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isErrorEnabled(marker)) return;

    this.error(marker, this.serialize(format), this.maybeSerialize(argArray));
  }

  @Override
  public void error(final Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isErrorEnabled(marker)) return;

    this.error(marker, this.serialize(msg), t);
  }
}
