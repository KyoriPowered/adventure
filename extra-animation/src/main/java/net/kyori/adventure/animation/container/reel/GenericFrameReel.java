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

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.animation.container.FrameContainer;
import net.kyori.adventure.util.Buildable;

/**
 * An extended frame container.
 *
 * @param <F> frame type
 * @param <R> reel type
 * @param <B> reel builder type
 * @since 4.10.0
 */
public interface GenericFrameReel<F, R extends GenericFrameReel<F, R, B>, B extends GenericFrameReelBuilder<F, R, B>> extends FrameContainer<F>, Buildable<R, B> {

  /**
   * Inverts (reverses the order) of frames to a copy of this object.
   *
   * @return the result of described operation
   * @since 4.10.0
   */
  R reversed();

  /**
   * Appends a frame to a copy of this container.
   *
   * @param frame the frame to append
   * @return a new instance with change performed
   * @since 4.10.0
   */
  R append(F frame);

  /**
   * Appends the frames of a component frame container to a copy of this container.
   *
   * @param frameContainer the frame container to append
   * @return a new instance with change performed
   * @since 4.10.0
   */
  R append(FrameContainer<F> frameContainer);

  /**
   * Repeats the last appended frame specified number of times. (frame to repeat excluded)
   *
   * @param times the number of last frame repetitions
   * @return a new instance with change performed
   * @since 4.10.0
   */
  R repeatLastFrame(int times);

  /**
   * Repeats the all frames specified number of times.
   *
   * @param times the number of frames repetitions (frames to repeat excluded)
   * @return a new instance with change performed
   * @since 4.10.0
   */
  R repeatAllFrames(int times);

  /**
   * Creates a sub reel of a fragment of this reel.
   *
   * @param begin index of first element in subreel
   * @param end index of last element in subreel
   * @return created subreel
   * @since 4.10.0
   */
  R subReel(int begin, int end);

  /**
   * Copies reel.
   *
   * @return reel copy
   * @since 4.10.0
   */
  R copy();

  /**
   * Creates empty reel from this reel.
   *
   * @return empty reel
   * @since 4.10.0
   */
  R empty();

  /**
   * Creates reel with this same type but with different frames.
   *
   * @param frames frames
   * @return created reel
   * @since 4.10.0
   */
  R frames(List<F> frames);

  /**
   * Maps reel to another reel.
   *
   * @param mapper frame mapper
   * @param reelFactory reel instance factory
   * @param <NF> new frames type
   * @param <NR> new reel type
   * @return mapped reel
   *
   * @since 1.10.0
   */
  default <NF, NR extends GenericFrameReel<NF, NR, ?>> NR map(final Function<F, NF> mapper, final ReelFactory<NF, NR> reelFactory) {
    final List<NF> list = new LinkedList<>();

    for (final F frame : frames())
      list.add(mapper.apply(frame));

    return reelFactory.produceReel(list);
  }

}
