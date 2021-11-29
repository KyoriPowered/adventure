/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage.transformation;

import java.util.List;
import java.util.function.Function;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A registry of transformation types understood by the MiniMessage parser.
 *
 * @since 4.1.0
 */
public interface TransformationRegistry extends Buildable<TransformationRegistry, TransformationRegistry.Builder> {

  /**
   * Gets a transformation from this registry based on the current state.
   *
   * @param name the tag name
   * @param inners the tokens that make up the tag arguments
   * @param placeholderResolver the placeholder resolver
   * @param context the debug context
   * @return a possible transformation
   * @since 4.2.0
   */
  @Nullable Transformation get(final String name, final List<TagPart> inners, final PlaceholderResolver placeholderResolver, final Context context);

  /**
   * Test if any registered transformation type matches the provided key.
   *
   * @param name tag name
   * @param placeholderResolver function to resolve other component types
   * @return whether any transformation exists
   * @since 4.1.0
   * @deprecated For removal since 4.2.0, use {@link #exists(String, PlaceholderResolver)} with {@link PlaceholderResolver#dynamic(Function)}
   */
  @ApiStatus.ScheduledForRemoval
  @Deprecated
  default boolean exists(final String name, final Function<String, ComponentLike> placeholderResolver) {
    return this.exists(name, PlaceholderResolver.dynamic(placeholderResolver));
  }

  /**
   * Tests if any registered transformation type matches the provided key.
   *
   * @param name the tag name
   * @return whether any transformation type exists
   * @since 4.2.0
   */
  boolean exists(final String name);

  /**
   * Tests if any registered transformation type matches the provided key.
   *
   * @param name the tag name
   * @param placeholderResolver the resolver to resolve other component types
   * @return whether any transformation exists
   * @since 4.2.0
   */
  boolean exists(final String name, final PlaceholderResolver placeholderResolver);

  /**
   * Creates a new {@link TransformationRegistry.Builder}.
   *
   * @return a builder
   * @since 4.2.0
   */
  static @NotNull Builder builder() {
    return new TransformationRegistryImpl.BuilderImpl();
  }

  /**
   * Gets an instance of the transformation registry without any transformations.
   *
   * @return a empty transformation registry
   * @since 4.2.0
   */
  static @NotNull TransformationRegistry empty() {
    return TransformationRegistryImpl.EMPTY;
  }

  /**
   * Gets an instance of the transformation registry with only the standard transformations.
   *
   * @return a standard transformation registry
   * @since 4.2.0
   */
  static @NotNull TransformationRegistry standard() {
    return TransformationRegistryImpl.STANDARD;
  }

  /**
   * A builder for {@link TransformationRegistry}.
   *
   * @since 4.2.0
   */
  interface Builder extends Buildable.Builder<TransformationRegistry> {

    /**
     * Clears all currently set transformations.
     *
     * @return this builder
     * @since 4.2.0
     */
    @NotNull Builder clear();

    /**
     * Adds a supplied transformation to the registry.
     *
     * @return this builder
     * @since 4.2.0
     */
    @NotNull Builder add(final @NotNull TransformationType<? extends Transformation> transformation);

    /**
     * Adds the supplied transformations to the registry.
     *
     * @return this builder
     * @since 4.2.0
     */
    @NotNull Builder add(final @NotNull TransformationType<? extends Transformation>... transformations);

  }
}
