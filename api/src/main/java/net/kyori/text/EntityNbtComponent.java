/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An entity NBT component.
 */
public interface EntityNbtComponent extends NbtComponent<EntityNbtComponent, EntityNbtComponent.Builder>, ScopedComponent<EntityNbtComponent> {
  /**
   * Creates an entity NBT component builder.
   *
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new EntityNbtComponentImpl.BuilderImpl();
  }

  /**
   * Gets the entity selector.
   *
   * @return the entity selector
   */
  @NonNull String selector();

  /**
   * Sets the entity selector.
   *
   * @param selector the entity selector
   * @return a component
   */
  @NonNull EntityNbtComponent selector(final @NonNull String selector);

  /**
   * An entity NBT component builder.
   */
  interface Builder extends NbtComponentBuilder<EntityNbtComponent, Builder> {
    /**
     * Sets the entity selector.
     *
     * @param selector the entity selector
     * @return this builder
     */
    @NonNull Builder selector(final @NonNull String selector);
  }
}
