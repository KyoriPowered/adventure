/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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
package net.kyori.text.serializer;

import net.kyori.text.Component;

import javax.annotation.Nonnull;

/**
 * A {@link Component} serializer and deserializer.
 *
 * @param <I> the input component type
 * @param <O> the output component type
 * @param <R> the serialized type
 */
public interface ComponentSerializer<I extends Component, O extends Component, R> {

  /**
   * Deserialize a component from input of type {@code R}.
   *
   * @param input the input
   * @return the component
   */
  @Nonnull
  O deserialize(@Nonnull final R input);

  /**
   * Serializes a component into an output of type {@code R}.
   *
   * @param component the component
   * @return the output
   */
  @Nonnull
  R serialize(@Nonnull final I component);
}
