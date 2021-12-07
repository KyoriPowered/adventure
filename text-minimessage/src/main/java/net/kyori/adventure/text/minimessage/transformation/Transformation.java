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
package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import net.kyori.examination.string.StringExaminer;

/**
 * A transformation that can be applied while parsing a message.
 *
 * <p>A transformation instance is created for each instance of a tag in a parsed string.</p>
 *
 * @see TransformationRegistry to access and register available transformations
 * @since 4.1.0
 */
public abstract class Transformation implements Examinable {

  protected Transformation() {
  }

  /**
   * Return a transformed {@code component} based on the applied parameters.
   *
   * @return the transformed component
   * @since 4.1.0
   */
  public abstract Component apply();

  @Override
  public final String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

  @Override
  public abstract boolean equals(final Object o);

  @Override
  public abstract int hashCode();
}
