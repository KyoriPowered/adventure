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

import java.util.List;
import org.jetbrains.annotations.NotNull;

class FrameReelImpl<F> extends AbstractGenericFrameReel<F, FrameReel<F>, FrameReelBuilder<F>> implements FrameReel<F> {

  protected FrameReelImpl(final List<F> frames) {
    super(frames, FrameReelImpl::new);
  }

  @Override
  public @NotNull FrameReelBuilder<F> toBuilder() {
    return new BuilderImpl<>(this.frames());
  }

  static class BuilderImpl<F> extends AbstractGenericFrameReelBuilder<F, FrameReel<F>, FrameReelBuilder<F>> implements FrameReelBuilder<F> {

    BuilderImpl(final List<F> frames) {
      super(frames, FrameReelImpl::new);
    }

    @Override
    protected FrameReelBuilder<F> instance() {
      return this;
    }
  }

}
