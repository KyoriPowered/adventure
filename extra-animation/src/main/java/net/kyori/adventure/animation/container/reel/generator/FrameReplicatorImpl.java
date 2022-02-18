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
package net.kyori.adventure.animation.container.reel.generator;

import java.util.Collections;
import java.util.stream.Stream;
import net.kyori.adventure.animation.container.reel.GenericFrameReel;
import net.kyori.adventure.animation.container.reel.ReelFactory;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

class FrameReplicatorImpl<F, R extends GenericFrameReel<F, R, ?>> implements FrameReplicator<F, R>, Examinable {

  private final F frame;

  private final ReelFactory<F, R> reelFactory;

  FrameReplicatorImpl(final F frame, final ReelFactory<F, R> reelFactory) {
    this.frame = frame;
    this.reelFactory = reelFactory;
  }

  @Override
  public F frame() {
    return this.frame;
  }

  @Override
  public R createReel(final int targetSize) {
    if (targetSize > 0)
      return this.reelFactory.produceReel(Collections.singletonList(this.frame)).repeatLastFrame(targetSize - 1);
    else
      return this.reelFactory.produceReel(Collections.emptyList());
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("frame", this.frame())
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

}
