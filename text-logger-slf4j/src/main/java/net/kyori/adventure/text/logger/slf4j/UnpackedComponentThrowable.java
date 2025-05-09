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

import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.ComponentMessageThrowable;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper for exceptions that implement ComponentMessageThrowable.
 */
@SuppressWarnings("OverrideThrowableToString")
final class UnpackedComponentThrowable extends Throwable {
  private static final long serialVersionUID = -1L;

  private final Class<? extends Throwable> backingType;

  static Throwable unpack(final Throwable maybeRich, final Function<Component, String> serializer) {
    if (!(maybeRich instanceof ComponentMessageThrowable)) return maybeRich; // TODO: do we need to unwrap any nested exceptions?

    final @Nullable Component message = ((ComponentMessageThrowable) maybeRich).componentMessage();
    final Throwable cause = maybeRich.getCause() != null ? unpack(maybeRich.getCause(), serializer) : null;
    final Throwable[] suppressed = maybeRich.getSuppressed();

    final UnpackedComponentThrowable ret = new UnpackedComponentThrowable(maybeRich.getClass(), serializer.apply(message), cause);
    ret.setStackTrace(maybeRich.getStackTrace());
    if (suppressed.length > 0) {
      for (int i = 0; i < suppressed.length; i++) {
        ret.addSuppressed(unpack(suppressed[i], serializer));
      }
    }

    return ret;
  }

  private UnpackedComponentThrowable(final Class<? extends Throwable> backingType, final String serializedMessage, final Throwable cause) {
    super(serializedMessage, cause);
    this.backingType = backingType;
  }

  @Override
  public String toString() {
    final String className = this.backingType.getName();
    final String message = this.getMessage();
    return message == null ? className : className + ":" + message;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
