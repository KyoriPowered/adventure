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
 * @param <T> type of waypoint data
 * @since 5.1.0
 */
public sealed interface Waypoint<T> permits WaypointImpl {

  /**
   * Creates an azimuth waypoint.
   *
   * @param color the color
   * @param angle the angle
   * @return an azimuth waypoint
   * @since 5.1.0
   */
  static Waypoint<Float> azimuth(final TextColor color, final float angle) {
    return new WaypointImpl<>(color, angle);
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
  static Waypoint<Float> azimuth(final Key style, final TextColor color, final float angle) {
    return new WaypointImpl<>(style, color, angle);
  }

  /**
   * Creates a chunk waypoint.
   *
   * @param color the color
   * @param chunkPos the chunk position
   * @return a chunk waypoint
   * @since 5.1.0
   */
  static Waypoint<ChunkPos> chunk(final TextColor color, final ChunkPos chunkPos) {
    return new WaypointImpl<>(color, chunkPos);
  }

  /**
   * Creates a chunk waypoint.
   *
   * @param style the style
   * @param color the color
   * @param chunkPos the chunk position
   * @return a chunk waypoint
   * @since 5.1.0
   */
  static Waypoint<ChunkPos> chunk(final Key style, final TextColor color, final ChunkPos chunkPos) {
    return new WaypointImpl<>(style, color, chunkPos);
  }

  /**
   * Creates a vector waypoint.
   *
   * @param color the color
   * @param vector the vector
   * @return a vector waypoint
   * @since 5.1.0
   */
  static Waypoint<Vector> vector(final TextColor color, final Vector vector) {
    return new WaypointImpl<>(color, vector);
  }

  /**
   * Creates a vector waypoint.
   *
   * @param style the style
   * @param color the color
   * @param vector the vector
   * @return a vector waypoint
   * @since 5.1.0
   */
  static Waypoint<Vector> vector(final Key style, final TextColor color, final Vector vector) {
    return new WaypointImpl<>(style, color, vector);
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
  Waypoint<T> style(final Key key);

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
  Waypoint<T> color(final TextColor color);

  /**
   * Gets the waypoint data.
   *
   * @return the waypoint data
   * @since 5.1.0
   */
  T data();

  /**
   * Sets the waypoint data.
   *
   * @param data the waypoint data
   * @return the waypoint
   * @since 5.1.0
   */
  @Contract("_ -> this")
  Waypoint<T> data(T data);

  /**
   * Represents a vector of 3 integers.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @since 5.1.0
   */
  record Vector(int x, int y, int z) {}

  /**
   * Represents a chunk position.
   *
   * @param x the x coordinate
   * @param z the y coordinate
   * @since 5.1.0
   */
  record ChunkPos(int x, int z) {}

}
