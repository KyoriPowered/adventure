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

import java.util.Map;
import java.util.function.Supplier;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.NamedArgumentMap;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class NamedArgumentMapImpl<T extends Tag.Argument> implements NamedArgumentMap {
  private final Context context;
  final Map<String, T> args;

  NamedArgumentMapImpl(final Context context, final Map<String, T> args) {
    this.context = context;
    this.args = args;
  }

  @Override
  public boolean isPresent(final @NotNull String name) {
    requireNonNull(name, "name");
    return this.args.containsKey(name);
  }

  @Override
  public int size() {
    return this.args.size();
  }

  @Override
  public Tag.@Nullable Argument get(final @NotNull String name) {
    requireNonNull(name, "name");
    return this.args.get(name);
  }

  @Override
  public @NotNull TriState flag(final @NotNull String name) {
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
  public Tag.@NotNull Argument orThrow(final @NotNull String name, final @NotNull String errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    final Tag.Argument arg = this.get(name);
    if (arg == null) {
      throw this.context.newException(errorMessage);
    }
    return arg;
  }

  @Override
  public Tag.@NotNull Argument orThrow(final @NotNull String name, final @NotNull Supplier<String> errorMessage) {
    requireNonNull(errorMessage, "errorMessage");
    final Tag.Argument arg = this.get(name);
    if (arg == null) {
      throw this.context.newException(errorMessage.get());
    }
    return arg;
  }
}
