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
package net.kyori.adventure.animation.container.reel;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

/**
 * The builder of {@link ComponentReel}.
 *
 * @since 1.10.0
 */
public interface ComponentReelBuilder extends GenericFrameReelBuilder<Component, ComponentReel, ComponentReelBuilder> {

  /**
   * Appends a component like.
   *
   * @param componentLike the component like to append
   * @return this instance
   *
   * @since 1.10.0
   */
  ComponentReelBuilder append(ComponentLike componentLike);

  /**
   * Appends specified number of empty component frames.
   *
   * @param number the number of frames to append
   * @return this instance
   *
   * @since 1.10.0
   */
  ComponentReelBuilder appendEmpties(int number);

}
