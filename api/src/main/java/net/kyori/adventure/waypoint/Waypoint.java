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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Contract;

/**
 * Represents a waypoint for a locator bar.
 *
 * @since 5.1.0
 * @sinceMinecraft 1.21.6
 */
public sealed interface Waypoint permits AzimuthWaypoint, ChunkWaypoint, EmptyWaypoint, VectorWaypoint, WaypointImpl {

  /**
   * Creates an empty waypoint.
   *
   * @param color the color
   * @return an empty waypoint
   * @since 5.1.0
   */
  static EmptyWaypoint empty(final TextColor color) {
    return new EmptyWaypointImpl(color);
  }

  /**
   * Creates an empty waypoint.
   *
   * @param style the style
   * @param color the color
   * @return an empty waypoint
   * @since 5.1.0
   */
  static EmptyWaypoint empty(final Key style, final TextColor color) {
    return new EmptyWaypointImpl(style, color);
  }

  /**
   * Creates an azimuth waypoint.
   *
   * @param color the color
   * @param angle the angle
   * @return an azimuth waypoint
   * @since 5.1.0
   */
  static AzimuthWaypoint azimuth(final TextColor color, final float angle) {
    return new AzimuthWaypointImpl(color, angle);
  }

  /**
   * Creates an azimuth waypoint.
   *
   * @param style the style
   * @param color the color
   * @param angle the angle
   * @return an azimuth waypoint
   * @since 5.1.0
   */
  static AzimuthWaypoint azimuth(final Key style, final TextColor color, final float angle) {
    return new AzimuthWaypointImpl(style, color, angle);
  }

  /**
   * Creates a chunk waypoint.
   *
   * @param color the color
   * @param x the X coordinate
   * @param z the Z coordinate
   * @return a chunk waypoint
   * @since 5.1.0
   */
  static ChunkWaypoint chunk(final TextColor color, final int x, final int z) {
    return new ChunkWaypointImpl(color, x, z);
  }

  /**
   * Creates a chunk waypoint.
   *
   * @param style the style
   * @param color the color
   * @param x the X coordinate
   * @param z the Z coordinate
   * @return a chunk waypoint
   * @since 5.1.0
   */
  static ChunkWaypoint chunk(final Key style, final TextColor color, final int x, final int z) {
    return new ChunkWaypointImpl(style, color, x, z);
  }

  /**
   * Creates a vector waypoint.
   *
   * @param color the color
   * @param x the X coordinate
   * @param y the Y coordinate
   * @param z the Z coordinate
   * @return a vector waypoint
   * @since 5.1.0
   */
  static VectorWaypoint vector(final TextColor color, final int x, final int y, final int z) {
    return new VectorWaypointImpl(color, x, y, z);
  }

  /**
   * Creates a vector waypoint.
   *
   * @param style the style
   * @param color the color
   * @param x the X coordinate
   * @param y the Y coordinate
   * @param z the Z coordinate
   * @return a vector waypoint
   * @since 5.1.0
   */
  static VectorWaypoint vector(final Key style, final TextColor color, final int x, final int y, final int z) {
    return new VectorWaypointImpl(style, color, x, y, z);
  }

  /**
   * Gets the style.
   *
   * @return the style
   * @since 5.1.0
   */
  Key style();

  /**
   * Sets the style.
   *
   * @param key the style
   * @return the waypoint
   * @since 5.1.0
   */
  @Contract("_ -> this")
  Waypoint style(final Key key);

  /**
   * Gets the color.
   *
   * @return the color
   * @since 5.1.0
   */
  TextColor color();

  /**
   * Sets the color.
   *
   * @param color the color
   * @return the waypoint
   * @since 5.1.0
   */
  @Contract("_ -> this")
  Waypoint color(final TextColor color);

}
