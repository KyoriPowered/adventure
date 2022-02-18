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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import net.kyori.adventure.animation.Animation;
import net.kyori.adventure.animation.channel.AnimationChannel;

/**
 * Display of single animation.
 *
 * @param <F> frame type
 * @param <T> target type
 *
 * @since 1.10.0
 */
public interface MonoAnimationDisplay<F, T> extends AnimationDisplay {

  /**
   * Gets the target.
   *
   * @return target
   *
   * @since 1.10.0
   */
  T target();

  /**
   * Gets the animation channel.
   *
   * @return animation channel
   *
   * @since 1.10.0
   */
  AnimationChannel channel();

  /**
   * Gets the animation.
   *
   * @return animation
   *
   * @since 1.10.0
   */
  Animation<F, T> animation();

  /**
   * Gets the display listeners.
   *
   * @return display listeners
   *
   * @since 1.10.0
   */
  Collection<DisplayListener<F, T>> listeners();

  @Override
  default Map<Object, AnimationChannel> channels() {
    return Collections.singletonMap(this.target(), this.channel());
  }

}
