/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.title;

import java.time.Duration;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class TitleTest {
  private static final Component TITLE = Component.text("title");
  private static final Component SUBTITLE = Component.text("subtitle");
  private static final Duration FADE_IN = Duration.ofMillis(1);
  private static final Duration STAY = Duration.ofMillis(2);
  private static final Duration FADE_OUT = Duration.ofMillis(4);
  private static final Title.Times TIMES = Title.Times.times(FADE_IN, STAY, FADE_OUT);

  @Test
  void testTitle() {
    final Title t = Title.title(TITLE, SUBTITLE, TIMES);
    assertSame(TITLE, t.title());
  }

  @Test
  void testSubtitle() {
    final Title t = Title.title(TITLE, SUBTITLE, TIMES);
    assertSame(SUBTITLE, t.subtitle());
  }

  @Test
  void testTimes() {
    final Title t0 = Title.title(TITLE, SUBTITLE, TIMES);
    assertSame(TIMES, t0.times());

    final Title t1 = Title.title(TITLE, SUBTITLE, null);
    assertNull(t1.times());
  }

  @Test
  void testRebuild() {
    final Title title = Title.title(TITLE, SUBTITLE, TIMES);
    assertEquals(title, title.toBuilder().build());
  }

  @Test
  void testBuilder() {
    final Title t0 = Title.builder()
      .title(TITLE)
      .subtitle(SUBTITLE)
      .times(TIMES)
      .build();
    assertEquals(Title.title(TITLE, SUBTITLE, TIMES), t0);
  }

  @Test
  void testTimesFadeIn() {
    final Title.Times times = Title.Times.times(FADE_IN, STAY, FADE_OUT);
    assertSame(FADE_IN, times.fadeIn());
  }

  @Test
  void testTimesStay() {
    final Title.Times times = Title.Times.times(FADE_IN, STAY, FADE_OUT);
    assertSame(STAY, times.stay());
  }

  @Test
  void testTimesFadeOut() {
    final Title.Times times = Title.Times.times(FADE_IN, STAY, FADE_OUT);
    assertSame(FADE_OUT, times.fadeOut());
  }

  @Test
  void testTimesRebuild() {
    final Title.Times times = Title.Times.times(FADE_IN, STAY, FADE_OUT);
    assertEquals(times, times.toBuilder().build());
  }

  @Test
  void testTimesBuilder() {
    final Title.Times t0 = Title.Times.builder()
      .fadeIn(FADE_IN)
      .stay(STAY)
      .fadeOut(FADE_OUT)
      .build();
    assertEquals(Title.Times.times(FADE_IN, STAY, FADE_OUT), t0);
  }

}
