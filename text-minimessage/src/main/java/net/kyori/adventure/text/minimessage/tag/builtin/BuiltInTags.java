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
package net.kyori.adventure.text.minimessage.tag.builtin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Tag types distributed with MiniMessage.
 *
 * <p>All built-in types are included in the default tag resolver.</p>
 *
 * @since 4.10.0
 */
public final class BuiltInTags {
  private BuiltInTags() {
  }

  private static final TagResolver DECORATION = Stream.concat(TextDecoration.NAMES.keyToValue().entrySet().stream(), DecorationTag.DECORATION_ALIASES.entrySet().stream())
    .flatMap(entry -> {
      return Stream.of(
        TagResolver.resolver(entry.getKey(), (args, ctx) -> DecorationTag.create(entry.getValue(), args, ctx)),
        TagResolver.resolver(DecorationTag.REVERT + entry.getKey(), DecorationTag.createNegated(entry.getValue()))
        );
    })
    .collect(TagResolver.toTagResolver());
  private static final TagResolver COLOR = new ColorTagResolver();
  private static final TagResolver HOVER_EVENT = TagResolver.resolver(HoverTag.HOVER, HoverTag::create);
  private static final TagResolver CLICK_EVENT = TagResolver.resolver(ClickTag.CLICK, ClickTag::create);
  private static final TagResolver KEYBIND = TagResolver.resolver(KeybindTag.KEYBIND, KeybindTag::create);
  private static final TagResolver TRANSLATABLE = TagResolver.resolver(
    names(TranslatableTag.TRANSLATABLE, TranslatableTag.TRANSLATABLE_2, TranslatableTag.TRANSLATABLE_3),
    TranslatableTag::create
  );
  private static final TagResolver INSERTION = TagResolver.resolver(InsertionTag.INSERTION, InsertionTag::create);
  private static final TagResolver FONT = TagResolver.resolver(FontTag.FONT, FontTag::create);
  private static final TagResolver GRADIENT = TagResolver.resolver(GradientTag.GRADIENT, GradientTag::create);
  private static final TagResolver RAINBOW = TagResolver.resolver(RainbowTag.RAINBOW, RainbowTag::create);

  /**
   * Get a resolver for the {@value HoverTag#HOVER} tag.
   *
   * @return a resolver for the {@value HoverTag#HOVER} tag
   * @since 4.10.0
   */
  public static TagResolver hoverEvent() {
    return HOVER_EVENT;
  }

  /**
   * Get a resolver for the {@value ClickTag#CLICK} tag.
   *
   * @return a resolver for the {@value ClickTag#CLICK} tag
   * @since 4.10.0
   */
  public static TagResolver clickEvent() {
    return CLICK_EVENT;
  }

  /**
   * Get a resolver for the {@value ColorTagResolver#COLOR} tags.
   *
   * <p>This tag supports both hex string colors as well as {@linkplain NamedTextColor named colors}.</p>
   *
   * @return a resolver for the {@value ColorTagResolver#COLOR} tags
   * @since 4.10.0
   */
  public static TagResolver color() {
    return COLOR;
  }

  /**
   * Get a resolver for the {@value KeybindTag#KEYBIND} tag.
   *
   * @return a resolver for the {@value KeybindTag#KEYBIND} tag
   * @since 4.10.0
   */
  public static TagResolver keybind() {
    return KEYBIND;
  }

  /**
   * Get a resolver for the {@value TranslatableTag#TRANSLATABLE} tag.
   *
   * <p>This tag also responds to {@value TranslatableTag#TRANSLATABLE_2} and {@value TranslatableTag#TRANSLATABLE_3}.</p>
   *
   * @return a resolver for the {@value TranslatableTag#TRANSLATABLE} tag
   * @since 4.10.0
   */
  public static TagResolver translatable() {
    return KEYBIND;
  }

  /**
   * Get a resolver for the {@value InsertionTag#INSERTION} tag.
   *
   * @return a resolver for the {@value InsertionTag#INSERTION} tag
   * @since 4.10.0
   */
  public static TagResolver insertion() {
    return INSERTION;
  }

  /**
   * Get a resolver for the {@value FontTag#FONT} tag.
   *
   * @return a resolver for the {@value FontTag#FONT} tag
   * @since 4.10.0
   */
  public static TagResolver font() {
    return FONT;
  }

  /**
   * Get a resolver for the {@value GradientTag#GRADIENT} tag.
   *
   * @return a resolver for the {@value GradientTag#GRADIENT} tag
   * @since 4.10.0
   */
  public static TagResolver gradient() {
    return GRADIENT;
  }

  /**
   * Get a resolver that handles all built-in tags.
   *
   * @return the resolver for built-in tags
   * @since 4.10.0
   */
  public static TagResolver all() {
    return TagResolver.builder()
      .resolvers(
        HOVER_EVENT,
        CLICK_EVENT,
        COLOR,
        KEYBIND,
        TRANSLATABLE,
        INSERTION,
        FONT,
        DECORATION,
        GRADIENT,
        RAINBOW
      )
      .build();
  }

  private static Set<String> names(final String... names) {
    return new HashSet<>(Arrays.asList(names));
  }
}
