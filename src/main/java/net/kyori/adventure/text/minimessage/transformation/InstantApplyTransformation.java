/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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

import java.util.Deque;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/**
 * A transformation that is applied directly to the parent builder.
 *
 * <p>Rather than normal transformations which are stored and applied to every component,
 * instant transformations are executed the moment the tag is found, and don't have any effect
 * on further components.</p>
 *
 * @since 4.1.0
 */
public abstract class InstantApplyTransformation extends Transformation {

  /**
   * Apply the child transformations to the provided builder.
   *
   * @param parent component to act on
   * @param transformations the stack of transformations that the parser is tracking. can be modified.
   * @since 4.1.0
   */
  public abstract void applyInstant(final TextComponent.Builder parent, final Deque<Transformation> transformations);

  @Override
  public Component apply(final Component component, final TextComponent.Builder parent) {
    return null;
  }
}
