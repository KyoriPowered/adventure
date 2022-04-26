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

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;

import static java.util.Objects.requireNonNull;

/**
 * An extended type of Logger capable of logging formatted components to the console.
 *
 * <p>The methods in this logger interface are intended to exactly mirror those methods in {@link Logger} that take a format string, but instead accepting {@link Component}s.</p>
 *
 * <p>Any {@code arg}s may be passed as Components as well.</p>
 *
 * @since 4.11.0
 */
public interface ComponentLogger extends Logger {
  /**
   * Get a logger instance with the name of the calling class.
   *
   * <p>This method is caller-sensitive and should not be wrapped. See
   *
   * <p>This logger is produced by implementations of the {@link ComponentLoggerProvider}.</p>
   *
   * @return a logger with the name of the calling class
   * @since 4.11.0
   */
  static @NotNull ComponentLogger logger() {
    return logger(CallerClassFinder.callingClassName());
  }

  /**
   * Get a logger instance with the provided name.
   *
   * <p>This logger is produced by implementations of the {@link ComponentLoggerProvider}.</p>
   *
   * @param name the name of the logger
   * @return a logger with the provided name
   * @since 4.11.0
   */
  static @NotNull ComponentLogger logger(final @NotNull String name) {
    return Handler.logger(requireNonNull(name, "name"));
  }

  /**
   * Get a logger instance with the binary name of the provided class.
   *
   * <p>This logger is produced by implementations of the {@link ComponentLoggerProvider}.</p>
   *
   * @param clazz the class to use when naming the logger
   * @return a logger with the name of the calling class
   * @since 4.11.0
   */
  static @NotNull ComponentLogger logger(final @NotNull Class<?> clazz) {
    return logger(clazz.getName());
  }

  /**
   * Log a message at the TRACE level.
   *
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void trace(final @NotNull Component msg);

  /**
   * Log a message at the TRACE level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the TRACE level. </p>
   *
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void trace(final @NotNull Component format, final @Nullable Object arg);

  /**
   * Log a message at the TRACE level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the TRACE level. </p>
   *
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void trace(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * Log a message at the TRACE level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the TRACE level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for TRACE. The variants taking {@link #trace(Component, Object) one} and
   * {@link #trace(Component, Object, Object) two} arguments exist solely in order to avoid this hidden cost.</p>
   *
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void trace(final @NotNull Component format, final @Nullable Object @NotNull... arguments);

  /**
   * Log an exception (throwable) at the TRACE level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void trace(final @NotNull Component msg, final @Nullable Throwable t);

  /**
   * Log a message with the specific Marker at the TRACE level.
   *
   * @param marker the marker data specific to this log statement
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void trace(final @NotNull Marker marker, final @NotNull Component msg);

  /**
   * This method is similar to {@link #trace(Component, Object)} method except that the
   * marker data is also taken into consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void trace(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg);

  /**
   * This method is similar to {@link #trace(Component, Object, Object)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void trace(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * This method is similar to {@link #trace(Component, Object...)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param argArray an array of arguments
   * @since 4.11.0
   */
  void trace(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... argArray);

  /**
   * This method is similar to {@link #trace(Component, Throwable)} method except that the
   * marker data is also taken into consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void trace(final @NotNull Marker marker, final @NotNull Component msg, final @Nullable Throwable t);

  /**
   * Log a message at the DEBUG level.
   *
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void debug(final @NotNull Component msg);

  /**
   * Log a message at the DEBUG level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the DEBUG level. </p>
   *
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void debug(final @NotNull Component format, final @Nullable Object arg);

  /**
   * Log a message at the DEBUG level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the DEBUG level. </p>
   *
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void debug(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * Log a message at the DEBUG level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the DEBUG level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for DEBUG. The variants taking
   * {@link #debug(Component, Object) one} and {@link #debug(Component, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.</p>
   *
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void debug(final @NotNull Component format, final @Nullable Object @NotNull... arguments);

  /**
   * Log an exception (throwable) at the DEBUG level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void debug(final @NotNull Component msg, final @Nullable Throwable t);

  /**
   * Log a message with the specific Marker at the DEBUG level.
   *
   * @param marker the marker data specific to this log statement
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void debug(final @NotNull Marker marker, final @NotNull Component msg);

  /**
   * This method is similar to {@link #debug(Component, Object)} method except that the
   * marker data is also taken into consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void debug(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg);

  /**
   * This method is similar to {@link #debug(Component, Object, Object)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void debug(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * This method is similar to {@link #debug(Component, Object...)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void debug(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object @NotNull... arguments);

  /**
   * This method is similar to {@link #debug(Component, Throwable)} method except that the
   * marker data is also taken into consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void debug(final @NotNull Marker marker, final @NotNull Component msg, final @Nullable Throwable t);

  /**
   * Log a message at the INFO level.
   *
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void info(final @NotNull Component msg);

  /**
   * Log a message at the INFO level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the INFO level. </p>
   *
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void info(final @NotNull Component format, final @Nullable Object arg);

  /**
   * Log a message at the INFO level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the INFO level. </p>
   *
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void info(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * Log a message at the INFO level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the INFO level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for INFO. The variants taking
   * {@link #info(Component, Object) one} and {@link #info(Component, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.</p>
   *
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void info(final @NotNull Component format, final @Nullable Object@NotNull... arguments);

  /**
   * Log an exception (throwable) at the INFO level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void info(final @NotNull Component msg, final @Nullable Throwable t);

  /**
   * Log a message with the specific Marker at the INFO level.
   *
   * @param marker The marker specific to this log statement
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void info(final @NotNull Marker marker, final @NotNull Component msg);

  /**
   * This method is similar to {@link #info(Component, Object)} method except that the
   * marker data is also taken into consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void info(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg);

  /**
   * This method is similar to {@link #info(Component, Object, Object)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void info(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * This method is similar to {@link #info(Component, Object...)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void info(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object@NotNull... arguments);

  /**
   * This method is similar to {@link #info(Component, Throwable)} method
   * except that the marker data is also taken into consideration.
   *
   * @param marker the marker data for this log statement
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void info(final @NotNull Marker marker, final @NotNull Component msg, final @NotNull Throwable t);

  /**
   * Log a message at the WARN level.
   *
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void warn(final @NotNull Component msg);

  /**
   * Log a message at the WARN level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the WARN level. </p>
   *
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void warn(final @NotNull Component format, final @Nullable Object arg);

  /**
   * Log a message at the WARN level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the WARN level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for WARN. The variants taking
   * {@link #warn(Component, Object) one} and {@link #warn(Component, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.</p>
   *
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void warn(final @NotNull Component format, final @Nullable Object@NotNull... arguments);

  /**
   * Log a message at the WARN level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the WARN level. </p>
   *
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void warn(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * Log an exception (throwable) at the WARN level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void warn(final @NotNull Component msg, final @NotNull Throwable t);

  /**
   * Log a message with the specific final @NotNull Marker at the WARN level.
   *
   * @param marker The marker specific to this log statement
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void warn(final @NotNull Marker marker, final @NotNull Component msg);

  /**
   * This method is similar to {@link #warn(Component, Object)} method except that the
   * marker data is also taken into consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void warn(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg);

  /**
   * This method is similar to {@link #warn(Component, Object, Object)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void warn(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * This method is similar to {@link #warn(Component, Object...)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void warn(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object@NotNull... arguments);

  /**
   * This method is similar to {@link #warn(Component, Throwable)} method
   * except that the marker data is also taken into consideration.
   *
   * @param marker the marker data for this log statement
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void warn(final @NotNull Marker marker, final @NotNull Component msg, final @NotNull Throwable t);

  /**
   * Log a message at the ERROR level.
   *
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void error(final @NotNull Component msg);

  /**
   * Log a message at the ERROR level according to the specified format
   * and argument.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the ERROR level.</p>
   *
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void error(final @NotNull Component format, final @Nullable Object arg);

  /**
   * Log a message at the ERROR level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous object creation when the logger
   * is disabled for the ERROR level.</p>
   *
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void error(final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * Log a message at the ERROR level according to the specified format
   * and arguments.
   *
   * <p>This form avoids superfluous string concatenation when the logger
   * is disabled for the ERROR level. However, this variant incurs the hidden
   * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
   * even if this logger is disabled for ERROR. The variants taking
   * {@link #error(Component, Object) one} and {@link #error(Component, Object, Object) two}
   * arguments exist solely in order to avoid this hidden cost.</p>
   *
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void error(final @NotNull Component format, final @Nullable Object@NotNull... arguments);

  /**
   * Log an exception (throwable) at the ERROR level with an
   * accompanying message.
   *
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void error(final @NotNull Component msg, final @NotNull Throwable t);

  /**
   * Log a message with the specific final @NotNull Marker at the ERROR level.
   *
   * @param marker The marker specific to this log statement
   * @param msg the message string to be logged
   * @since 4.11.0
   */
  void error(final @NotNull Marker marker, final @NotNull Component msg);

  /**
   * This method is similar to {@link #error(Component, Object)} method except that the
   * marker data is also taken into consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg the argument
   * @since 4.11.0
   */
  void error(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg);

  /**
   * This method is similar to {@link #error(Component, Object, Object)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arg1 the first argument
   * @param arg2 the second argument
   * @since 4.11.0
   */
  void error(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object arg1, final @Nullable Object arg2);

  /**
   * This method is similar to {@link #error(Component, Object...)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param format the format string
   * @param arguments a list of 3 or more arguments
   * @since 4.11.0
   */
  void error(final @NotNull Marker marker, final @NotNull Component format, final @Nullable Object@NotNull... arguments);

  /**
   * This method is similar to {@link #error(Component, Throwable)}
   * method except that the marker data is also taken into
   * consideration.
   *
   * @param marker the marker data specific to this log statement
   * @param msg the message accompanying the exception
   * @param t the exception (throwable) to log
   * @since 4.11.0
   */
  void error(final @NotNull Marker marker, final @NotNull Component msg, final @NotNull Throwable t);
}
