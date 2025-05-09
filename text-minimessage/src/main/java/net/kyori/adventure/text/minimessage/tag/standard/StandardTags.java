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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

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
        TranslatableFallbackTag.RESOLVER,
        InsertionTag.RESOLVER,
        FontTag.RESOLVER,
        DecorationTag.RESOLVER,
        GradientTag.RESOLVER,
        RainbowTag.RESOLVER,
        ResetTag.RESOLVER,
        NewlineTag.RESOLVER,
        TransitionTag.RESOLVER,
        SelectorTag.RESOLVER,
        ScoreTag.RESOLVER,
        NbtTag.RESOLVER,
        PrideTag.RESOLVER,
        ShadowColorTag.RESOLVER
      )
      .build();

  /**
   * Get a resolver for a specific text decoration.
   *
   * <p>This tag supports both the standard names from {@link TextDecoration#NAMES} as well as a few aliases from {@link DecorationTag}.</p>
   *
   * @param decoration the decoration to have a tag for
   * @return a resolver for a certain decoration's tags
   * @since 4.10.0
   */
  public static @NotNull TagResolver decorations(final @NotNull TextDecoration decoration) {
    return requireNonNull(DecorationTag.RESOLVERS.get(decoration), "No resolver found for decoration (this should not be possible?)");
  }

  /**
   * Get a resolver for all decoration tags.
   *
   * <p>This tag supports both standard names from {@link TextDecoration#NAMES} as well as a few aliases from {@link DecorationTag}.</p>
   *
   * @return a resolver for all decoration tags
   * @since 4.10.0
   */
  public static @NotNull TagResolver decorations() {
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
  public static @NotNull TagResolver color() {
    return ColorTagResolver.INSTANCE;
  }

  /**
   * Get a resolver for the {@value HoverTag#HOVER} tag.
   *
   * @return a resolver for the {@value HoverTag#HOVER} tag
   * @since 4.10.0
   */
  public static @NotNull TagResolver hoverEvent() {
    return HoverTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value ClickTag#CLICK} tag.
   *
   * @return a resolver for the {@value ClickTag#CLICK} tag
   * @since 4.10.0
   */
  public static @NotNull TagResolver clickEvent() {
    return ClickTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value KeybindTag#KEYBIND} tag.
   *
   * @return a resolver for the {@value KeybindTag#KEYBIND} tag
   * @since 4.10.0
   */
  public static @NotNull TagResolver keybind() {
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
  public static @NotNull TagResolver translatable() {
    return TranslatableTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value TranslatableFallbackTag#TRANSLATE_OR} tag.
   *
   * <p>This tag also responds to {@value TranslatableFallbackTag#LANG_OR} and {@value TranslatableFallbackTag#TR_OR}.</p>
   *
   * @return a resolver for the {@value TranslatableFallbackTag#TRANSLATE_OR} tag
   * @since 4.13.0
   */
  public static @NotNull TagResolver translatableFallback() {
    return TranslatableFallbackTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value InsertionTag#INSERTION} tag.
   *
   * @return a resolver for the {@value InsertionTag#INSERTION} tag
   * @since 4.10.0
   */
  public static @NotNull TagResolver insertion() {
    return InsertionTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value FontTag#FONT} tag.
   *
   * @return a resolver for the {@value FontTag#FONT} tag
   * @since 4.10.0
   */
  public static @NotNull TagResolver font() {
    return FontTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value GradientTag#GRADIENT} tag.
   *
   * @return a resolver for the {@value GradientTag#GRADIENT} tag
   * @since 4.10.0
   */
  public static @NotNull TagResolver gradient() {
    return GradientTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value RainbowTag#RAINBOW} tag.
   *
   * @return a resolver for the {@value RainbowTag#RAINBOW} tag
   * @since 4.10.0
   */
  public static @NotNull TagResolver rainbow() {
    return RainbowTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value TransitionTag#TRANSITION} tag.
   *
   * @return a resolver for the {@value TransitionTag#TRANSITION} tag
   * @since 4.10.0
   */
  public static TagResolver transition() {
    return TransitionTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value ResetTag#RESET} tag.
   *
   * @return a resolver for the {@value ResetTag#RESET} tag.
   * @since 4.10.0
   */
  public static @NotNull TagResolver reset() {
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
  public static @NotNull TagResolver newline() {
    return NewlineTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value SelectorTag#SELECTOR} tag.
   *
   * <p>This tag also responds to {@value SelectorTag#SEL}.</p>
   *
   * @return a resolver for the {@value SelectorTag#SELECTOR} tag
   * @since 4.11.0
   */
  public static @NotNull TagResolver selector() {
    return SelectorTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value ScoreTag#SCORE} tag.
   *
   * @return a resolver for the {@value ScoreTag#SCORE} tag
   * @since 4.13.0
   */
  public static @NotNull TagResolver score() {
    return ScoreTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value NbtTag#NBT} tag.
   *
   * <p>This tag also responds to {@value NbtTag#DATA}.</p>
   *
   * @return a resolver for the {@value NbtTag#NBT} tag.
   * @since 4.13.0
   */
  public static @NotNull TagResolver nbt() {
    return NbtTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value PrideTag#PRIDE} tag.
   *
   * @return a resolver for the {@value PrideTag#PRIDE} tag
   * @since 4.18.0
   */
  public static @NotNull TagResolver pride() {
    return PrideTag.RESOLVER;
  }

  /**
   * Get a resolver for the {@value ShadowColorTag#SHADOW_COLOR} tags.
   *
   * <p>This tag support both hex string</p>
   *
   * @return a resolver for the {@value ShadowColorTag#SHADOW_COLOR} tags
   * @since 4.18.0
   */
  public static @NotNull TagResolver shadowColor() {
    return ShadowColorTag.RESOLVER;
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
  public static @NotNull TagResolver defaults() {
    return ALL;
  }

  static Set<String> names(final String... names) {
    return new HashSet<>(Arrays.asList(names));
  }
}
