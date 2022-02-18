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
import org.jetbrains.annotations.ApiStatus;

/**
 * Abstract container frame supplier.
 *
 * @deprecated internal usage
 *
 * @param <F> frame type
 * @param <C> container type
 *
 * @since 1.10.0
 */
@Deprecated
@ApiStatus.Internal
public abstract class AbstractContainerFrameSupplier<F, C extends FrameContainer<F>> implements ContainerFrameSupplier<F, C> {

  private final C container;

  protected AbstractContainerFrameSupplier(final C container) {
    this.container = container;
  }

  @Override
  public C container() {
    return this.container;
  }
}
