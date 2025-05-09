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
 * A {@link Component} decoder, which provides deserialization, but without serialization.
 *
 * <p>For both serialization and deserialization, use {@link ComponentSerializer}</p>
 *
 * @param <S> the serialized type
 * @param <O> the output component type
 * @since 4.16.0
 */
public interface ComponentDecoder<S, O extends Component> {
  /**
   * Deserialize a component from input of type {@code S}.
   *
   * @param input the input
   * @return the component
   * @since 4.16.0
   */
  @NotNull O deserialize(final @NotNull S input);

  /**
   * Deserialize a component from input of type {@code S}.
   *
   * <p>If {@code input} is {@code null}, then {@code null} will be returned.</p>
   *
   * @param input the input
   * @return the component if {@code input} is non-null, otherwise {@code null}
   * @since 4.16.0
   */
  @Contract(value = "!null -> !null; null -> null", pure = true)
  default @Nullable O deserializeOrNull(final @Nullable S input) {
    return this.deserializeOr(input, null);
  }

  /**
   * Deserialize a component from input of type {@code S}.
   *
   * <p>If {@code input} is {@code null}, then {@code fallback} will be returned.</p>
   *
   * @param input the input
   * @param fallback the fallback value
   * @return the component if {@code input} is non-null, otherwise {@code fallback}
   * @since 4.16.0
   */
  @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
  default @Nullable O deserializeOr(final @Nullable S input, final @Nullable O fallback) {
    if (input == null) return fallback;

    return this.deserialize(input);
  }
}
