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
package net.kyori.adventure.text;

import net.kyori.adventure.text.object.ObjectContents;

/**
 * Displays a non-text object.
 *
 * @since 4.25.0
 * @sinceMinecraft 1.21.9
 */
public sealed interface ObjectComponent extends ScopedComponent<ObjectComponent> permits ObjectComponentImpl {
  /**
   * Gets the contents of this object component.
   *
   * @return the contents
   * @since 4.25.0
   */
  ObjectContents contents();

  /**
   * Creates a copy of this object component with the given contents.
   *
   * @param contents the contents to set
   * @return new object component
   * @since 4.25.0
   */
  ObjectComponent contents(ObjectContents contents);

  @Override
  Builder toBuilder();

  /**
   * An object component builder.
   *
   * @since 4.25.0
   */
  sealed interface Builder extends ComponentBuilder<ObjectComponent, Builder> permits ObjectComponentImpl.BuilderImpl {
    /**
     * Sets the contents of this object component builder.
     *
     * @param objectContents the contents to set
     * @return this builder
     * @since 4.25.0
     */
    Builder contents(ObjectContents objectContents);
  }
}
