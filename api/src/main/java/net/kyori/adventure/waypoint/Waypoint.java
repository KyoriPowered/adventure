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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnmodifiableView;

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
   * @param x     the X coordinate
   * @param z     the Z coordinate
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
   * @param x     the X coordinate
   * @param z     the Z coordinate
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
   * @param x     the X coordinate
   * @param y     the Y coordinate
   * @param z     the Z coordinate
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
   * @param x     the X coordinate
   * @param y     the Y coordinate
   * @param z     the Z coordinate
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

  /**
   * Adds a listener to this waypoint.
   *
   * @param listener the listener to add
   * @return this waypoint
   * @since 5.1.0
   */
  @Contract("_ -> this")
  Waypoint addListener(final Waypoint.Listener listener);

  /**
   * Removes a listener from this waypoint.
   *
   * @param listener the listener to remove
   * @return this waypoint
   * @since 5.1.0
   */
  @Contract("_ -> this")
  Waypoint removeListener(final Waypoint.Listener listener);

  /**
   * Gets an unmodifiable view of the audiences currently tracking this waypoint.
   *
   * <p>The returned value may be empty if this method is unsupported.</p>
   *
   * @return an unmodifiable view of the audiences tracking this waypoint
   * @since 5.1.0
   */
  @UnmodifiableView
  Iterable<? extends Audience> viewers();

  /**
   * A listener for changes on a {@link Waypoint}.
   *
   * <p>The platform implements this to react when waypoint properties change
   * or when an audience begins/stops tracking a waypoint.</p>
   *
   * @since 5.1.0
   */
  interface Listener {

    /**
     * Called when the style of a waypoint changes.
     *
     * @param waypoint the waypoint
     * @param oldStyle the old style
     * @param newStyle the new style
     * @since 5.1.0
     */
    default void waypointStyleChanged(final Waypoint waypoint, final Key oldStyle, final Key newStyle) {
    }

    /**
     * Called when the color of a waypoint changes.
     *
     * @param waypoint the waypoint
     * @param oldColor the old color
     * @param newColor the new color
     * @since 5.1.0
     */
    default void waypointColorChanged(final Waypoint waypoint, final TextColor oldColor, final TextColor newColor) {
    }

    /**
     * Called when the angle of an {@link AzimuthWaypoint} changes.
     *
     * @param waypoint the waypoint
     * @param oldAngle the old angle
     * @param newAngle the new angle
     * @since 5.1.0
     */
    default void waypointAngleChanged(final AzimuthWaypoint waypoint, final float oldAngle, final float newAngle) {
    }

    /**
     * Called when the chunk position of a {@link ChunkWaypoint} changes.
     *
     * @param waypoint the waypoint
     * @param oldX the old X
     * @param oldZ the old Z
     * @param newX the new X
     * @param newZ the new Z
     * @since 5.1.0
     */
    default void waypointChunkPositionChanged(
      final ChunkWaypoint waypoint,
      final int oldX, final int oldZ,
      final int newX, final int newZ
    ) {
    }

    /**
     * Called when the coordinates of a {@link VectorWaypoint} changes.
     *
     * @param waypoint they waypoint
     * @param oldX the old X
     * @param oldY the old Y
     * @param oldZ the old Z
     * @param newX the new X
     * @param newY the new Y
     * @param newZ the new Z
     * @since 5.1.0
     */
    default void waypointVectorChanged(
      final VectorWaypoint waypoint,
      final int oldX, final int oldY, final int oldZ,
      final int newX, final int newY, final int newZ
    ) {
    }
  }

}
