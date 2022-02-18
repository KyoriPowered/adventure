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
package net.kyori.adventure.animation.display;

import net.kyori.adventure.animation.Animation;

class DisplayContextImpl<F, T> implements DisplayContext<F, T> {

  @SuppressWarnings("unchecked")
  static <F, T> DisplayContext<F, T> contextUnchecked(final Animation<?, ?> animation, final AnimationDisplay display, final Object target) {
    return new DisplayContextImpl<>((Animation<F, T>) animation, display, (T) target);
  }

  private final Animation<F, T> animation;

  private final AnimationDisplay display;

  private final T target;

  DisplayContextImpl(final Animation<F, T> animation, final AnimationDisplay display, final T target) {
    this.animation = animation;
    this.display = display;
    this.target = target;
  }

  @Override
  public Animation<F, T> animation() {
    return this.animation;
  }

  @Override
  public AnimationDisplay display() {
    return this.display;
  }

  @Override
  public T target() {
    return this.target;
  }

}
