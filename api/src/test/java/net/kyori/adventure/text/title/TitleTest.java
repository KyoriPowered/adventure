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
package net.kyori.adventure.text.title;

import com.google.common.testing.EqualsTester;
import java.time.Duration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TitleTest {

  private final Component foo = Component.text("foo");
  private final Component bar = Component.text("bar");
  private final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(1500));

  @Test
  void testCreateSimple() {
    final Title title = Title.title(this.foo, this.bar);
    assertEquals(this.foo, title.title());
    assertEquals(this.bar, title.subtitle());
    assertEquals(Title.DEFAULT_TIMES, title.times());
  }

  @Test
  void testCreateCustomTimes() {
    final Title title = Title.title(this.bar, this.foo, this.times);
    assertEquals(this.bar, title.title());
    assertEquals(this.foo, title.subtitle());
    assertEquals(this.times, title.times());
  }

  @Test
  void testGetTitleParts() {
    final Title title = Title.title(this.foo, this.bar, this.times);
    assertEquals(this.foo, title.part(TitlePart.TITLE));
    assertEquals(this.bar, title.part(TitlePart.SUBTITLE));
    assertEquals(this.times, title.part(TitlePart.TIMES));
  }

  @Test
  void testIllegalTitleParts() {
    final Title title = Title.title(this.foo, this.bar);
    final TitlePart<Component> unknownPart = new TitlePart<Component>() {
      @Override
      public String toString() {
        return "TitlePart.CAT";
      }
    };

    assertThrows(IllegalArgumentException.class, () -> title.part(unknownPart));
    assertThrows(NullPointerException.class, () -> title.part(null));
  }

  @Test
  void testEquality() {
    final Title title = Title.title(this.foo, this.bar, this.times);
    final Title equalTitle = Title.title(Component.text("foo"), Component.text("bar"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(1500)));
    final Title notEqualSubtitle = Title.title(Component.text("foo"), Component.text("cat"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(1500)));
    final Title notEqualTitle = Title.title(Component.text("cat"), Component.text("bar"), Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(1000), Duration.ofMillis(1500)));
    final Title notEqualTimes = Title.title(Component.text("foo"), Component.text("bar"), Title.Times.times(Duration.ofMillis(499), Duration.ofMillis(1000), Duration.ofMillis(1500)));

    new EqualsTester()
      .addEqualityGroup(title, equalTitle)
      .addEqualityGroup(notEqualSubtitle)
      .addEqualityGroup(notEqualTitle)
      .addEqualityGroup(notEqualTimes)
      .testEquals();
  }
}
