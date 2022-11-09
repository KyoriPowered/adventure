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

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.CheckReturnValue;
import org.slf4j.spi.DefaultLoggingEventBuilder;
import org.slf4j.spi.LoggingEventAware;

final class ComponentLoggingEventBuilderImpl extends DefaultLoggingEventBuilder implements ComponentLoggingEventBuilder {

  private static final String FQCN = ComponentLoggingEventBuilderImpl.class.getName();

  private final Function<Component, String> serializer;

  ComponentLoggingEventBuilderImpl(final Logger logger, final Level level, final Function<Component, String> serializer) {
    super(logger, level);
    this.serializer = serializer;
  }

  private String serialize(final ComponentLike component) {
    return component == null ? null : this.serializer.apply(component.asComponent());
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

  @Override
  public @NotNull ComponentLoggingEventBuilder setMessage(final ComponentLike message) {
    super.setMessage(this.serialize(message));
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder setComponentMessage(final @NotNull Supplier<? extends ComponentLike> messageSupplier) {
    super.setMessage(this.serialize(messageSupplier.get()));
    return this;
  }

  @Override
  public void log(final ComponentLike message) {
    this.loggingEvent.setMessage(this.serialize(message));
    this.logOwnBoundary(this.loggingEvent);
  }

  /**
   * Set the message with one argument and publish this logging event.
   *
   * @param message the message
   * @param arg     the argument
   * @see #log()
   * @since 4.12.0
   */
  @Override
  public void log(final ComponentLike message, final Object arg) {
    this.loggingEvent.setMessage(this.serialize(message));
    this.loggingEvent.addArgument(this.maybeSerialize(arg));
    this.logOwnBoundary(this.loggingEvent);
  }

  /**
   * Set the message with two arguments and publish this logging event.
   *
   * @param message the message
   * @param arg0    the first argument
   * @param arg1    the second argument
   * @see #log()
   * @since 4.12.0
   */
  @Override
  public void log(final ComponentLike message, final Object arg0, final Object arg1) {
    this.loggingEvent.setMessage(this.serialize(message));
    this.loggingEvent.addArgument(this.maybeSerialize(arg0));
    this.loggingEvent.addArgument(this.maybeSerialize(arg1));
    this.logOwnBoundary(this.loggingEvent);
  }

  /**
   * Set the message with an array of arguments and publish this logging event.
   *
   * @param message the message
   * @param args    the arguments
   * @see #log()
   * @since 4.12.0
   */
  @Override
  public void log(final ComponentLike message, final Object @NotNull ... args) {
    this.loggingEvent.setMessage(this.serialize(message));
    this.loggingEvent.addArguments(this.maybeSerialize(args));
    this.logOwnBoundary(this.loggingEvent);
  }

  /**
   * Set the message supplier and publish this logging event.
   *
   * @param messageSupplier the message supplier
   * @see #log()
   * @since 4.12.0
   */
  @Override
  public void logComponent(final @NotNull Supplier<? extends ComponentLike> messageSupplier) {
    this.log(messageSupplier.get());
  }

  // Impl junk

  private void logOwnBoundary(final LoggingEvent event) {
    this.setCallerBoundary(FQCN);
    if (this.logger instanceof LoggingEventAware) {
      ((LoggingEventAware) this.logger).log(event);
    } else {
      super.log(event);
    }
  }

  // Overloads for direct-log methods
  @Override
  public void log(final String message, final Object arg) {
    this.loggingEvent.setMessage(message);
    this.loggingEvent.addArgument(this.maybeSerialize(arg));
    this.logOwnBoundary(this.loggingEvent);
  }

  @Override
  public void log(final String message, final Object arg0, final Object arg1) {
    this.loggingEvent.setMessage(message);
    this.loggingEvent.addArgument(this.maybeSerialize(arg0));
    this.loggingEvent.addArgument(this.maybeSerialize(arg1));
    this.logOwnBoundary(this.loggingEvent);
  }

  @Override
  public void log(final String message, final Object... args) {
    this.loggingEvent.setMessage(message);
    this.loggingEvent.addArguments(this.maybeSerialize(args));
    this.logOwnBoundary(this.loggingEvent);
  }

  // Overloads for return types
  @Override
  @CheckReturnValue
  public @NotNull ComponentLoggingEventBuilder setCause(final @Nullable Throwable cause) {
    super.setCause(UnpackedComponentThrowable.unpack(cause, this.serializer));
    return this;
  }

  @Override
  @CheckReturnValue
  public @NotNull ComponentLoggingEventBuilder addMarker(final @NotNull Marker marker) {
    super.addMarker(marker);
    return this;
  }

  @Override
  @CheckReturnValue
  public @NotNull ComponentLoggingEventBuilder addArgument(final @Nullable Object p) {
    super.addArgument(this.maybeSerialize(p));
    return this;
  }

  @Override
  @CheckReturnValue
  public @NotNull ComponentLoggingEventBuilder addArgument(final @Nullable Supplier<?> objectSupplier) {
    super.addArgument(objectSupplier == null ? null : this.maybeSerialize(objectSupplier.get()));
    return this;
  }

  @Override
  @CheckReturnValue
  public @NotNull ComponentLoggingEventBuilder addKeyValue(final @Nullable String key, final @Nullable Object value) {
    super.addKeyValue(key, this.maybeSerialize(value));
    return this;
  }

  @Override
  @CheckReturnValue
  public @NotNull ComponentLoggingEventBuilder addKeyValue(final @Nullable String key, final Supplier<Object> valueSupplier) {
    super.addKeyValue(key, this.maybeSerialize(valueSupplier.get()));
    return this;
  }

  @Override
  @CheckReturnValue
  public @NotNull ComponentLoggingEventBuilder setMessage(final @Nullable String message) {
    super.setMessage(message);
    return this;
  }

  @Override
  @CheckReturnValue
  public @NotNull ComponentLoggingEventBuilder setMessage(final @Nullable Supplier<String> messageSupplier) {
    super.setMessage(messageSupplier);
    return this;
  }
}
