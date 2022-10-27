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

import java.util.function.Supplier;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;

final class NoOpComponentLoggingEventBuilderImpl implements ComponentLoggingEventBuilder {
  static final NoOpComponentLoggingEventBuilderImpl INSTANCE = new NoOpComponentLoggingEventBuilderImpl();

  private NoOpComponentLoggingEventBuilderImpl() {
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder setCause(final @Nullable Throwable cause) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder addMarker(final @NotNull Marker marker) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder addArgument(final @Nullable Object p) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder addArgument(final @Nullable Supplier<?> objectSupplier) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder addKeyValue(final @Nullable String key, final @Nullable Object value) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder addKeyValue(final @Nullable String key, final Supplier<Object> valueSupplier) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder setMessage(final @Nullable String message) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder setMessage(final @Nullable ComponentLike message) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder setMessage(final @NotNull Supplier<String> messageSupplier) {
    return this;
  }

  @Override
  public @NotNull ComponentLoggingEventBuilder setComponentMessage(final @NotNull Supplier<? extends ComponentLike> messageSupplier) {
    return this;
  }

  @Override
  public void log() {
  }

  @Override
  public void log(final String message) {
  }

  @Override
  public void log(final String message, final Object arg) {
  }

  @Override
  public void log(final String message, final Object arg0, final Object arg1) {
  }

  @Override
  public void log(final String message, final Object... args) {
  }

  @Override
  public void log(final Supplier<String> messageSupplier) {
  }

  @Override
  public void log(final @Nullable ComponentLike message) {
  }

  @Override
  public void log(final @Nullable ComponentLike message, final @Nullable Object arg) {
  }

  @Override
  public void log(final @Nullable ComponentLike message, final @Nullable Object arg0, final @Nullable Object arg1) {
  }

  @Override
  public void log(final @Nullable ComponentLike message, final @Nullable Object @NotNull ... args) {
  }

  @Override
  public void logComponent(final @NotNull Supplier<? extends @Nullable ComponentLike> messageSupplier) {
  }
}
