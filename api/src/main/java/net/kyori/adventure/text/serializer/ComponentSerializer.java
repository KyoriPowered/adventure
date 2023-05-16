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
package net.kyori.adventure.text.serializer;

import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Component} serializer and deserializer.
 *
 * @param <I> the input component type
 * @param <O> the output component type
 * @param <R> the serialized type
 * @since 4.0.0
 */
public interface ComponentSerializer<I extends Component, O extends Component, R> extends ComponentEncoder<I, R> {
  /**
   * Deserialize a component from input of type {@code R}.
   *
   * @param input the input
   * @return the component
   * @since 4.0.0
   */
  @NotNull O deserialize(final @NotNull R input);

  /**
   * Deserialize a component from input of type {@code R}.
   *
   * <p>If {@code input} is {@code null}, then {@code null} will be returned.</p>
   *
   * @param input the input
   * @return the component if {@code input} is non-null, otherwise {@code null}
   * @since 4.7.0
   * @deprecated for removal since 4.8.0, use {@link #deserializeOrNull(Object)} instead.
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Contract(value = "!null -> !null; null -> null", pure = true)
  @Deprecated
  default @Nullable O deseializeOrNull(final @Nullable R input) {
    return this.deserializeOrNull(input);
  }

  /**
   * Deserialize a component from input of type {@code R}.
   *
   * <p>If {@code input} is {@code null}, then {@code null} will be returned.</p>
   *
   * @param input the input
   * @return the component if {@code input} is non-null, otherwise {@code null}
   * @since 4.8.0
   */
  @Contract(value = "!null -> !null; null -> null", pure = true)
  default @Nullable O deserializeOrNull(final @Nullable R input) {
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
  default @Nullable O deserializeOr(final @Nullable R input, final @Nullable O fallback) {
    if (input == null) return fallback;

    return this.deserialize(input);
  }

  /**
   * Serializes a component into an output of type {@code R}.
   *
   * @param component the component
   * @return the output
   * @since 4.0.0
   */
  @Override
  @NotNull R serialize(final @NotNull I component);

  /**
   * Serializes a component into an output of type {@code R}.
   *
   * <p>If {@code component} is {@code null}, then {@code null} will be returned.</p>
   *
   * @param component the component
   * @return the output if {@code component} is non-null, otherwise {@code null}
   * @since 4.7.0
   */
  @Override
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
   * @since 4.7.0
   */
  @Override
  @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
  default @Nullable R serializeOr(final @Nullable I component, final @Nullable R fallback) {
    if (component == null) return fallback;

    return this.serialize(component);
  }

  /**
   *
   *
   * @since 4.14.0
   */
  interface Builder {

    UnaryOperator<String> DEFAULT_NO_OP = UnaryOperator.identity();
    UnaryOperator<Component> DEFAULT_COMPACTING_METHOD = Component::compact;

    /**
     * Specify a function that takes the component at the end of the parser process.
     * <p>By default, this compacts the resulting component with {@link Component#compact()}.</p>
     *
     * @param postProcessor method run at the end of parsing
     * @return this builder
     * @since 4.14.0
     */
    @NotNull Builder postProcessor(final @NotNull UnaryOperator<Component> postProcessor);

    /**
     * Specify a function that takes the string at the start of the parser process.
     * <p>By default, this does absolutely nothing.</p>
     *
     * @param preProcessor method run at the start of parsing
     * @return this builder
     * @since 4.14.0
     */
    @NotNull Builder preProcessor(final @NotNull UnaryOperator<String> preProcessor);
  }
}
