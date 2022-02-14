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
package net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.Builder;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class TagResolverBuilderImpl implements TagResolver.Builder {
  static final Collector<TagResolver, TagResolver.Builder, TagResolver> COLLECTOR = Collector.of(
    TagResolver::builder,
    TagResolver.Builder::resolver,
    (left, right) -> TagResolver.builder().resolvers(left.build(), right.build()),
    TagResolver.Builder::build
  );

  private final Map<String, Tag> replacements = new HashMap<>();
  private final List<TagResolver> resolvers = new ArrayList<>();

  @Override
  public @NotNull Builder tag(final @NotNull String name, final @NotNull Tag tag) {
    this.replacements.put(
      requireNonNull(name, "name"),
      requireNonNull(tag, "tag")
    );
    return this;
  }

  @Override
  public @NotNull TagResolver.Builder resolver(final @NotNull TagResolver resolver) {
    if (!this.consumePotentialMappable(resolver)) {
      this.popMap();
      this.resolvers.add(requireNonNull(resolver, "resolver"));
    }
    return this;
  }

  @Override
  public @NotNull TagResolver.Builder resolvers(final @NotNull TagResolver @NotNull... resolvers) {
    boolean popped = false;
    for (final TagResolver resolver : requireNonNull(resolvers, "resolvers")) {
      if (resolver instanceof SequentialTagResolver) {
        this.resolvers(((SequentialTagResolver) resolver).resolvers);
      } else if (!this.consumePotentialMappable(resolver)) {
        if (!popped) {
          this.popMap();
          popped = true;
        }
        this.resolvers.add(requireNonNull(resolver, "resolvers[?]"));
        continue;
      }
      popped = false;
    }
    return this;
  }

  @Override
  public @NotNull TagResolver.Builder resolvers(final @NotNull Iterable<? extends TagResolver> resolvers) {
    boolean popped = false;
    for (final TagResolver resolver : requireNonNull(resolvers, "resolvers")) {
      if (resolver instanceof SequentialTagResolver) {
        this.resolvers(((SequentialTagResolver) resolver).resolvers);
      } else if (!this.consumePotentialMappable(resolver)) {
        if (!popped) {
          this.popMap();
          popped = true;
        }
        this.resolvers.add(requireNonNull(resolver, "resolvers[?]"));
        continue;
      }
      popped = false;
    }
    return this;
  }

  private void popMap() {
    if (!this.replacements.isEmpty()) {
      this.resolvers.add(new MapTagResolver(new HashMap<>(this.replacements)));
      this.replacements.clear();
    }
  }

  private boolean consumePotentialMappable(final TagResolver resolver) {
    if (resolver instanceof MappableResolver) {
      return ((MappableResolver) resolver).contributeToMap(this.replacements);
    } else {
      return false;
    }
  }

  @Override
  public @NotNull TagResolver build() {
    this.popMap();
    if (this.resolvers.size() == 0) {
      return EmptyTagResolver.INSTANCE;
    } else if (this.resolvers.size() == 1) {
      return this.resolvers.get(0);
    } else {
      final TagResolver[] resolvers = this.resolvers.toArray(new TagResolver[0]);
      Collections.reverse(Arrays.asList(resolvers));
      return new SequentialTagResolver(resolvers);
    }
  }

}
