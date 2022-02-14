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
package net.kyori.test;

import org.jetbrains.annotations.NotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class EqualityAssertions {

  private EqualityAssertions() {
  }

  //Covers all branches in the standard #equals and #hashcode used in this project
  @SafeVarargs
  public static <T> void assertEqualityAndNonEquality(final @NotNull T value, final @NotNull T equalToValue, final @NotNull T@NotNull... notEqualToValue) {
    assertEquals(value, value);
    assertEquals(value, equalToValue);
    assertNotEquals(value, null);
    assertNotEquals(value, value instanceof String ? new boolean[0] : "");
    assertEquals(value.hashCode(), equalToValue.hashCode());

    for (final T t : notEqualToValue) {
      assertNotEquals(value, t);
      assertNotEquals(value.hashCode(), t.hashCode());
    }
  }
}
