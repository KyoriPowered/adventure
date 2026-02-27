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

import static java.util.Objects.requireNonNull;

sealed class WaypointImpl implements Waypoint permits AzimuthWaypointImpl, ChunkWaypointImpl, EmptyWaypointImpl, VectorWaypointImpl {

  private Key style;
  private TextColor color;

  WaypointImpl(final Key style, final TextColor color) {
    this.style = requireNonNull(style, "style");
    this.color = requireNonNull(color, "color");
  }

  WaypointImpl(final TextColor color) {
    this(Key.key("default"), color);
  }

  @Override
  public Key style() {
    return this.style;
  }

  @Override
  public Waypoint style(final Key key) {
    this.style = requireNonNull(key, "key");
    return this;
  }

  @Override
  public TextColor color() {
    return this.color;
  }

  @Override
  public Waypoint color(final TextColor color) {
    this.color = requireNonNull(color, "color");
    return this;
  }
}
