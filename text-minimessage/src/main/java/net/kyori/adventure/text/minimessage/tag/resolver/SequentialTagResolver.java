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
package net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SequentialTagResolver implements TagResolver, SerializableResolver {
  final TagResolver[] resolvers;

  SequentialTagResolver(final @NotNull TagResolver@NotNull[] resolvers) {
    this.resolvers = resolvers;
  }

  @Override
  public @Nullable Tag resolve(final @NotNull String name, final @NotNull ArgumentQueue arguments, final @NotNull Context ctx) throws ParsingException {
    @Nullable ParsingException thrown = null;
    for (final TagResolver resolver : this.resolvers) {
      try {
        final @Nullable Tag placeholder = resolver.resolve(name, arguments, ctx);

        if (placeholder != null) return placeholder;
      } catch (final ParsingException ex) {
        arguments.reset();
        if (thrown == null) {
          thrown = ex;
        } else {
          thrown.addSuppressed(ex);
        }
      } catch (final Exception ex) {
        arguments.reset();
        final ParsingException err = ctx.newException("Exception thrown while parsing <" + name + ">", ex, arguments);
        if (thrown == null) {
          thrown = err;
        } else {
          thrown.addSuppressed(err);
        }
      }
    }

    if (thrown != null) {
      throw thrown;
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

  @Override
  public void handle(final @NotNull Component serializable, final @NotNull ClaimConsumer consumer) {
    for (final TagResolver resolver : this.resolvers) {
      if (resolver instanceof SerializableResolver) {
        ((SerializableResolver) resolver).handle(serializable, consumer);
      }
    }
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof SequentialTagResolver)) {
      return false;
    }
    final SequentialTagResolver that = (SequentialTagResolver) other;
    return Arrays.equals(this.resolvers, that.resolvers);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.resolvers);
  }
}
