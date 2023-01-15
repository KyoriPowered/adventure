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
package net.kyori.adventure.text.format;

import java.util.Map;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Writes style properties to a mutable object. Used to override some default methods from {@link StyleSetter}
 * with faster alternatives that only work for mutable objects.
 *
 * @param <T> The type implementing this interface e.g. {@link Component}
 * @see StyleSetter
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public interface MutableStyleSetter<T extends MutableStyleSetter<?>> extends StyleSetter<T> {
  /**
   * Sets {@code decorations} to {@link TextDecoration.State#TRUE}.
   *
   * @param decorations the decorations
   * @return a mutable object ({@code T})
   * @since 4.10.0
   */
  @Override
  @Contract("_ -> this")
  @SuppressWarnings("unchecked")
  default @NotNull T decorate(final @NotNull TextDecoration@NotNull... decorations) {
    for (int i = 0, length = decorations.length; i < length; i++) {
      this.decorate(decorations[i]);
    }
    return (T) this;
  }

  /**
   * Sets decorations using the specified {@code decorations} map.
   *
   * <p>If a given decoration does not have a value explicitly set, the value of that particular decoration is not changed.</p>
   *
   * @param decorations a map containing text decorations and their respective state.
   * @return a mutable object ({@code T})
   * @since 4.10.0
   */
  @Override
  @Contract("_ -> this")
  @SuppressWarnings("unchecked")
  default @NotNull T decorations(final @NotNull Map<TextDecoration, TextDecoration.State> decorations) {
    requireNonNull(decorations, "decorations");
    for (final Map.Entry<TextDecoration, TextDecoration.State> entry : decorations.entrySet()) {
      this.decoration(entry.getKey(), entry.getValue());
    }
    return (T) this;
  }

  /**
   * Sets the state of a set of decorations to {@code flag}.
   *
   * @param decorations the decorations
   * @param flag {@code true} if this mutable object should have the decorations, {@code false} if
   *     this mutable object should not have the decorations
   * @return a mutable object ({@code T})
   * @since 4.10.0
   */
  @Override
  @Contract("_, _ -> this")
  @SuppressWarnings("unchecked")
  default @NotNull T decorations(final @NotNull Set<TextDecoration> decorations, final boolean flag) {
    final TextDecoration.State state = TextDecoration.State.byBoolean(flag);
    decorations.forEach(decoration -> this.decoration(decoration, state));
    return (T) this;
  }
}
