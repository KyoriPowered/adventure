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
package net.kyori.adventure.text.minimessage;

import java.util.List;
import java.util.function.Supplier;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class ArgumentQueueImpl<T extends Tag.Argument> implements ArgumentQueue {
  private final Context context;
  final List<T> args;
  private int ptr = 0;

  ArgumentQueueImpl(final Context context, final List<T> args) {
    this.context = context;
    this.args = args;
  }

  @Override
  public @NotNull T pop() {
    if (!this.hasNext()) {
      throw this.context.newException("Missing argument for this tag!", this);
    }
    return this.args.get(this.ptr++);
  }

  @Override
  public @NotNull T popOr(final @NotNull String errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    if (!this.hasNext()) {
      throw this.context.newException(errorMessage, this);
    }
    return this.args.get(this.ptr++);
  }

  @Override
  public @NotNull T popOr(final @NotNull Supplier<String> errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    if (!this.hasNext()) {
      throw this.context.newException(requireNonNull(errorMessage.get(), "errorMessage.get()"), this);
    }
    return this.args.get(this.ptr++);
  }

  @Override
  public @Nullable T peek() {
    return this.hasNext() ? this.args.get(this.ptr) : null;
  }

  @Override
  public boolean hasNext() {
    return this.ptr < this.args.size();
  }

  @Override
  public void reset() {
    this.ptr = 0;
  }

  @Override
  public String toString() {
    return this.args.toString();
  }
}
