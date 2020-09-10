/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.audience;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An audience that wraps a collection.
 *
 * @since 4.0.0
 */
public class MutableForwardingAudience implements ForwardingAudience {
  private final Collection<Audience> audiences;

  /**
   * Creates an audience, using an {@link ArrayList} as the backing collection.
   *
   * @return an audience
   * @since 4.0.0
   */
  public static @NonNull MutableForwardingAudience arrayList() {
    return new MutableForwardingAudience(new ArrayList<>());
  }

  /**
   * Creates an audience, using a {@link HashSet} as the backing collection.
   *
   * @return an audience
   * @since 4.0.0
   */
  public static @NonNull MutableForwardingAudience hashSet() {
    return new MutableForwardingAudience(new HashSet<>());
  }

  /**
   * Creates.
   *
   * @param audiences the collection
   * @since 4.0.0
   */
  public MutableForwardingAudience(final @NonNull Collection<Audience> audiences) {
    this.audiences = audiences;
  }

  @Override
  public @NonNull Collection<Audience> audiences() {
    return this.audiences;
  }
}
