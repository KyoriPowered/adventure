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
package net.kyori.adventure.util;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ComponentMessageThrowableTest {
  @Test
  void testGetMessage() {
    final Component c0 = Component.text("uh oh");
    assertSame(c0, ComponentMessageThrowable.getMessage(new Checked(c0)));
    assertNull(ComponentMessageThrowable.getMessage(new IllegalStateException("uh oh")));
  }

  @Test
  void testGetOrConvertMessage() {
    final Component c0 = Component.text("uh oh");
    assertSame(c0, ComponentMessageThrowable.getOrConvertMessage(new Checked(c0)));
    assertEquals(c0, ComponentMessageThrowable.getOrConvertMessage(new IllegalStateException("uh oh")));
    assertNull(ComponentMessageThrowable.getOrConvertMessage(new IllegalStateException((String) null)));
  }

  @SuppressWarnings("serial")
  static class Checked extends Exception implements ComponentMessageThrowable {
    private final @Nullable Component componentMessage;

    Checked(final @Nullable Component componentMessage) {
      this.componentMessage = componentMessage;
    }

    @Override
    public @Nullable Component componentMessage() {
      return this.componentMessage;
    }
  }
}
