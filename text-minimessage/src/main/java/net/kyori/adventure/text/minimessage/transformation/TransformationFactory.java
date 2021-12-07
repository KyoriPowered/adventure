/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage.transformation;

import java.util.List;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;

/**
 * A supplier of new transformation instances.
 *
 * @param <T> the transformation type
 * @since 4.2.0
 */
@FunctionalInterface
public interface TransformationFactory<T extends Transformation> {
  /**
   * Produce a new instance of the transformation given a specific tag.
   *
   * @param ctx the parse context
   * @param name the tag name
   * @param args an unmodifiable list of tag arguments, may be empty
   * @return the new instance
   * @since 4.2.0
   */
  T parse(final Context ctx, final String name, final List<TagPart> args);

  /**
   * A variant of a transformation factory that doesn't take a context object.
   *
   * @param <T> the transformation type
   * @since 4.2.0
   */
  @FunctionalInterface
  interface ContextFree<T extends Transformation> extends TransformationFactory<T> {
    @Override
    default T parse(final Context ctx, final String name, final List<TagPart> args) {
      return this.parse(name, args);
    }

    /**
     * Produce a new instance of the transformation given a specific tag.
     *
     * @param name the tag name
     * @param args an unmodifiable list of tag arguments, may be empty
     * @return the new instance
     * @since 4.2.0
     */
    T parse(final String name, final List<TagPart> args);
  }
}
