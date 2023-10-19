/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.bossbar;

import java.util.Collections;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * {@link BossBar} internal implementation.
 *
 * @since 4.12.0
 */
@ApiStatus.Internal
@NullMarked
public interface BossBarImplementation {
  /**
   * Gets an implementation, and casts it to {@code type}.
   *
   * @param bar the bossbar
   * @param type the implementation type
   * @param <I> the implementation type
   * @return a {@code I}
   * @since 4.12.0
   */
  @ApiStatus.Internal
  static <I extends BossBarImplementation> I get(final BossBar bar, final Class<I> type) {
    return BossBarImpl.ImplementationAccessor.get(bar, type);
  }

  /**
   * Gets the viewers of this bossbar.
   *
   * @return the viewers of this bossbar
   * @since 4.14.0
   */
  @ApiStatus.Internal
  default Iterable<? extends BossBarViewer> viewers() {
    return Collections.emptyList();
  }

  /**
   * A {@link BossBarImplementation} service provider.
   *
   * @since 4.12.0
   */
  @ApiStatus.Internal
  interface Provider {
    /**
     * Gets an implementation.
     *
     * @param bar the bossbar
     * @return a {@code I}
     * @since 4.12.0
     */
    @ApiStatus.Internal
    BossBarImplementation create(final BossBar bar);
  }
}
