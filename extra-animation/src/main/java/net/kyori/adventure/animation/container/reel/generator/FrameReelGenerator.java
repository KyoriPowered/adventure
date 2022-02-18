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

import java.util.function.Function;
import net.kyori.adventure.animation.container.reel.ComponentReel;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.GenericFrameReel;
import net.kyori.adventure.animation.container.reel.ReelFactory;
import net.kyori.adventure.text.Component;

/**
 * An entity that create frame reels basing on its size.
 *
 * @param <R> reel type
 *
 * @since 1.10.0
 */
public interface FrameReelGenerator<R extends GenericFrameReel<?, R, ?>> {

  /**
   * Creates a reel like of reel.
   *
   * @param reel the reel
   * @param <R> type of the reel
   * @return created reel like
   *
   * @since 1.10.0
   */
  static <R extends GenericFrameReel<?, R, ?>> FrameReelLike<R> fromReel(R reel) {
    return new FrameReelLikeImpl<>(reel);
  }

  /**
   * Creates an inflexible generator of reel.
   *
   * @param reel the reel
   * @param <R> type of the reel
   * @return created generator
   *
   * @since 1.10.0
   */
  static <R extends GenericFrameReel<?, R, ?>> InflexibleFrameReelLike<R> literalReel(R reel) {
    return new InflexibleFrameReelLikeImpl<>(reel);
  }

  /**
   * Creates a fully flexible component reel like of component reel.
   *
   * @param reel the reel
   * @return created generator
   *
   * @since 1.10.0
   */
  static ComponentReelLike fromComponentReel(ComponentReel reel) {
    return new ComponentReelLikeImpl(reel);
  }

  /**
   * Creates a frame replicator.
   *
   * @param frame frame to replicate
   * @param reelFactory reel instances factory
   * @param <F> frame type
   * @param <R> reel type
   * @return frame replicator
   *
   * @since 1.10.0
   */
  static <F, R extends GenericFrameReel<F, R, ?>> FrameReplicator<F, R> frameReplicator(F frame, ReelFactory<F, R> reelFactory) {
    return new FrameReplicatorImpl<>(frame, reelFactory);
  }

  /**
   * Creates a frame replicator with reel type of {@link FrameReel}.
   *
   * @param frame frame to replicate
   * @param <F> frame type
   * @return frame replicator
   *
   * @since 1.10.0
   */
  static <F> FrameReplicator<F, FrameReel<F>> frameReplicator(F frame) {
    return new FrameReplicatorImpl<>(frame, FrameReel.reelFactory());
  }

  /**
   * Creates a frame replicator with reel type of {@link ComponentReel}.
   *
   * @param frame frame to replicate
   * @return frame replicator
   *
   * @since 1.10.0
   */
  static FrameReplicator<Component, ComponentReel> componentFrameReplicator(Component frame) {
    return new FrameReplicatorImpl<>(frame, FrameReel.componentReelFactory());
  }

  /**
   * Creates a reel replicator.
   *
   * @param reel reel to replicate
   * @param <R> reel type
   * @return reel replicator
   *
   * @since 1.10.0
   */
  static <R extends GenericFrameReel<?, R, ?>> ReelReplicator<R> reelReplicator(R reel) {
    return new ReelReplicatorImpl<>(reel);
  }

  /**
   * Creates a gradient generator.
   *
   * <p>Read ({@link FrameReelGenerator#createReel(int)} to see how it works.</p>
   *
   * @param frameMapper frame mapper
   * @param reelFactory reel instance factory
   * @param <F> frame type
   * @param <R> reel type
   * @return gradient generator
   *
   * @since 1.10.0
   */
  static <F, R extends GenericFrameReel<F, R, ?>> GradientGenerator<F, R> gradient(Function<Float, F> frameMapper, ReelFactory<F, R> reelFactory) {
    return new GradientGeneratorImpl<>(frameMapper, reelFactory);
  }

  /**
   * Converts this object to frame reel with target size.
   *
   * @param targetSize size of target reel
   * @return created reel
   *
   * @since 1.10.0
   */
  R createReel(int targetSize);

}
