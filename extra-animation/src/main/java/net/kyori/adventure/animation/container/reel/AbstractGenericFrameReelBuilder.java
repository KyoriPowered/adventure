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
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.animation.container.FrameContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract generic frame reel builder.
 *
 * @deprecated internal use
 *
 * @param <F> frame type
 * @param <R> reel type
 * @param <B> reel builder type
 *
 * @since 1.10.0
 */
public abstract class AbstractGenericFrameReelBuilder<F, R extends GenericFrameReel<F, R, B>, B extends GenericFrameReelBuilder<F, R, B>> implements GenericFrameReelBuilder<F, R, B> {

  private final List<F> frames;

  private final Function<List<F>, R> reelFactory;

  protected AbstractGenericFrameReelBuilder(final List<F> frames, final Function<List<F>, R> reelFactory) {
    this.frames = new LinkedList<>(frames);
    this.reelFactory = reelFactory;
  }

  protected abstract B instance();

  @Override
  public B append(final F frame) {
    this.frames.add(frame);
    return this.instance();
  }

  @Override
  public B append(final FrameContainer<F> frameContainer) {
    this.frames.addAll(frameContainer.frames());
    return this.instance();
  }

  @Override
  public B repeatLastFrame(final int times) {
    final F lastFrame = this.frames().get(this.frames().size() - 1);

    for (int i = 0; i < times; i++) this.frames.add(lastFrame);
    return this.instance();
  }

  @Override
  public B repeatAllFrames(final int times) {
    final List<F> frames = new ArrayList<>(this.frames);

    for (int i = 0; i < times; i++) this.frames.addAll(frames);
    return this.instance();
  }

  @Override
  public List<F> frames() {
    return Collections.unmodifiableList(this.frames);
  }

  protected List<F> framesMutable() {
    return this.frames;
  }

  @Override
  public @NotNull R build() {
    return this.reelFactory.apply(this.frames);
  }

  @Override
  public String toString() {
    return this.frames.toString();
  }

}
