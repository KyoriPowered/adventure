/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.waypoint;

import java.util.List;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.ApiStatus;

/**
 * {@link Waypoint} internal implementation.
 *
 * @since 5.1.0
 */
@ApiStatus.Internal
public interface WaypointImplementation {

  /**
   * Gets an implementation, and casts it to {@code type}.
   *
   * @param waypoint the waypoint
   * @param type the implementation type
   * @param <I> the implementation type
   * @return a {@code I}
   * @since 5.1.0
   */
  @ApiStatus.Internal
  static <I extends WaypointImplementation> I get(final Waypoint waypoint, final Class<I> type) {
    return WaypointImpl.ImplementationAccessor.get(waypoint, type);
  }

  /**
   * Gets viewers of this waypoint.
   *
   * @return the viewers of this waypoint
   * @since 5.1.0
   */
  @ApiStatus.Internal
  default Iterable<? extends Audience> viewers() {
    return List.of();
  }

  /**
   * A {@link WaypointImplementation} service provider.
   *
   * @since 5.1.0
   */
  @ApiStatus.Internal
  interface Provider {
    /**
     * Gets an implementation.
     *
     * @param waypoint the waypoint
     * @return a {@code I}
     * @since 5.1.0
     */
    @ApiStatus.Internal
    WaypointImplementation create(final Waypoint waypoint);
  }

}
