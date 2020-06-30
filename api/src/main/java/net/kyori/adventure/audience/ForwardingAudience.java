/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.audience;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import java.util.function.Consumer;

/**
 * An audience that delegates to another audience.
 */
@FunctionalInterface
public interface ForwardingAudience extends StubAudience {
  /**
   * Gets the delegate audience.
   *
   * @return the audience, or {@code null} to silently drop
   */
  @Nullable Audience audience();

  /**
   * Forwards the given {@code action} onto the delegate audience, and returns the result
   * of calling {@code perform} on the delegate, or an empty audience if {@link #audience()}
   * returned null.
   *
   * @param type the type of audience the action requires
   * @param action the action
   * @param <T> the type of audience
   * @return an audience
   */
  @Override
  default <T extends Audience> @NonNull Audience perform(final @NonNull Class<T> type, final @NonNull Consumer<T> action) {
    final /* @Nullable */ Audience audience = this.audience();
    if(audience == null) {
      return Audience.empty();
    }
    final Audience result = audience.perform(type, action);
    return result == audience ? this : result;
  }
}
