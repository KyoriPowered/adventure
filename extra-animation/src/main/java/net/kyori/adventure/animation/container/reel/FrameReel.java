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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.animation.container.FrameContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

/**
 * Basic frame reel.
 *
 * @param <F> frame type
 *
 * @since 1.10.0
 */
public interface FrameReel<F> extends GenericFrameReel<F, FrameReel<F>, FrameReelBuilder<F>> {

  /**
   * Reel factory of {@link FrameReel}.
   *
   * @param <F> frame type
   * @return reel factory
   *
   * @since 1.10.0
   */
  static <F> ReelFactory<F, FrameReel<F>> reelFactory() {
    return FrameReel::reel;
  }

  /**
   * Creates {@link FrameReel}.
   *
   * @param frames frame array
   * @param <F> frame type
   * @return created reel
   *
   * @since 1.10.0
   */
  @SafeVarargs
  static <F> FrameReel<F> reel(F... frames) {
    return new FrameReelImpl<F>(Arrays.asList(frames));
  }

  /**
   * Creates {@link FrameReel}.
   *
   * @param frames frame list
   * @param <F> frame type
   * @return created reel
   *
   * @since 1.10.0
   */
  static <F> FrameReel<F> reel(List<F> frames) {
    return new FrameReelImpl<F>(frames);
  }

  /**
   * Creates {@link FrameReel}.
   *
   * @param container frame container
   * @param <F> frame type
   * @return created reel
   *
   * @since 1.10.0
   */
  static <F> FrameReel<F> reel(FrameContainer<F> container) {
    return new FrameReelImpl<F>(container.frames());
  }

  /**
   * Creates {@link FrameReel} by appending subContainers one by one.
   *
   * @param subContainers frame array
   * @param <F> frame type
   * @return created reel
   *
   * @since 1.10.0
   */
  @SafeVarargs
  static <F> FrameReel<F> reel(FrameContainer<F>... subContainers) {
    final FrameReelBuilder<F> builder = new FrameReelImpl<F>(Collections.emptyList()).toBuilder();

    for (final FrameContainer<F> subContainer : subContainers)
      builder.append(subContainer);

    return builder.build();
  }

  /**
   * Creates empty {@link FrameReel}.
   *
   * @param <F> frame type
   * @return created reel
   *
   * @since 1.10.0
   */
  static <F> FrameReel<F> emptyReel() {
    return new FrameReelImpl<F>(Collections.emptyList());
  }

  /**
   * Creates new builder of {@link FrameReel}.
   *
   * @param <F> frame type
   * @return created builder
   *
   * @since 1.10.0
   */
  static <F> FrameReelBuilder<F> reelBuilder() {
    return new FrameReelImpl.BuilderImpl<>(Collections.emptyList());
  }

  /**
   * Creates new builder of {@link FrameReel}.
   *
   * @param first first frame
   * @param <F> frame type
   * @return created builder
   *
   * @since 1.10.0
   */
  static <F> FrameReelBuilder<F> reelBuilder(F first) {
    return new FrameReelImpl.BuilderImpl<F>(Collections.emptyList()).append(first);
  }

  /**
   * Reel factory of {@link ComponentReel}.
   *
   * @return reel factory
   *
   * @since 1.10.0
   */
  static ReelFactory<Component, ComponentReel> componentReelFactory() {
    return FrameReel::component;
  }

  /**
   * Creates {@link ComponentReel}.
   *
   * @param frames frame array
   * @return created reel
   *
   * @since 1.10.0
   */
  static ComponentReel component(Component... frames) {
    return component(reel(frames));
  }

  /**
   * Creates {@link ComponentReel}.
   *
   * @param container frame container
   * @return created reel
   *
   * @since 1.10.0
   */
  static ComponentReel component(FrameContainer<Component> container) {
    return new ComponentReelImpl(container.frames());
  }

  /**
   * Creates {@link ComponentReel} of component likes.
   *
   * @param frames component likes to convert to frames
   * @return created reel
   *
   * @since 1.10.0
   */
  static ComponentReel component(ComponentLike... frames) {
    return component(ComponentLike.asComponents(Arrays.asList(frames)));
  }

  /**
   * Creates {@link ComponentReel}.
   *
   * @param frames frame list
   * @return created reel
   *
   * @since 1.10.0
   */
  static ComponentReel component(List<Component> frames) {
    return component(reel(frames));
  }

  /**
   * Creates {@link ComponentReel}.
   *
   * @param subContainers subcontainers
   * @return created reel
   *
   * @since 1.10.0
   */
  @SafeVarargs
  static ComponentReel component(FrameContainer<Component>... subContainers) {
    return component(reel(subContainers));
  }

  /**
   * Creates empty {@link ComponentReel}.
   *
   * @return created reel
   *
   * @since 1.10.0
   */
  static ComponentReel emptyComponent() {
    return new ComponentReelImpl(Collections.emptyList());
  }

  /**
   * Creates new {@link ComponentReelBuilder}.
   *
   * @return created builder
   *
   * @since 1.10.0
   */
  static ComponentReelBuilder componentReelBuilder() {
    return new ComponentReelImpl.BuilderImpl(Collections.emptyList());
  }

}
