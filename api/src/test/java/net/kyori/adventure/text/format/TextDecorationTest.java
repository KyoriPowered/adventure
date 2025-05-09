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
package net.kyori.adventure.text.format;

import java.util.Arrays;
import net.kyori.adventure.util.TriState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextDecorationTest {
  @Test
  void testByBoolean() {
    assertEquals(TextDecoration.State.NOT_SET, TextDecoration.State.byBoolean(null));
    assertEquals(TextDecoration.State.FALSE, TextDecoration.State.byBoolean(false));
    assertEquals(TextDecoration.State.TRUE, TextDecoration.State.byBoolean(true));
  }

  @Test
  void testWithStateThrowsOnNull() {
    assertThrows(NullPointerException.class, () -> TextDecoration.BOLD.withState((TextDecoration.State) null));
    assertThrows(NullPointerException.class, () -> TextDecoration.BOLD.withState((TriState) null));
  }

  @Test
  void testByTristate() {
    assertEquals(TextDecoration.State.NOT_SET, TextDecoration.State.byTriState(TriState.NOT_SET));
    assertEquals(TextDecoration.State.FALSE, TextDecoration.State.byTriState(TriState.FALSE));
    assertEquals(TextDecoration.State.TRUE, TextDecoration.State.byTriState(TriState.TRUE));

    assertThrows(NullPointerException.class, () -> TextDecoration.State.byTriState(null));
    assertDoesNotThrow(() -> Arrays.stream(TriState.values()).forEach(TextDecoration.State::byTriState));
  }
}
