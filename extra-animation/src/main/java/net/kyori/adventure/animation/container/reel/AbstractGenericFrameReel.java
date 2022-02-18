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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.animation.container.AbstractFrameContainer;
import net.kyori.adventure.animation.container.FrameContainer;
import net.kyori.adventure.util.MonkeyBars;
import org.jetbrains.annotations.ApiStatus;

/**
 * Abstract generic frame reel.
 *
 * @deprecated internal usage
 *
 * @param <F> frame type
 * @param <R> reel type
 * @param <B> reel builder type
 *
 * @since 1.10.0
 */
@Deprecated
@ApiStatus.Internal
public abstract class AbstractGenericFrameReel<F, R extends GenericFrameReel<F, R, B>, B extends GenericFrameReelBuilder<F, R, B>> extends AbstractFrameContainer<F> implements GenericFrameReel<F, R, B> {

  private final Function<List<F>, R> instanceFactory;

  protected AbstractGenericFrameReel(final List<F> frames, final Function<List<F>, R> instanceFactory) {
    super(frames);
    this.instanceFactory = instanceFactory;
  }

  protected Function<List<F>, R> instanceFactory() {
    return this.instanceFactory;
  }

  @Override
  public R reversed() {
    final List<F> frames = new ArrayList<>(frames());

    Collections.reverse(frames);

    return this.instanceFactory.apply(frames);
  }

  @Override
  public R append(final F frame) {
    return this.instanceFactory.apply(MonkeyBars.addOne(frames(), frame));
  }

  @Override
  public R append(final FrameContainer<F> frameContainer) {
    final List<F> newFrames = new ArrayList<>(this.frames());

    newFrames.addAll(frameContainer.frames());
    return this.instanceFactory.apply(newFrames);
  }

  @Override
  public R repeatLastFrame(final int times) {
    final List<F> newFrames = new ArrayList<>(this.frames());
    final F lastFrame = this.frames().get(frames().size() - 1);

    for (int i = 0; i < times; i++) newFrames.add(lastFrame);
    return this.instanceFactory.apply(newFrames);
  }

  @Override
  public R repeatAllFrames(final int times) {
    final List<F> newFrames = new ArrayList<>(this.frames());

    for (int i = 0; i < times; i++) newFrames.addAll(this.frames());
    return this.instanceFactory.apply(newFrames);
  }

  @Override
  public R subReel(final int begin, final int end) {
    return this.instanceFactory.apply(frames().subList(begin, end + 1));
  }

  @Override
  public int hashCode() {
    return frames().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof FrameContainer) {
      return ((FrameContainer<?>) obj).frames().equals(frames());
    } else
      return false;
  }

  @Override
  public R copy() {
    return this.instanceFactory.apply(frames());
  }

  @Override
  public R empty() {
    return this.instanceFactory.apply(Collections.emptyList());
  }

  @Override
  public R frames(final List<F> frames) {
    return this.instanceFactory.apply(frames);
  }

  @Override
  public String toString() {
    return frames().toString();
  }

}
