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
package net.kyori.adventure.animation.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract frame container.
 *
 * @param <F> frame type
 *
 * @since 1.10.0
 */
public abstract class AbstractFrameContainer<F> implements FrameContainer<F> {

  private final List<F> frames;

  protected AbstractFrameContainer(final List<F> frames) {
    this.frames = Collections.unmodifiableList(new ArrayList<>(frames));
  }

  protected AbstractFrameContainer(final List<F> frames, final boolean copyFrameList) {
    if (copyFrameList)
      this.frames = Collections.unmodifiableList(new ArrayList<>(frames));
    else
      this.frames = Collections.unmodifiableList(frames);
  }

  protected AbstractFrameContainer(final F[] frames) {
    this(Arrays.asList(frames));
  }

  @Override
  public List<F> frames() {
    return this.frames;
  }

  @Override
  public String toString() {
    return '[' + String.join(",", this.frames().stream().map(Object::toString).collect(Collectors.toList())) + ']';
  }

}
