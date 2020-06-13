/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.audience;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AudienceTest {
  @Test
  void testOf_none() {
    assertSame(Audience.empty(), Audience.of());
  }

  @Test
  void testOf_one() {
    final Audience a0 = Audience.empty();
    assertSame(a0,  Audience.of(a0));
  }

  @Test
  void testOf_many() {
    final Audience a0 = Audience.empty();
    final Audience a1 = Audience.empty();
    final Audience ma = Audience.of(a0, a1);
    assertTrue(ma instanceof MultiAudience);
    assertThat(((MultiAudience) ma).audiences()).containsExactly(a0, a1).inOrder();
  }

  @Test
  void testWeakOf_none() {
    final Audience empty = Audience.empty();
    final Audience weak = Audience.weakOf(empty);
    assertSame(empty, weak);
  }
}
