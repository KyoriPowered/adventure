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

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A transformation that is only applied once, rather than being inherited by sibling components.
 *
 * @since 4.1.0
 */
public abstract class OneTimeTransformation extends Transformation {

  /**
   * Apply the transformation based on the provided state.
   *
   * @param current the component being acted on
   * @param parent builder that will accept {@code current} after all other transformations have been executed
   * @param transformations transformations being tracked
   * @return the transformed {@code current}
   * @since 4.1.0
   */
  public abstract Component applyOneTime(final @NonNull Component current, final TextComponent.@NonNull Builder parent, final @NonNull Deque<Transformation> transformations);

  @Override
  public Component apply(final @NonNull Component component, final TextComponent.@NonNull Builder parent) {
    return null;
  }
}
