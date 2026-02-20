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

import org.jetbrains.annotations.Contract;

/**
 * Represents a vector waypoint.
 *
 * @since 5.1.0
 * @sinceMinecraft 1.21.6
 */
public sealed interface VectorWaypoint extends Waypoint permits VectorWaypointImpl {

  /**
   * Gets the X coordinate.
   *
   * @return the X coordinate
   * @since 5.1.0
   */
  int x();

  /**
   * Gets the Y coordinate.
   *
   * @return the Y coordinate
   * @since 5.1.0
   */
  int y();

  /**
   * Gets the Z coordinate.
   *
   * @return the Z coordinate
   * @since 5.1.0
   */
  int z();

  /**
   * Sets the X, Y and Z coordinates.
   *
   * @param x the X coordinate
   * @param y the Y coordinate
   * @param z the Z coordinate
   * @return the waypoint
   * @since 5.1.0
   */
  @Contract("_, _, _ -> this")
  VectorWaypoint pos(final int x, final int y, final int z);

}
