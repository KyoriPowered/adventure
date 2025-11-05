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

import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.helpers.CheckReturnValue;
import org.slf4j.spi.LoggingEventBuilder;

/**
 * A builder for logging events that accepts {@link Component} messages and arguments.
 *
 * @since 5.0.0
 */
public interface ComponentLoggingEventBuilder extends LoggingEventBuilder {

  @Override
  @CheckReturnValue
  ComponentLoggingEventBuilder setCause(final @Nullable Throwable cause);

  @Override
  @CheckReturnValue
  ComponentLoggingEventBuilder addMarker(final Marker marker);

  @Override
  @CheckReturnValue
  ComponentLoggingEventBuilder addArgument(final @Nullable Object p);

  @Override
  @CheckReturnValue
  ComponentLoggingEventBuilder addArgument(final @Nullable Supplier<?> objectSupplier);

  @Override
  @CheckReturnValue
  ComponentLoggingEventBuilder addKeyValue(final @Nullable String key, final @Nullable Object value);

  @Override
  @CheckReturnValue
  ComponentLoggingEventBuilder addKeyValue(final @Nullable String key, final Supplier<Object> valueSupplier);

  @Override
  @CheckReturnValue
  ComponentLoggingEventBuilder setMessage(final @Nullable String message);

  /**
   * Set the message to be logged for this event.
   *
   * @param message the message
   * @return this builder
   * @since 5.0.0
   */
  @SuppressWarnings("checkstyle:MethodName") // match overloads
  @CheckReturnValue
  ComponentLoggingEventBuilder setMessage(final @Nullable ComponentLike message);

  @Override
  @CheckReturnValue
  ComponentLoggingEventBuilder setMessage(final Supplier<@Nullable String> messageSupplier);

  /**
   * Set the message supplier to be logged for this event.
   *
   * @param messageSupplier the message supplier
   * @return this builder
   * @since 5.0.0
   */
  @SuppressWarnings("checkstyle:MethodName") // match overloads
  @CheckReturnValue
  ComponentLoggingEventBuilder setComponentMessage(final Supplier<? extends @Nullable ComponentLike> messageSupplier);

  /**
   * Set the message and publish this logging event.
   *
   * @param message the message
   * @see #log()
   * @since 5.0.0
   */
  void log(final @Nullable ComponentLike message);

  /**
   * Set the message with one argument and publish this logging event.
   *
   * @param message the message
   * @param arg the argument
   * @see #log()
   * @since 5.0.0
   */
  void log(final @Nullable ComponentLike message, final @Nullable Object arg);

  /**
   * Set the message with two arguments and publish this logging event.
   *
   * @param message the message
   * @param arg0 the first argument
   * @param arg1 the second argument
   * @see #log()
   * @since 5.0.0
   */
  void log(final @Nullable ComponentLike message, final @Nullable Object arg0, final @Nullable Object arg1);

  /**
   * Set the message with an array of arguments and publish this logging event.
   *
   * @param message the message
   * @param args the arguments
   * @see #log()
   * @since 5.0.0
   */
  void log(final @Nullable ComponentLike message, @Nullable Object... args);

  /**
   * Set the message supplier and publish this logging event.
   *
   * @param messageSupplier the message supplier
   * @see #log()
   * @since 5.0.0
   */
  void logComponent(final Supplier<? extends @Nullable ComponentLike> messageSupplier);
}
