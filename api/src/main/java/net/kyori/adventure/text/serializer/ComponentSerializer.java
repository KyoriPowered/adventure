/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.jetbrains.annotations.Contract;

/**
 * A {@link Component} serializer and deserializer.
 *
 * @param <I> the input component type
 * @param <O> the output component type
 * @param <R> the serialized type
 * @since 4.0.0
 */
public interface ComponentSerializer<I extends Component, O extends Component, R> {
  /**
   * Deserialize a component from input of type {@code R}.
   *
   * @param input the input
   * @return the component
   * @since 4.0.0
   */
  @NonNull O deserialize(final @NonNull R input);

  /**
   * Deserialize a component from input of type {@code R}.
   *
   * <p>If {@code input} is {@code null}, then {@code null} will be returned.</p>
   *
   * @param input the input
   * @return the component if {@code input} is non-null, otherwise {@code null}
   * @since 4.7.0
   */
  @Contract(value = "!null -> !null; null -> null", pure = true)
  default @PolyNull O deseializeOrNull(final @PolyNull R input) {
    return this.deserializeOr(input, null);
  }

  /**
   * Deserialize a component from input of type {@code R}.
   *
   * <p>If {@code input} is {@code null}, then {@code fallback} will be returned.</p>
   *
   * @param input the input
   * @param fallback the fallback value
   * @return the component if {@code input} is non-null, otherwise {@code fallback}
   * @since 4.7.0
   */
  @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
  default @PolyNull O deserializeOr(final @Nullable R input, final @PolyNull O fallback) {
    if(input == null) return fallback;

    return this.deserialize(input);
  }

  /**
   * Serializes a component into an output of type {@code R}.
   *
   * @param component the component
   * @return the output
   * @since 4.0.0
   */
  @NonNull R serialize(final @NonNull I component);

  /**
   * Serializes a component into an output of type {@code R}.
   *
   * <p>If {@code component} is {@code null}, then {@code null} will be returned.</p>
   *
   * @param component the component
   * @return the output if {@code component} is non-null, otherwise {@code null}
   * @since 4.7.0
   */
  @Contract(value = "!null -> !null; null -> null", pure = true)
  default @PolyNull R serializeOrNull(final @PolyNull I component) {
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
   * @since 4.7.0
   */
  @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
  default @PolyNull R serializeOr(final @Nullable I component, final @PolyNull R fallback) {
    if(component == null) return fallback;

    return this.serialize(component);
  }
}
