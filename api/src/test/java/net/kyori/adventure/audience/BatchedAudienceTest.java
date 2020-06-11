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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertSame;

class BatchedAudienceTest {
  @Test
  void testOf_empty() {
    final Audience empty = Audience.empty();
    final Audience batched = BatchedAudience.of(empty);
    assertSame(empty, batched);
  }

  @Test
  void testOf_batched() {
    final Audience b0 = BatchedAudience.of(Audience.empty());
    final Audience b1 = BatchedAudience.of(b0);
    assertSame(b0, b1);
  }

  @Test
  void test_flush() {
    final BatchedAudience batch = BatchedAudience.of(Audience.of(new ArrayList<>()));
    assertSame(0, batch.flush());
    final AtomicInteger counter = new AtomicInteger(0);
    batch.queue(audience -> counter.incrementAndGet());
    assertSame(1, batch.flush());
  }
}
