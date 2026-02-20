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

final class ChunkWaypointImpl extends WaypointImpl implements ChunkWaypoint {

  private int x;
  private int z;

  ChunkWaypointImpl(final Key style, final TextColor color, final int x, final int z) {
    super(style, color);
    this.x = x;
    this.z = z;
  }

  ChunkWaypointImpl(final TextColor color, final int x, final int z) {
    super(color);
    this.x = x;
    this.z = z;
  }

  @Override
  public int x() {
    return this.x;
  }

  @Override
  public int z() {
    return this.z;
  }

  @Override
  public ChunkWaypoint pos(final int x, final int z) {
    this.x = x;
    this.z = z;
    return this;
  }

}
