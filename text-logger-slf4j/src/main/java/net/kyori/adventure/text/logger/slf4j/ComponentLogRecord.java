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

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.event.Level;

@ApiStatus.Internal
public final class ComponentLogRecord {
  private final @NotNull Level level;
  private final @Nullable Marker marker;
  private final @Nullable Component componentFormatOrMessage;
  private final @Nullable String stringFormatOrMessage;
  private final @Nullable Object[] arguments;
  private final @Nullable Throwable throwable;

  public ComponentLogRecord(
    final @NotNull Level level,
    final @Nullable Marker marker,
    final @Nullable Component componentFormatOrMessage,
    final @Nullable String stringFormatOrMessage,
    final @Nullable Object[] arguments,
    final @Nullable Throwable throwable
  ) {
    this.level = level;
    this.marker = marker;
    this.componentFormatOrMessage = componentFormatOrMessage;
    this.stringFormatOrMessage = stringFormatOrMessage;
    this.arguments = arguments;
    this.throwable = throwable;
  }

  public @NotNull Level level() {
    return this.level;
  }

  public @Nullable Marker marker() {
    return this.marker;
  }

  public @Nullable Component componentFormatOrMessage() {
    return this.componentFormatOrMessage;
  }

  public @Nullable String stringFormatOrMessage() {
    return this.stringFormatOrMessage;
  }

  public @Nullable Object[] arguments() {
    return this.arguments;
  }

  public @Nullable Throwable throwable() {
    return this.throwable;
  }
}
