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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
public final class StandardTags {

  private StandardTags() {
  }

  private static final TagResolver ALL = TagResolver.builder()
      .resolvers(
        HoverTag.RESOLVER,
        ClickTag.RESOLVER,
        ColorTagResolver.INSTANCE,
        KeybindTag.RESOLVER,
        TranslatableTag.RESOLVER,
        InsertionTag.RESOLVER,
        FontTag.RESOLVER,
        DecorationTag.RESOLVER,
        GradientTag.RESOLVER,
        RainbowTag.RESOLVER,
        ResetTag.RESOLVER,
        NewlineTag.RESOLVER
      )
      .build();

  /**
   * Get a resolver for all decoration tags.
   *
   * <p>This tag supports both standard names from {@link TextDecoration#NAMES} as well as a few aliases from {@link DecorationTag}.</p>
   *
   * @return a resolver for all decoration tags
   * @since 4.10.0
   */
  public static TagResolver decoration() {
    return DecorationTag.RESOLVER;
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
    return ColorTagResolver.INSTANCE;
  }

  /**
   * Get a resolver for the {@value HoverTag#HOVER} tag.
   *
   * @return a resolver for the {@value HoverTag#HOVER} tag
   * @since 4.10.0
   */
  public static TagResolver hoverEvent() {
    return HoverTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value ClickTag#CLICK} tag.
   *
   * @return a resolver for the {@value ClickTag#CLICK} tag
   * @since 4.10.0
   */
  public static TagResolver clickEvent() {
    return ClickTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value KeybindTag#KEYBIND} tag.
   *
   * @return a resolver for the {@value KeybindTag#KEYBIND} tag
   * @since 4.10.0
   */
  public static TagResolver keybind() {
    return KeybindTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value TranslatableTag#TRANSLATE} tag.
   *
   * <p>This tag also responds to {@value TranslatableTag#LANG} and {@value TranslatableTag#TR}.</p>
   *
   * @return a resolver for the {@value TranslatableTag#TRANSLATE} tag
   * @since 4.10.0
   */
  public static TagResolver translatable() {
    return TranslatableTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value InsertionTag#INSERTION} tag.
   *
   * @return a resolver for the {@value InsertionTag#INSERTION} tag
   * @since 4.10.0
   */
  public static TagResolver insertion() {
    return InsertionTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value FontTag#FONT} tag.
   *
   * @return a resolver for the {@value FontTag#FONT} tag
   * @since 4.10.0
   */
  public static TagResolver font() {
    return FontTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value GradientTag#GRADIENT} tag.
   *
   * @return a resolver for the {@value GradientTag#GRADIENT} tag
   * @since 4.10.0
   */
  public static TagResolver gradient() {
    return GradientTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value RainbowTag#RAINBOW} tag.
   *
   * @return a resolver for the {@value RainbowTag#RAINBOW} tag
   * @since 4.10.0
   */
  public static TagResolver rainbow() {
    return RainbowTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value ResetTag#RESET} tag.
   *
   * @return a resolver for the {@value ResetTag#RESET} tag.
   * @since 4.10.0
   */
  public static TagResolver reset() {
    return ResetTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value NewlineTag#NEWLINE} tag.
   *
   * <p>This tag also responds to {@value NewlineTag#BR}.</p>
   *
   * @return a resolver for the {@value NewlineTag#NEWLINE} tag.
   * @since 4.10.0
   */
  public static TagResolver newline() {
    return NewlineTag.RESOLVER;
  }

  /**
   * Get a resolver that handles all default standard tags.
   *
   * <p>This will currently return all standard tags, but in the future MiniMessage
   * may add tags that are not enabled by default, and which must explicitly be opted-in to.</p>
   *
   * @return the resolver for built-in tags
   * @since 4.10.0
   */
  public static TagResolver defaults() {
    return ALL;
  }

  static Set<String> names(final String... names) {
    return new HashSet<>(Arrays.asList(names));
  }
}
