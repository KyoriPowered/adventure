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

import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmptyWaypointTest {
  private final AtomicInteger style = new AtomicInteger();
  private final AtomicInteger color = new AtomicInteger();
  private final Waypoint.Listener listener = new Waypoint.Listener() {
    @Override
    public void waypointStyleChanged(@NonNull final Waypoint waypoint, @NonNull final Key oldStyle, @NonNull final Key newStyle) {
      EmptyWaypointTest.this.style.incrementAndGet();
    }

    @Override
    public void waypointColorChanged(@NonNull final Waypoint waypoint, @NonNull final TextColor oldColor, @NonNull final TextColor newColor) {
      EmptyWaypointTest.this.color.incrementAndGet();
    }
  };
  private final EmptyWaypoint waypoint = Waypoint.empty(NamedTextColor.RED);

  @Test
  void testColor() {
    final TextColor testColor = NamedTextColor.BLUE;
    assertEquals(testColor, this.waypoint.color(testColor).color());
    assertEquals(0, this.color.get());

    this.waypoint.addListener(this.listener);
    final TextColor testColor2 = NamedTextColor.YELLOW;
    assertEquals(testColor2, this.waypoint.color(testColor2).color());
    assertEquals(1, this.color.get());

    assertEquals(testColor2, this.waypoint.color(testColor2).color());
    assertEquals(1, this.color.get()); // value has not changed, should not have incremented
  }

  @Test
  void testStyle() {
    final Key testStyle = Key.key("bowtie");
    assertEquals(testStyle, this.waypoint.style(testStyle).style());
    assertEquals(0, this.style.get());

    this.waypoint.addListener(this.listener);
    final Key testStyle2 = Key.key("default");
    assertEquals(testStyle2, this.waypoint.style(testStyle2).style());
    assertEquals(1, this.style.get());

    assertEquals(testStyle2, this.waypoint.style(testStyle2).style());
    assertEquals(1, this.style.get()); // value has not changed, should not have incremented
  }
}
