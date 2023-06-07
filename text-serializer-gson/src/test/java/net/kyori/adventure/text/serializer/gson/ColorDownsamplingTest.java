/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text.serializer.gson;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ColorDownsamplingTest {
  @Test
  void testPre116Downsamples() {
    final TextColor original = TextColor.color(0xAB2211);
    final NamedTextColor downsampled = NamedTextColor.nearestTo(original);
    final Component test = Component.text("meow", original);
    assertEquals("{\"color\":\"" + NamedTextColor.NAMES.key(downsampled) + "\",\"text\":\"meow\"}", GsonComponentSerializer.colorDownsamplingGson().serializer().toJson(test));
  }

  @Test
  void testPre116DownsamplesInChildren() {
    final TextColor original = TextColor.color(0xEC41AA);
    final NamedTextColor downsampled = NamedTextColor.nearestTo(original);
    final Component test = Component.text(builder -> builder.content("hey").append(Component.text("there", original)));
    assertEquals("{\"extra\":[{\"color\":\"" + NamedTextColor.NAMES.key(downsampled) + "\",\"text\":\"there\"}],\"text\":\"hey\"}", GsonComponentSerializer.colorDownsamplingGson().serializer().toJson(test));
  }
}
