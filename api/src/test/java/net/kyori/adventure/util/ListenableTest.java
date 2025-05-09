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

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListenableTest {
  private final Listenable<Runnable> listenable = new Listenable<Runnable>() {
  };

  @Test
  void testEquality() {
    final AtomicInteger count = new AtomicInteger();
    final Runnable listener = count::incrementAndGet;

    this.listenable.forEachListener(Runnable::run);
    assertEquals(0, count.get()); // 0 listeners

    this.listenable.addListener0(listener);
    this.listenable.forEachListener(Runnable::run);
    assertEquals(1, count.get()); // 1 listener

    this.listenable.addListener0(listener);
    this.listenable.forEachListener(Runnable::run);
    assertEquals(3, count.get()); // 2 listeners

    this.listenable.removeListener0(listener);
    this.listenable.forEachListener(Runnable::run);
    assertEquals(4, count.get()); // 1 listener

    this.listenable.removeListener0(listener);
    this.listenable.forEachListener(Runnable::run);
    assertEquals(4, count.get()); // 0 listeners
  }
}
