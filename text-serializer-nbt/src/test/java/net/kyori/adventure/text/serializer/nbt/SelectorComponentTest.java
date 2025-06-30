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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.serializer.nbt.SerializerTests.serializeComponent;
import static net.kyori.adventure.text.serializer.nbt.SerializerTests.testComponent;

final class SelectorComponentTest {
  @Test
  void test() {
    String pattern = "@p";
    testComponent(
      Component.selector(pattern),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.SELECTOR, pattern)
        .build()
    );
  }

  @Test
  void testSeparator() {
    String pattern = "@r";
    Component separator = Component.text(",");

    testComponent(
      Component.selector(pattern, separator),
      CompoundBinaryTag.builder()
        .putString(ComponentTreeConstants.SELECTOR, pattern)
        .put(ComponentTreeConstants.SEPARATOR, serializeComponent(separator))
        .build()
    );
  }
}
