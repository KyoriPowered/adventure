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
package net.kyori.adventure.text.minimessage.internal.serializer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.TagInternals;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A specific {@link TagResolver} that can process serialization requests for a component.
 *
 * @since 4.10.0
 */
public interface SerializableResolver {
  /**
   * Create a tag resolver that only responds to a single tag name, and whose value does not depend on that name.
   *
   * @param name the name to respond to
   * @param handler the tag handler, may throw {@link ParsingException} if provided arguments are in an invalid format
   * @param componentClaim the claim to test components against
   * @return a resolver that creates tags using the provided handler
   * @since 4.10.0
   */
  static @NotNull TagResolver claimingComponent(final @NotNull String name, final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, final @NotNull Function<Component, @Nullable Emitable> componentClaim) {
    return claimingComponent(Collections.singleton(name), handler, componentClaim);
  }

  /**
   * Create a tag resolver that only responds to certain tag names, and whose value does not depend on that name.
   *
   * @param names the names to respond to
   * @param handler the tag handler, may throw {@link ParsingException} if provided arguments are in an invalid format
   * @param componentClaim the claim to test components against
   * @return a resolver that creates tags using the provided handler
   * @since 4.10.0
   */
  static @NotNull TagResolver claimingComponent(final @NotNull Set<String> names, final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, final @NotNull Function<Component, @Nullable Emitable> componentClaim) {
    final Set<String> ownNames = new HashSet<>(names);
    for (final String name : ownNames) {
      TagInternals.assertValidTagName(name);
    }
    requireNonNull(handler, "handler");
    return new ComponentClaimingResolverImpl(ownNames, handler, componentClaim);
  }

  /**
   * Create a tag resolver that only responds to a single tag name, and whose value does not depend on that name.
   *
   * @param name the name to respond to
   * @param handler the tag handler, may throw {@link ParsingException} if provided arguments are in an invalid format
   * @param styleClaim the extractor for style claims on components
   * @return a resolver that creates tags using the provided handler
   * @since 4.10.0
   */
  static @NotNull TagResolver claimingStyle(final @NotNull String name, final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, final @NotNull StyleClaim<?> styleClaim) {
    return claimingStyle(Collections.singleton(name), handler, styleClaim);
  }

  /**
   * Create a tag resolver that only responds to certain tag names, and whose value does not depend on that name.
   *
   * @param names the names to respond to
   * @param handler the tag handler, may throw {@link ParsingException} if provided arguments are in an invalid format
   * @param styleClaim the extractor for style claims on components
   * @return a resolver that creates tags using the provided handler
   * @since 4.10.0
   */
  static @NotNull TagResolver claimingStyle(final @NotNull Set<String> names, final @NotNull BiFunction<ArgumentQueue, Context, Tag> handler, final @NotNull StyleClaim<?> styleClaim) {
    final Set<String> ownNames = new HashSet<>(names);
    for (final String name : ownNames) {
      TagInternals.assertValidTagName(name);
    }
    requireNonNull(handler, "handler");
    return new StyleClaimingResolverImpl(ownNames, handler, styleClaim);
  }

  /**
   * Attempt to process a component for serialization.
   *
   * @param serializable the component to serialize
   * @param consumer a consumer for component claims, must not be stored
   * @since 4.10.0
   */
  void handle(final @NotNull Component serializable, final @NotNull ClaimConsumer consumer);

  /**
   * A subinterface for resolvers that only handle one single tag.
   *
   * @since 4.10.0
   */
  interface Single extends SerializableResolver {
    @Override
    default void handle(final @NotNull Component serializable, final @NotNull ClaimConsumer consumer) {
      final @Nullable StyleClaim<?> style = this.claimStyle();
      if (style != null && !consumer.styleClaimed(style.claimKey())) {
        final @Nullable Emitable applied = style.apply(serializable.style());
        if (applied != null) {
          consumer.style(style.claimKey(), applied);
        }
      }
      if (!consumer.componentClaimed()) {
        final @Nullable Emitable component = this.claimComponent(serializable);
        if (component != null) {
          consumer.component(component);
        }
      }
    }

    /**
     * Claim a style for tag emission.
     *
     * <p>Style emitters are additive -- a non-{@code null} result will not terminate traversal of
     * iterable tags. However, each style element can only be claimed once.</p>
     *
     * @return an emitable if this claimer handles some element of the provided style
     * @since 4.10.0
     */
    default @Nullable StyleClaim<?> claimStyle() {
      return null;
    }

    /**
     * Claim a full component for tag emission.
     *
     * <p>The first non-null result will be the <em>only</em> handler for this component. The component's style will be handled separately.</p>
     *
     * <p>Children of the provided component should be ignored.</p>
     *
     * @param component the component to inspect
     * @return an emitable if this claimer handles the provided component type
     * @since 4.10.0
     */
    default @Nullable Emitable claimComponent(final @NotNull Component component) {
      return null;
    }
  }
}
