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

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

/* package */ final class QueueAudience implements BatchAudience {
  private final Audience audience;
  private @MonotonicNonNull Queue<Operation> operations;

  /* package */ QueueAudience(final @NonNull Audience audience) {
    this.audience = requireNonNull(audience, "audience");
  }

  @Override
  public void queue(final @NonNull Operation operation) {
    if(this.operations == null) {
      this.operations = new LinkedBlockingQueue<>();
    }

    this.operations.offer(requireNonNull(operation, "operation"));
  }

  @Override
  public int flushBatch() {
    if(this.operations == null) {
      return 0;
    }

    int operations = 0;
    try {
      while(operations < Integer.MAX_VALUE) {
        this.operations.remove().process(this.audience);
        operations++;
      }
    } catch(final NoSuchElementException e) {
      // No-op, end of queue
    }

    return operations;
  }
}
