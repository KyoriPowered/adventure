/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.serializer.legacy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.Index;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * This even more of a hack.
 */

/**
 * A <b>legacy</b> format.
 *
 * @since 4.0.0
 */
public final class LegacyFormat implements Examinable {
  static final Index<Character, LegacyFormat> INDEX;
  private static final LegacyFormat RESET = new LegacyFormat(true, 'r');
  private static final Index<NamedTextColor, LegacyFormat> COLORS_INDEX;
  private static final Index<TextDecoration, LegacyFormat> DECORATIONS_INDEX;

  static {
    final List<LegacyFormat> colors = new ArrayList<>(16);

    colors.add(new LegacyFormat(NamedTextColor.BLACK, '0'));
    colors.add(new LegacyFormat(NamedTextColor.DARK_BLUE, '1'));
    colors.add(new LegacyFormat(NamedTextColor.DARK_GREEN, '2'));
    colors.add(new LegacyFormat(NamedTextColor.DARK_AQUA, '3'));
    colors.add(new LegacyFormat(NamedTextColor.DARK_RED, '4'));
    colors.add(new LegacyFormat(NamedTextColor.DARK_PURPLE, '5'));
    colors.add(new LegacyFormat(NamedTextColor.GOLD, '6'));
    colors.add(new LegacyFormat(NamedTextColor.GRAY, '7'));
    colors.add(new LegacyFormat(NamedTextColor.DARK_GRAY, '8'));
    colors.add(new LegacyFormat(NamedTextColor.BLUE, '9'));
    colors.add(new LegacyFormat(NamedTextColor.GREEN, 'a'));
    colors.add(new LegacyFormat(NamedTextColor.AQUA, 'b'));
    colors.add(new LegacyFormat(NamedTextColor.RED, 'c'));
    colors.add(new LegacyFormat(NamedTextColor.LIGHT_PURPLE, 'd'));
    colors.add(new LegacyFormat(NamedTextColor.YELLOW, 'e'));
    colors.add(new LegacyFormat(NamedTextColor.WHITE, 'f'));

    final List<LegacyFormat> decorations = new ArrayList<>(5);

    decorations.add(new LegacyFormat(TextDecoration.OBFUSCATED, 'k'));
    decorations.add(new LegacyFormat(TextDecoration.BOLD, 'l'));
    decorations.add(new LegacyFormat(TextDecoration.STRIKETHROUGH, 'm'));
    decorations.add(new LegacyFormat(TextDecoration.UNDERLINED, 'n'));
    decorations.add(new LegacyFormat(TextDecoration.ITALIC, 'o'));

    final List<LegacyFormat> formats = new ArrayList<>(colors.size() + decorations.size() + 1);

    formats.addAll(colors);
    formats.addAll(decorations);
    formats.add(RESET);

    INDEX = Index.create(legacyFormat -> legacyFormat.character, formats);
    COLORS_INDEX = Index.create(legacyFormat -> legacyFormat.color, colors);
    DECORATIONS_INDEX = Index.create(legacyFormat -> legacyFormat.decoration, decorations);
  }

  private final char character;
  private final @Nullable NamedTextColor color;
  private final @Nullable TextDecoration decoration;
  private final boolean reset;

  /*
   * Separate constructors to ensure a format can never be more than one thing.
   */

  private LegacyFormat(final @Nullable NamedTextColor color, final char character) {
    this.character = character;
    this.color = color;
    this.decoration = null;
    this.reset = false;
  }

  private LegacyFormat(final @Nullable TextDecoration decoration, final char character) {
    this.character = character;
    this.color = null;
    this.decoration = decoration;
    this.reset = false;
  }

  private LegacyFormat(final boolean reset, final char character) {
    this.character = character;
    this.color = null;
    this.decoration = null;
    this.reset = reset;
  }

  /**
   * Gets a legacy format from a color.
   *
   * @param color the color
   * @return the legacy format
   * @since 4.9.0
   */
  public static @NotNull LegacyFormat legacyColor(final @NotNull NamedTextColor color) {
    final LegacyFormat value = COLORS_INDEX.value(Objects.requireNonNull(color, "color"));
    if (value == null) throw new IllegalArgumentException("Could not find a legacy format for " + color);
    return value;
  }

  /**
   * Gets a legacy format from a decoration.
   *
   * @param decoration the decoration
   * @return the legacy format
   * @since 4.9.0
   */
  public static @NotNull LegacyFormat legacyDecoration(final @NotNull TextDecoration decoration) {
    final LegacyFormat value = DECORATIONS_INDEX.value(Objects.requireNonNull(decoration, "decoration"));
    if (value == null) throw new IllegalArgumentException("Could not find a legacy format for " + decoration);
    return value;
  }

  /**
   * Gets the reset legacy format.
   *
   * @return the legacy format
   * @since 4.9.0
   */
  public static @NotNull LegacyFormat legacyReset() {
    return RESET;
  }

  /**
   * Gets a legacy format from a legacy character.
   *
   * @param character the character
   * @return the format, or {@code null} if there is no format for the provided character
   * @since 4.9.0
   */
  public static @Nullable LegacyFormat legacyCharacter(final char character) {
    return INDEX.value(character);
  }

  /**
   * Gets the color.
   *
   * @return the color
   * @since 4.0.0
   */
  public @Nullable TextColor color() {
    return this.color;
  }

  /**
   * Gets the decoration.
   *
   * @return the decoration
   * @since 4.0.0
   */
  public @Nullable TextDecoration decoration() {
    return this.decoration;
  }

  /**
   * Gets if this format is a reset.
   *
   * @return {@code true} if a reset, {@code false} otherwise
   * @since 4.0.0
   */
  public boolean reset() {
    return this.reset;
  }

  /**
   * Gets the legacy character.
   *
   * @return the character
   * @since 4.9.0
   */
  public char character() {
    return this.character;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final LegacyFormat that = (LegacyFormat) other;
    return this.color == that.color && this.decoration == that.decoration && this.reset == that.reset;
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(this.color);
    result = (31 * result) + Objects.hashCode(this.decoration);
    result = (31 * result) + Boolean.hashCode(this.reset);
    return result;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("color", this.color),
      ExaminableProperty.of("decoration", this.decoration),
      ExaminableProperty.of("reset", this.reset)
    );
  }
}
