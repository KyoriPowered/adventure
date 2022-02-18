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
package net.kyori.adventure.animation.supplier;

import net.kyori.adventure.animation.container.FrameContainer;

/**
 * A frame supplier that pulls frames from container.
 *
 * @param <F> the frame type
 * @param <C> the container type
 *
 * @since 1.10.0
 */
public interface ContainerFrameSupplier<F, C extends FrameContainer<F>> extends FrameSupplier<F> {

  /**
   * Creates container frame supplier thar pulls frames in exactly same order as in container.
   *
   * @param container container
   * @param <F> frame type
   * @param <C> container type
   * @return created supplier
   *
   * @since 1.10.0
   */
  static <F, C extends FrameContainer<F>> ContainerFrameSupplier<F, C> linear(C container) {
    return new LinearContainerFrameSupplierImpl<>(container);
  }

  /**
   * Creates container frame supplier thar pulls frames in inverted container order. (first is last, last is first)
   *
   * @param container container
   * @param <F> frame type
   * @param <C> container type
   * @return created supplier
   *
   * @since 1.10.0
   */
  static <F, C extends FrameContainer<F>> ContainerFrameSupplier<F, C> invertedLinear(C container) {
    return new InvertedLinearContainerFrameSupplierImpl<>(container);
  }

  /**
   * Creates container frame supplier thar pulls frames in same order as in container, but when it reaches the container end it gets back to loopIndex.
   *
   * @param container container
   * @param loopIndex index after which the loop starts
   * @param <F> frame type
   * @param <C> container type
   * @return created supplier
   *
   * @since 1.10.0
   */
  static <F, C extends FrameContainer<F>> ContainerFrameSupplier<F, C> circular(C container, int loopIndex) {
    return new CircularContainerFrameSupplierImpl<>(container, loopIndex);
  }

  /**
   * Gets the container.
   *
   * @return container
   *
   * @since 1.10.0
   */
  C container();

}
