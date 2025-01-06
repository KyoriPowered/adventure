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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.TranslationArgumentLike;
import net.kyori.adventure.text.VirtualComponent;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * A {@link TranslationArgument} with an associated string name.
 *
 * <p>This is intended for use with {@link TranslatableComponent translatable components}
 * used with a {@link MiniMessageTranslator} instance to allow {@code <name>} tags.</p>
 *
 * <p>Static methods on this class work by creating
 * {@link VirtualComponent virtual components} that store instances of this class.
 * The MiniMessage translator instance detects these virtual components to use the name
 * provided as tag names to replace the {@code <arg>} tag.</p>
 *
 * <p>As the names provided to all static methods in this class are used to create tags,
 * they must be valid tag names.</p>
 *
 * @since 4.19.0
 */
@ApiStatus.NonExtendable
public interface NamedTranslationArgument {
  /**
   * Create a named boolean argument.
   *
   * @param name the name
   * @param value the value
   * @return the named argument
   * @since 4.19.0
   */
  static @NotNull ComponentLike bool(final @TagPattern @NotNull String name, final boolean value) {
    return argument(name, TranslationArgument.bool(value));
  }

  /**
   * Create a named numeric argument.
   *
   * @param name the name
   * @param value the value
   * @return the named argument
   * @since 4.19.0
   */
  static @NotNull ComponentLike numeric(final @TagPattern @NotNull String name, final @NotNull Number value) {
    return argument(name, TranslationArgument.numeric(value));
  }

  /**
   * Create a named component argument.
   *
   * @param name the name
   * @param value the value
   * @return the named argument
   * @since 4.19.0
   */
  static @NotNull ComponentLike component(final @TagPattern @NotNull String name, final @NotNull ComponentLike value) {
    return argument(name, TranslationArgument.component(value));
  }

  /**
   * Create a named translation argument.
   *
   * @param name the name
   * @param argument the translation argument
   * @return the named argument
   * @since 4.19.0
   */
  static @NotNull ComponentLike argument(final @TagPattern @NotNull String name, final @NotNull TranslationArgumentLike argument) {
    return argument(name, requireNonNull(argument, "argument").asTranslationArgument());
  }

  /**
   * Create a named translation argument.
   *
   * @param name the name
   * @param argument the translation argument
   * @return the named argument
   * @since 4.19.0
   */
  static @NotNull ComponentLike argument(final @TagPattern @NotNull String name, final @NotNull TranslationArgument argument) {
    return Component.virtual(Void.class, new NamedTranslationArgumentImpl(name, argument));
  }

  /**
   * The name of this translation argument.
   *
   * @return the name
   * @since 4.19.0
   */
  @TagPattern @NotNull String name();

  /**
   * The backing translation argument.
   *
   * @return the translation argument
   * @since 4.19.0
   **/
  @NotNull TranslationArgument translationArgument();
}
