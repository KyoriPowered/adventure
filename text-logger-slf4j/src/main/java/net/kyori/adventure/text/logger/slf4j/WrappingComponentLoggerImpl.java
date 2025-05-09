/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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

import java.util.Arrays;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.spi.LocationAwareLogger;
import org.slf4j.spi.LoggingEventBuilder;
import org.slf4j.spi.NOPLoggingEventBuilder;

final class WrappingComponentLoggerImpl implements ComponentLogger {
  private static final String FQCN = WrappingComponentLoggerImpl.class.getName();

  private final Logger logger;
  private final boolean isLocationAware;
  private final Function<Component, String> serializer;

  WrappingComponentLoggerImpl(final Logger backing, final Function<Component, String> serializer) {
    this.serializer = serializer;
    this.logger = backing;
    this.isLocationAware = backing instanceof LocationAwareLogger;
  }

  private String serialize(final Component input) {
    if (input == null) return null;

    return this.serializer.apply(input);
  }

  private Object maybeSerialize(final @Nullable Object input) {
    if (input instanceof ComponentLike) {
      return this.serialize(((ComponentLike) input).asComponent());
    } else {
      return input;
    }
  }

  private Object[] maybeSerialize(final @Nullable Object@NotNull... args) {
    Object[] writable = args;
    for (int i = 0; i < writable.length; i++) {
      if (writable[i] instanceof ComponentLike) {
        if (writable == args) {
          writable = Arrays.copyOf(args, args.length);
        }
        writable[i] = this.serialize(((ComponentLike) writable[i]).asComponent());
      }
    }

    if (writable.length > 0 && writable[writable.length - 1] instanceof Throwable) {
      if (writable == args) {
        writable = Arrays.copyOf(args, args.length);
      }
      writable[writable.length - 1] = UnpackedComponentThrowable.unpack((Throwable) writable[writable.length - 1], this.serializer);
    }

    return writable;
  }

  // Basic methods, plain delegation

  @Override
  public String getName() {
    return this.logger.getName();
  }

  @Override
  public boolean isTraceEnabled() {
    return this.logger.isTraceEnabled();
  }

  @Override
  public boolean isTraceEnabled(final Marker marker) {
    return this.logger.isTraceEnabled(marker);
  }

  @Override
  public boolean isDebugEnabled() {
    return this.logger.isDebugEnabled();
  }

  @Override
  public boolean isDebugEnabled(final Marker marker) {
    return this.logger.isDebugEnabled(marker);
  }

  @Override
  public boolean isInfoEnabled() {
    return this.logger.isInfoEnabled();
  }

  @Override
  public boolean isInfoEnabled(final Marker marker) {
    return this.logger.isInfoEnabled(marker);
  }

  @Override
  public boolean isWarnEnabled() {
    return this.logger.isWarnEnabled();
  }

  @Override
  public boolean isWarnEnabled(final Marker marker) {
    return this.logger.isWarnEnabled(marker);
  }

  @Override
  public boolean isErrorEnabled() {
    return this.logger.isErrorEnabled();
  }

  @Override
  public boolean isErrorEnabled(final Marker marker) {
    return this.logger.isErrorEnabled(marker);
  }

  @Override
  public boolean isEnabledForLevel(final Level level) {
    return this.logger.isEnabledForLevel(level);
  }

  @Override
  public @NotNull LoggingEventBuilder makeLoggingEventBuilder(final @NotNull Level level) {
    return this.logger.makeLoggingEventBuilder(level);
  }

  @Override
  public @NotNull LoggingEventBuilder atLevel(final @NotNull Level level) {
    if (this.logger.isEnabledForLevel(level)) {
      return this.logger.makeLoggingEventBuilder(level);
    } else {
      return NOPLoggingEventBuilder.singleton();
    }
  }

  // Standard string methods, to process potential Component arguments

  @Override
  public void trace(final @NotNull String format) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        format,
        null,
        null
      );
    } else {
      this.logger.trace(format);
    }
  }

  @Override
  public void trace(final @NotNull String format, final @Nullable Object arg) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.trace(format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void trace(final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.trace(format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void trace(final @NotNull String format, final @Nullable Object @NotNull... arguments) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        format,
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.trace(format, this.maybeSerialize(arguments));
    }
  }

  @Override
  public void trace(final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.trace(msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull String msg) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        msg,
        null,
        null
      );
    } else {
      this.logger.trace(marker, msg);
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.trace(marker, format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.trace(marker, format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object @NotNull... argArray) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        format,
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.trace(marker, format, this.maybeSerialize(argArray));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.trace(marker, msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void debug(final @NotNull String format) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        format,
        null,
        null
      );
    } else {
      this.logger.debug(format);
    }
  }

  @Override
  public void debug(final @NotNull String format, final @Nullable Object arg) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.debug(format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void debug(final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.debug(format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void debug(final @NotNull String format, final @Nullable Object @NotNull... arguments) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        format,
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.debug(format, this.maybeSerialize(arguments));
    }
  }

  @Override
  public void debug(final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.debug(msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull String msg) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        msg,
        null,
        null
      );
    } else {
      this.logger.debug(marker, msg);
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.debug(marker, format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.debug(marker, format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object @NotNull... argArray) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        format,
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.debug(marker, format, this.maybeSerialize(argArray));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.debug(marker, msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void info(final @NotNull String format) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        format,
        null,
        null
      );
    } else {
      this.logger.info(format);
    }
  }

  @Override
  public void info(final @NotNull String format, final @Nullable Object arg) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.info(format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void info(final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.info(format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void info(final @NotNull String format, final @Nullable Object @NotNull... arguments) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        format,
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.info(format, this.maybeSerialize(arguments));
    }
  }

  @Override
  public void info(final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.info(msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull String msg) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        msg,
        null,
        null
      );
    } else {
      this.logger.info(marker, msg);
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.info(marker, format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.info(marker, format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object @NotNull... argArray) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        format,
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.info(marker, format, this.maybeSerialize(argArray));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.info(marker, msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void warn(final @NotNull String format) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        format,
        null,
        null
      );
    } else {
      this.logger.warn(format);
    }
  }

  @Override
  public void warn(final @NotNull String format, final @Nullable Object arg) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.warn(format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void warn(final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.warn(format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void warn(final @NotNull String format, final @Nullable Object @NotNull... arguments) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        format,
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.warn(format, this.maybeSerialize(arguments));
    }
  }

  @Override
  public void warn(final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.warn(msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull String msg) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        msg,
        null,
        null
      );
    } else {
      this.logger.warn(marker, msg);
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.warn(marker, format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.warn(marker, format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object @NotNull... argArray) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        format,
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.warn(marker, format, this.maybeSerialize(argArray));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.warn(marker, msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void error(final @NotNull String format) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        format,
        null,
        null
      );
    } else {
      this.logger.error(format);
    }
  }

  @Override
  public void error(final @NotNull String format, final @Nullable Object arg) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.error(format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void error(final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.error(format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void error(final @NotNull String format, final @Nullable Object @NotNull... arguments) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        format,
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.error(format, this.maybeSerialize(arguments));
    }
  }

  @Override
  public void error(final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.error(msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull String msg) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        msg,
        null,
        null
      );
    } else {
      this.logger.error(marker, msg);
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        format,
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.error(marker, format, this.maybeSerialize(arg));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        format,
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.error(marker, format, this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull String format, final @Nullable Object @NotNull... argArray) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        format,
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.error(marker, format, this.maybeSerialize(argArray));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull String msg, final @Nullable Throwable t) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        msg,
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.error(marker, msg, UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  // Component-primary methods

  @Override
  public void trace(final @NotNull Component format) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(format),
        null,
        null
      );
    } else {
      this.logger.trace(this.serialize(format));
    }
  }

  @Override
  public void trace(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.trace(this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void trace(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.trace(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void trace(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(format),
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.trace(this.serialize(format), this.maybeSerialize(arguments));
    }
  }

  @Override
  public void trace(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isTraceEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.trace(this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull Component msg) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(msg),
        null,
        null
      );
    } else {
      this.logger.trace(marker, this.serialize(msg));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.trace(marker, this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.trace(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(format),
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.trace(marker, this.serialize(format), this.maybeSerialize(argArray));
    }
  }

  @Override
  public void trace(final @NotNull Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isTraceEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.TRACE_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.trace(marker, this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void debug(final @NotNull Component format) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(format),
        null,
        null
      );
    } else {
      this.logger.debug(this.serialize(format));
    }
  }

  @Override
  public void debug(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.debug(this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void debug(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.debug(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void debug(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(format),
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.debug(this.serialize(format), this.maybeSerialize(arguments));
    }
  }

  @Override
  public void debug(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isDebugEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.debug(this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull Component msg) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(msg),
        null,
        null
      );
    } else {
      this.logger.debug(marker, this.serialize(msg));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.debug(marker, this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.debug(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(format),
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.debug(marker, this.serialize(format), this.maybeSerialize(argArray));
    }
  }

  @Override
  public void debug(final @NotNull Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isDebugEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.DEBUG_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.debug(marker, this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void info(final @NotNull Component format) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(format),
        null,
        null
      );
    } else {
      this.logger.info(this.serialize(format));
    }
  }

  @Override
  public void info(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.info(this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void info(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.info(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void info(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(format),
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.info(this.serialize(format), this.maybeSerialize(arguments));
    }
  }

  @Override
  public void info(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isInfoEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.info(this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull Component msg) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(msg),
        null,
        null
      );
    } else {
      this.logger.info(marker, this.serialize(msg));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.info(marker, this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.info(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(format),
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.info(marker, this.serialize(format), this.maybeSerialize(argArray));
    }
  }

  @Override
  public void info(final @NotNull Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isInfoEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.INFO_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.info(marker, this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void warn(final @NotNull Component format) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(format),
        null,
        null
      );
    } else {
      this.logger.warn(this.serialize(format));
    }
  }

  @Override
  public void warn(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.warn(this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void warn(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.warn(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void warn(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(format),
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.warn(this.serialize(format), this.maybeSerialize(arguments));
    }
  }

  @Override
  public void warn(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isWarnEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.warn(this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull Component msg) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(msg),
        null,
        null
      );
    } else {
      this.logger.warn(marker, this.serialize(msg));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.warn(marker, this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.warn(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(format),
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.warn(marker, this.serialize(format), this.maybeSerialize(argArray));
    }
  }

  @Override
  public void warn(final @NotNull Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isWarnEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.WARN_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.warn(marker, this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void error(final @NotNull Component format) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(format),
        null,
        null
      );
    } else {
      this.logger.error(this.serialize(format));
    }
  }

  @Override
  public void error(final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.error(this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void error(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.error(this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void error(final @NotNull Component format, final @Nullable Object @NotNull... arguments) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(format),
        this.maybeSerialize(arguments),
        null
      );
    } else {
      this.logger.error(this.serialize(format), this.maybeSerialize(arguments));
    }
  }

  @Override
  public void error(final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isErrorEnabled()) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        null,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.error(this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull Component msg) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(msg),
        null,
        null
      );
    } else {
      this.logger.error(marker, this.serialize(msg));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg)},
        null
      );
    } else {
      this.logger.error(marker, this.serialize(format), this.maybeSerialize(arg));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(format),
        new Object[] {this.maybeSerialize(arg1), this.maybeSerialize(arg2)},
        null
      );
    } else {
      this.logger.error(marker, this.serialize(format), this.maybeSerialize(arg1), this.maybeSerialize(arg2));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(format),
        this.maybeSerialize(argArray),
        null
      );
    } else {
      this.logger.error(marker, this.serialize(format), this.maybeSerialize(argArray));
    }
  }

  @Override
  public void error(final @NotNull Marker marker, final @NotNull Component msg, final @Nullable Throwable t) {
    if (!this.isErrorEnabled(marker)) return;

    if (this.isLocationAware) {
      ((LocationAwareLogger) this.logger).log(
        marker,
        FQCN,
        LocationAwareLogger.ERROR_INT,
        this.serialize(msg),
        null,
        UnpackedComponentThrowable.unpack(t, this.serializer)
      );
    } else {
      this.logger.error(marker, this.serialize(msg), UnpackedComponentThrowable.unpack(t, this.serializer));
    }
  }
}
