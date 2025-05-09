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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.TagInternals;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A collection of known tags.
 *
 * <p>A resolver can handle anywhere from a single tag, to a dynamically generated set of tags, returning a tag based on the provided name and arguments.</p>
 *
 * @see StandardTags
 * @see Placeholder
 * @since 4.10.0
 */
public interface TagResolver {
  /**
   * Create a new builder for a tag resolver.
   *
   * @return the tag resolver builder
   * @since 4.10.0
   */
  static @NotNull Builder builder() {
    return new TagResolverBuilderImpl();
  }

  /**
   * Get the tag resolver that resolves all {@link StandardTags standard tags}.
   *
   * <p>This is the default resolver used by parsers.</p>
   *
   * @return the default resolver
   * @since 4.10.0
   */
  static @NotNull TagResolver standard() {
    return StandardTags.defaults();
  }

  /**
   * An empty tag resolver that will return {@code null} for all resolve attempts.
   *
   * @return the tag resolver
   * @since 4.10.0
   */
  static @NotNull TagResolver empty() {
    return EmptyTagResolver.INSTANCE;
  }

  /**
   * A tag resolver that will resolve a single tag by a case-insensitive key.
   *
   * @param name the name of the tag to resolve
   * @param tag the tag logic to return
   * @return a new tag resolver
   * @since 4.10.0
   */
  static TagResolver.@NotNull Single resolver(@TagPattern final @NotNull String name, final @NotNull Tag tag) {
    TagInternals.assertValidTagName(name);
    return new SingleResolver(
      name,
      requireNonNull(tag, "tag")
    );
  }

  /**
   * Create a tag resolver that only responds to a single tag name, and whose value does not depend on that name.
   *
   * @param name the name to respond to
   * @param handler the tag handler, may throw {@link ParsingException} if provided arguments are in an invalid format
   * @return a resolver that creates tags using the provided handler
   * @since 4.10.0
   */
  static @NotNull TagResolver resolver(@TagPattern final @NotNull String name, final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler) {
    return resolver(Collections.singleton(name), handler);
  }

  /**
   * Create a tag resolver that only responds to certain tag names, and whose value does not depend on that name.
   *
   * @param names the names to respond to
   * @param handler the tag handler, may throw {@link ParsingException} if provided arguments are in an invalid format
   * @return a resolver that creates tags using the provided handler
   * @since 4.10.0
   */
  static @NotNull TagResolver resolver(final @NotNull Set<String> names, final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler) {
    final Set<String> ownNames = new HashSet<>(names);
    for (final String name : ownNames) {
      TagInternals.assertValidTagName(name);
    }
    requireNonNull(handler, "handler");

    return new TagResolver() {
      @Override
      public @Nullable Tag resolve(final @NotNull String name, final @NotNull ArgumentQueue arguments, final @NotNull Context ctx) throws ParsingException {
        if (!names.contains(name)) return null;

        return handler.apply(arguments, ctx);
      }

      @Override
      public boolean has(final @NotNull String name) {
        return names.contains(name);
      }
    };
  }

  /**
   * Constructs a tag resolver capable of resolving from multiple sources.
   *
   * <p>The last specified resolver takes priority.</p>
   *
   * @param resolvers the tag resolvers
   * @return the tag resolver
   * @since 4.10.0
   */
  static @NotNull TagResolver resolver(final @NotNull TagResolver@NotNull... resolvers) {
    if (Objects.requireNonNull(resolvers, "resolvers").length == 1) {
      return Objects.requireNonNull(resolvers[0], "resolvers must not contain null elements");
    }
    return builder().resolvers(resolvers).build();
  }

  /**
   * Constructs a tag resolver capable of resolving from multiple sources.
   *
   * <p>The last specified resolver takes priority.</p>
   *
   * <p>The provided iterable is copied. This means changes to the iterable will not reflect in the returned resolver.</p>
   *
   * @param resolvers the tag resolvers
   * @return the tag resolver
   * @since 4.10.0
   */
  static @NotNull TagResolver resolver(final @NotNull Iterable<? extends TagResolver> resolvers) {
    // We can break out early and return exact resolvers in the case of a zero/one length array.
    if (resolvers instanceof Collection<?>) {
      final int size = ((Collection<?>) resolvers).size();
      if (size == 0) return empty();
      if (size == 1) return Objects.requireNonNull(resolvers.iterator().next(), "resolvers must not contain null elements");
    }

    return builder().resolvers(resolvers).build();
  }

  /**
   * Constructs a tag resolver capable of caching resolved tags.
   *
   * <p>The resolver can return {@code null} to indicate it cannot resolve a placeholder.
   * Once a string to replacement mapping has been created, it will be cached to avoid
   * the cost of recreating the replacement.</p>
   *
   * <p>Due to the complexity of handling lookups for tags with arguments, the built-in cache does not support anything but tags without arguments.</p>
   *
   * @param resolver the resolver
   * @return the caching tag resolver
   * @since 4.10.0
   */
  static @NotNull TagResolver caching(final TagResolver.@NotNull WithoutArguments resolver) {
    if (resolver instanceof CachingTagResolver) {
      return resolver;
    } else {
      return new CachingTagResolver(Objects.requireNonNull(resolver, "resolver"));
    }
  }

  /**
   * A collector that will combine a stream of resolvers into one joined resolver.
   *
   * @return a collector for tag resolvers
   * @since 4.10.0
   */
  static @NotNull Collector<TagResolver, ?, TagResolver> toTagResolver() {
    return TagResolverBuilderImpl.COLLECTOR;
  }

  /**
   * Gets a tag from this resolver based on the current state.
   *
   * @param name the tag name
   * @param arguments the arguments passed to the tag
   * @param ctx the parse context
   * @return a possible tag
   * @throws ParsingException if the provided arguments are invalid
   * @since 4.10.0
   */
  @Nullable Tag resolve(@TagPattern final @NotNull String name, final @NotNull ArgumentQueue arguments, final @NotNull Context ctx) throws ParsingException;

  /**
   * Get whether this resolver handles tags with a certain name.
   *
   * <p>This does not allow validating arguments.</p>
   *
   * @param name the tag name
   * @return whether this resolver has a tag with this name
   * @since 4.10.0
   */
  boolean has(final @NotNull String name);

  /**
   * A resolver that only handles a single tag key.
   *
   * @see TagResolver#resolver(String, Tag)
   * @since 4.10.0
   */
  @ApiStatus.NonExtendable
  interface Single extends TagResolver.WithoutArguments {
    /**
     * The key this resolver matches.
     *
     * <p>The returned key is compared case-insensitively.</p>
     *
     * @return the key
     * @since 4.10.0
     */
    @NotNull String key();

    /**
     * The tag returned by this resolver when the key is matching.
     *
     * @return the tag
     * @since 4.10.0
     */
    @NotNull Tag tag();

    @Override
    default @Nullable Tag resolve(@TagPattern final @NotNull String name) {
      if (this.has(name)) {
        return this.tag();
      }
      return null;
    }

    @Override
    default boolean has(final @NotNull String name) {
      return name.equalsIgnoreCase(this.key());
    }
  }

  /**
   * A tag resolver that only handles tags which do not take arguments.
   *
   * @since 4.10.0
   */
  @FunctionalInterface
  interface WithoutArguments extends TagResolver {
    /**
     * Resolve a tag based only on the provided name.
     *
     * @param name the provided name
     * @return a tag, if any is known.
     * @since 4.10.0
     */
    @Nullable Tag resolve(@TagPattern final @NotNull String name);

    /**
     * Check if this resolver knows of a tag.
     *
     * @param name the tag name
     * @return whether this tag is present
     * @since 4.10.0
     */
    @Override
    default boolean has(final @NotNull String name) {
      return this.resolve(name) != null;
    }

    @Override
    default @Nullable Tag resolve(@TagPattern final @NotNull String name, final @NotNull ArgumentQueue arguments, final @NotNull Context ctx) throws ParsingException {
      final Tag resolved = this.resolve(name);
      if (resolved != null && arguments.hasNext()) {
        throw ctx.newException("Tag '<" + name + ">' does not accept any arguments");
      }
      return resolved;
    }
  }

  /**
   * A builder to gradually construct tag resolvers.
   *
   * <p>Entries added later will take priority over entries added earlier.</p>
   *
   * @since 4.10.0
   */
  interface Builder {
    /**
     * Add a single tag to this resolver.
     *
     * @param name the tag identifier
     * @param tag the tag logic
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder tag(@TagPattern final @NotNull String name, final @NotNull Tag tag);

    /**
     * Add a single dynamically created tag to this resolver.
     *
     * @param name the name to respond to
     * @param handler the tag handler, may throw {@link ParsingException} if provided arguments are in an invalid format
     * @return this builder
     * @since 4.10.0
     */
    default @NotNull Builder tag(@TagPattern final @NotNull String name, final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler) {
      return this.tag(Collections.singleton(name), handler);
    }

    /**
     * Add a single dynamically created tag to this resolver.
     *
     * @param names the names to respond to
     * @param handler the tag handler, may throw {@link ParsingException} if provided arguments are in an invalid format
     * @return this builder
     * @since 4.10.0
     */
    default @NotNull Builder tag(final @NotNull Set<String> names, final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler) {
      return this.resolver(TagResolver.resolver(names, handler));
    }

    /**
     * Add a placeholder resolver to those queried by the result of this builder.
     *
     * @param resolver the resolver to add
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder resolver(final @NotNull TagResolver resolver);

    /**
     * Add placeholder resolvers to those queried by the result of this builder.
     *
     * @param resolvers the resolvers to add
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder resolvers(final @NotNull TagResolver@NotNull... resolvers);

    /**
     * Add placeholder resolvers to those queried by the result of this builder.
     *
     * @param resolvers the resolvers to add
     * @return this builder
     * @since 4.10.0
     */
    @NotNull Builder resolvers(final @NotNull Iterable<? extends TagResolver> resolvers);

    /**
     * Add a resolver that dynamically queries and caches based on the provided function.
     *
     * @param dynamic the function to query for replacements
     * @return this builder
     * @since 4.10.0
     */
    default @NotNull Builder caching(final TagResolver.@NotNull WithoutArguments dynamic) {
      return this.resolver(TagResolver.caching(dynamic));
    }

    /**
     * Create a placeholder resolver based on the input.
     *
     * <p>If no elements are added, this may return an empty resolver.</p>
     *
     * @return the resolver
     * @since 4.10.0
     */
    @NotNull TagResolver build();
  }
}
