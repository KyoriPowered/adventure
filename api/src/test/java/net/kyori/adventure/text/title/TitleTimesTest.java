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
import net.kyori.adventure.title.Title;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TitleTimesTest {

  private final Duration d0 = Duration.ofSeconds(1);
  private final Duration d1 = Duration.ofSeconds(2);
  private final Duration d2 = Duration.ofMillis(1234);

  @Test
  void testCreate() {
    final Title.Times times = Title.Times.times(this.d0, this.d1, this.d2);
    assertEquals(this.d0, times.fadeIn());
    assertEquals(this.d1, times.stay());
    assertEquals(this.d2, times.fadeOut());
  }

  @Test
  void testEquality() {
    final Title.Times times = Title.Times.times(this.d0, this.d1, this.d2);
    final Title.Times equalTimes = Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(2000), Duration.ofMillis(1234));
    final Title.Times notEqualFadeIn = Title.Times.times(Duration.ofMillis(999), Duration.ofMillis(2000), Duration.ofMillis(1234));
    final Title.Times notEqualStay = Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(2001), Duration.ofMillis(1234));
    final Title.Times notEqualFadeOut = Title.Times.times(Duration.ofMillis(1000), Duration.ofMillis(2000), Duration.ofMillis(1235));

    new EqualsTester()
      .addEqualityGroup(times, equalTimes)
      .addEqualityGroup(notEqualFadeIn)
      .addEqualityGroup(notEqualStay)
      .addEqualityGroup(notEqualFadeOut)
      .testEquals();
  }
}
