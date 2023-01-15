/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.util;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * An extension interface for {@link Throwable}s to provide a {@link Component}-based message.
 *
 * @since 4.0.0
 */
public interface ComponentMessageThrowable {
  /**
   * Gets the {@link Component}-based message from a {@link Throwable}, if available.
   *
   * @param throwable the throwable
   * @return the message
   * @since 4.0.0
   */
  @SuppressWarnings("checkstyle:MethodName")
  static @Nullable Component getMessage(final @Nullable Throwable throwable) {
    if (throwable instanceof ComponentMessageThrowable) {
      return ((ComponentMessageThrowable) throwable).componentMessage();
    }
    return null;
  }

  /**
   * Gets the {@link Component}-based message from a {@link Throwable}, or converts {@link Throwable#getMessage()}.
   *
   * @param throwable the throwable
   * @return the message
   * @since 4.0.0
   */
  @SuppressWarnings("checkstyle:MethodName")
  static @Nullable Component getOrConvertMessage(final @Nullable Throwable throwable) {
    if (throwable instanceof ComponentMessageThrowable) {
      return ((ComponentMessageThrowable) throwable).componentMessage();
    } else if (throwable != null) {
      final String message = throwable.getMessage();
      if (message != null) {
        return Component.text(message);
      }
    }
    return null;
  }

  /**
   * Gets the message.
   *
   * @return the message
   * @since 4.0.0
   */
  @Nullable Component componentMessage();
}
