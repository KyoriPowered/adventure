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
package net.kyori.adventure.text.event;

import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Something that can provide a {@link HoverEvent}.
 *
 * @param <V> the value type
 * @since 4.0.0
 */
public interface HoverEventSource<V> {
  /**
   * Fetches a {@link HoverEvent} from a {@code HoverEventSource}.
   *
   * @param source the hover event source
   * @param <V> the value type
   * @return a hover event, or {@code null}
   * @since 4.0.0
   */
  static <V> @Nullable HoverEvent<V> unbox(final @Nullable HoverEventSource<V> source) {
    return source != null ? source.asHoverEvent() : null;
  }

  /**
   * Represent this object as a hover event.
   *
   * @return a hover event
   * @since 4.0.0
   */
  default @NotNull HoverEvent<V> asHoverEvent() {
    return this.asHoverEvent(UnaryOperator.identity());
  }

  /**
   * Creates a hover event with value derived from this object.
   *
   * <p>The event value will be passed through the provided callback to allow
   * transforming the original value of the event.</p>
   *
   * @param op transformation on value
   * @return a hover event
   * @since 4.0.0
   */
  @NotNull HoverEvent<V> asHoverEvent(final @NotNull UnaryOperator<V> op);
}
