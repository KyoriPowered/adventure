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
package net.kyori.adventure.text.serializer;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Component} encoder, which provides serialization, but without deserialization.
 *
 * <p>For both serialization and deserialization, use {@link ComponentSerializer}</p>
 *
 * @param <I> the input component type
 * @param <R> the serialized type
 * @since 4.14.0
 */
public interface ComponentEncoder<I extends Component, R> {
  /**
   * Serializes a component into an output of type {@code R}.
   *
   * @param component the component
   * @return the output
   * @since 4.14.0
   */
  @NotNull R serialize(final @NotNull I component);

  /**
   * Serializes a component into an output of type {@code R}.
   *
   * <p>If {@code component} is {@code null}, then {@code null} will be returned.</p>
   *
   * @param component the component
   * @return the output if {@code component} is non-null, otherwise {@code null}
   * @since 4.14.0
   */
  @Contract(value = "!null -> !null; null -> null", pure = true)
  default @Nullable R serializeOrNull(final @Nullable I component) {
    return this.serializeOr(component, null);
  }

  /**
   * Serializes a component into an output of type {@code R}.
   *
   * <p>If {@code component} is {@code null}, then {@code fallback} will be returned.</p>
   *
   * @param component the component
   * @param fallback the fallback value
   * @return the output if {@code component} is non-null, otherwise {@code fallback}
   * @since 4.14.0
   */
  @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
  default @Nullable R serializeOr(final @Nullable I component, final @Nullable R fallback) {
    if (component == null) return fallback;

    return this.serialize(component);
  }
}
