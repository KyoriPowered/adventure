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

import java.util.Objects;
import net.kyori.adventure.animation.container.FrameContainer;

class CircularContainerFrameSupplierImpl<F, C extends FrameContainer<F>> extends AbstractContainerFrameSupplier<F, C> {

  private final int loopIndex;

  private final int loopSize;

  protected CircularContainerFrameSupplierImpl(final C container, final int loopIndex) {
    super(container);
    if (loopIndex < 0 || loopIndex >= container.size())
      throw new IllegalArgumentException("Loop index must be non-negative and less than container size.");
    this.loopIndex = loopIndex;
    this.loopSize = container.size() - loopIndex;
  }

  @Override
  public F frame(final long index) {
    if (index < 0)
      return null;
    if (index < container().size())
      return Objects.requireNonNull(container().frames().get((int) index), "Frame container has given null frame on index " + index + ".");
    else
      return Objects.requireNonNull(container().frames().get((int) (((index - this.loopIndex) % this.loopSize) + this.loopIndex)), "Frame container has given null frame on index " + index + ".");
  }

}
