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
package net.kyori.adventure.text.minimessage.tag;

import java.util.List;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag.Argument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SequentialTagResolver implements TagResolver {
  final TagResolver[] resolvers;

  SequentialTagResolver(final @NotNull TagResolver@NotNull[] resolvers) {
    this.resolvers = resolvers;
  }

  @Override
  public @Nullable Tag resolve(final @NotNull String name, final @NotNull List<? extends Argument> arguments, final @NotNull Context ctx) {
    for (final TagResolver resolver : this.resolvers) {
      final @Nullable Tag placeholder = resolver.resolve(name, arguments, ctx);
      if (placeholder != null) {
        return placeholder;
      }
    }
    return null;
  }

  @Override
  public boolean has(final @NotNull String name) {
    for (final TagResolver resolver : this.resolvers) {
      if (resolver.has(name)) {
        return true;
      }
    }
    return false;
  }
}
