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
package net.kyori.adventure.text.minimessage.translation;

import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.TranslationArgumentLike;
import net.kyori.adventure.text.VirtualComponent;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * A utility class to create arguments for
 * {@link TranslatableComponent translatable components}.
 * This is intended to be used with a {@link MiniMessageTranslator} instance to allow for
 * {@code <name>} tags.
 *
 * <p>Static methods on this class work by creating
 * {@link VirtualComponent virtual components} that store data about the argument.
 * The MiniMessage translator instance detects these virtual components to use the name
 * provided as tag names to replace the {@code <arg>} tag.</p>
 *
 * <p>As the names provided to all static methods in this class are used to create tags,
 * they must be valid tag names.</p>
 *
 * @since 4.20.0
 */
public final class Argument {
  private Argument() {
  }

  /**
   * Creates a named boolean argument.
   *
   * @param name the name
   * @param value the value
   * @return the named argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike bool(final @TagPattern @NotNull String name, final boolean value) {
    return argument(name, TranslationArgument.bool(value));
  }

  /**
   * Creates a named numeric argument.
   *
   * @param name the name
   * @param value the value
   * @return the named argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike numeric(final @TagPattern @NotNull String name, final @NotNull Number value) {
    return argument(name, TranslationArgument.numeric(value));
  }

  /**
   * Creates a named string argument.
   *
   * @param name the name
   * @param value the value
   * @return the named argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike numeric(final @TagPattern @NotNull String name, final @NotNull String value) {
    return argument(name, TranslationArgument.component(Component.text(value)));
  }

  /**
   * Creates a named component argument.
   *
   * @param name the name
   * @param value the value
   * @return the named argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike component(final @TagPattern @NotNull String name, final @NotNull ComponentLike value) {
    return argument(name, TranslationArgument.component(value));
  }

  /**
   * Creates a named translation argument.
   *
   * @param name the name
   * @param argument the translation argument
   * @return the named argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike argument(final @TagPattern @NotNull String name, final @NotNull TranslationArgumentLike argument) {
    return argument(name, requireNonNull(argument, "argument").asTranslationArgument());
  }

  /**
   * Creates a named translation argument.
   *
   * @param name the name
   * @param argument the translation argument
   * @return the named argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike argument(final @TagPattern @NotNull String name, final @NotNull TranslationArgument argument) {
    return Component.virtual(Void.class, new MiniMessageTranslatorArgument<>(name, requireNonNull(argument, "argument")));
  }

  /**
   * Creates a named tag argument.
   *
   * @param name the name
   * @param tag the tag
   * @return the named argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike tag(final @TagPattern @NotNull String name, final @NotNull Tag tag) {
    return Component.virtual(Void.class, new MiniMessageTranslatorArgument<>(name, requireNonNull(tag, "tag")));
  }

  /**
   * Creates an argument used to add arbitrary tag resolvers to the deserialization process.
   *
   * @param resolvers the resolvers
   * @return the argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike tagResolver(final @NotNull TagResolver @NotNull... resolvers) {
    return tagResolver(TagResolver.resolver(resolvers));
  }

  /**
   * Creates an argument used to add arbitrary tag resolvers to the deserialization process.
   *
   * @param resolvers the resolvers
   * @return the argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike tagResolver(final @NotNull Iterable<TagResolver> resolvers) {
    return tagResolver(TagResolver.resolver(resolvers));
  }

  /**
   * Creates an argument used to add arbitrary tag resolvers to the deserialization process.
   *
   * @param tagResolver the tag resolver
   * @return the argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike tagResolver(final @NotNull TagResolver tagResolver) {
    // The name field is unused here.
    return Component.virtual(Void.class, new MiniMessageTranslatorArgument<>("unused", requireNonNull(tagResolver, "tagResolver")));
  }

  /**
   * Creates an argument used to set the target of the deserialization process.
   *
   * @param target the target
   * @return the argument
   * @since 4.20.0
   */
  public static @NotNull ComponentLike target(final @NotNull Pointered target) {
    return Component.virtual(Void.class, new MiniMessageTranslatorTarget(requireNonNull(target, "target")));
  }
}
