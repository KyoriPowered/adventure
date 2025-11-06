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

import java.util.function.Supplier;
import net.kyori.adventure.text.minimessage.internal.util.ListMapHolder;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.util.TriState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/*
 * Note to anyone looking at this class and wondering about the {@link NonNull}s. For
 * some reason, IntelliJ completely ignores any {@link NullMarked} annotations on the package
 * or on the class, so these pop methods had to be annotated explicitly ¯\_(ツ)_/¯.
 */
final class ArgumentQueueImpl<T extends Tag.Argument> implements ArgumentQueue {
  private final Context context;
  private final ListMapHolder<T, String, T> args;
  private int ptr = 0;

  ArgumentQueueImpl(final Context context, final ListMapHolder<T, String, T> args) {
    this.context = context;
    this.args = args;
  }

  public ListMapHolder<T, String, T> args() {
    return this.args;
  }

  @Override
  public @NonNull T pop() {
    if (!this.hasNext()) {
      throw this.context.newException("Missing argument for this tag!", this);
    }
    return this.args.list().get(this.ptr++);
  }

  @Override
  public @NonNull T popOr(final String errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    if (!this.hasNext()) {
      throw this.context.newException(errorMessage, this);
    }
    return this.args.list().get(this.ptr++);
  }

  @Override
  public @NonNull T popOr(final Supplier<String> errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    if (!this.hasNext()) {
      throw this.context.newException(requireNonNull(errorMessage.get(), "errorMessage.get()"), this);
    }
    return this.args.list().get(this.ptr++);
  }

  @Override
  public @Nullable T peek() {
    return this.hasNext() ? this.args.list().get(this.ptr) : null;
  }

  @Override
  public boolean hasNext() {
    return this.ptr < this.args.list().size();
  }

  @Override
  public void reset() {
    this.ptr = 0;
  }

  @Override
  public String toString() {
    return this.args.toString();
  }

  @Override
  public boolean isPresent(final String name) {
    requireNonNull(name, "name");
    return this.args.map().containsKey(name);
  }

  @Override
  public Tag.@Nullable Argument get(final String name) {
    requireNonNull(name, "name");
    return this.args.map().get(name);
  }

  @Override
  public TriState flag(final String name) {
    final Tag.Argument argument = this.get(name);
    if (argument == null) {
      // The normal flag is not preset, so try the inverted flag
      final Tag.Argument invertedArgument = this.get('!' + name);
      if (invertedArgument == null) {
        return TriState.NOT_SET;
      }

      return TriState.FALSE;
    }

    return TriState.TRUE;
  }

  @Override
  public boolean isFlagPresent(final String name) {
    if (this.isPresent(name)) {
      return true;
    }
    return this.isPresent('!' + name);
  }

  @Override
  public Tag.Argument orThrow(final String name, final String errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    final Tag.Argument arg = this.get(name);
    if (arg == null) {
      throw this.context.newException(errorMessage);
    }
    return arg;
  }

  @Override
  public Tag.Argument orThrow(final String name, final Supplier<String> errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    final Tag.Argument arg = this.get(name);
    if (arg == null) {
      throw this.context.newException(errorMessage.get());
    }
    return arg;
  }
}
