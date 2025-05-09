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
package net.kyori.adventure.sound;

import com.google.common.testing.EqualsTester;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoundStopTest {
  private static final Key SOUND_KEY = Key.key("minecraft", "block.fence_gate.open");
  private static final Sound.Type SOUND_TYPE = () -> SOUND_KEY;

  @Test
  void testGetters() {
    final SoundStop stop = SoundStop.namedOnSource(SOUND_KEY, Sound.Source.HOSTILE);
    assertEquals(SOUND_KEY, stop.sound());
    assertEquals(Sound.Source.HOSTILE, stop.source());
  }

  @Test
  void testOfIsEqual() {
    new EqualsTester()
      .addEqualityGroup(
        SoundStop.all()
      )
      .addEqualityGroup(
        SoundStop.source(Sound.Source.HOSTILE)
      )
      .addEqualityGroup(
        SoundStop.named(SOUND_KEY),
        SoundStop.named(SOUND_TYPE),
        SoundStop.named(() -> SOUND_TYPE)
      )
      .addEqualityGroup(
        SoundStop.namedOnSource(SOUND_KEY, Sound.Source.HOSTILE),
        SoundStop.namedOnSource(SOUND_TYPE, Sound.Source.HOSTILE),
        SoundStop.namedOnSource(() -> SOUND_TYPE, Sound.Source.HOSTILE)
      )
      .testEquals();
  }
}
