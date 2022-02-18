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
package net.kyori.adventure.animation.container.reel.mixer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.animation.container.reel.GenericFrameReel;
import net.kyori.adventure.animation.container.reel.ReelTransformer;

/**
 * Abstract mono type reel mixer.
 *
 * @deprecated internal usage
 *
 * @param <R> reel type
 * @param <M> mixer type
 * @param <B> mixer builder type
 *
 * @since 1.10.0
 */
public abstract class AbstractMonoTypeReelMixer<R extends GenericFrameReel<?, R, ?>, M extends GenericMonoTypeReelMixer<R, M, B>, B extends GenericMonoTypeReelMixerBuilder<R, M, B>> implements GenericMonoTypeReelMixer<R, M, B> {

  private final List<R> reels;

  private final Function<List<R>, M> instanceFactory;

  private AbstractMonoTypeReelMixer(final ArrayList<R> reels, final Function<List<R>, M> instanceFactory) {
    this.reels = reels;
    this.instanceFactory = instanceFactory;
  }

  protected AbstractMonoTypeReelMixer(final List<R> reels, final Function<List<R>, M> instanceFactory) {
    this(new ArrayList<>(reels), instanceFactory);
  }

  @Override
  public List<R> reels() {
    return Collections.unmodifiableList(this.reels);
  }

  @Override
  public R reel(final int index) {
    return this.reels.get(index);
  }

  @Override
  public M reel(final R reel, final int index) {
    final ArrayList<R> reels = new ArrayList<>(this.reels);

    reels.set(index, reel);
    return this.instanceFactory.apply(reels);
  }

  @Override
  public M transformReel(final int index, final ReelTransformer<R> transformer) {
    return this.reel(transformer.transform(this.reel(index)), index);
  }

  @Override
  public M append(final R reel) {
    final ArrayList<R> reels = new ArrayList<>(this.reels);

    reels.add(reel);
    return this.instanceFactory.apply(reels);
  }

  @Override
  public String toString() {
    return this.reels().toString();
  }

}
