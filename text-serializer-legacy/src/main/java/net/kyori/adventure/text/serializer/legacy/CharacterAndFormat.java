/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A combination of a {@code character} and a {@link TextFormat}.
 *
 * @since 4.14.0
 */
public final class CharacterAndFormat {
  /**
   * Character and format pair representing {@link NamedTextColor#BLACK}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat BLACK = new CharacterAndFormat('0', NamedTextColor.BLACK);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_BLUE}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat DARK_BLUE = new CharacterAndFormat('1', NamedTextColor.DARK_BLUE);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_GREEN}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat DARK_GREEN = new CharacterAndFormat('2', NamedTextColor.DARK_GREEN);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_AQUA}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat DARK_AQUA = new CharacterAndFormat('3', NamedTextColor.DARK_AQUA);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_RED}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat DARK_RED = new CharacterAndFormat('4', NamedTextColor.DARK_RED);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_PURPLE}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat DARK_PURPLE = new CharacterAndFormat('5', NamedTextColor.DARK_PURPLE);
  /**
   * Character and format pair representing {@link NamedTextColor#GOLD}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat GOLD = new CharacterAndFormat('6', NamedTextColor.GOLD);
  /**
   * Character and format pair representing {@link NamedTextColor#GRAY}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat GRAY = new CharacterAndFormat('7', NamedTextColor.GRAY);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_GRAY}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat DARK_GRAY = new CharacterAndFormat('8', NamedTextColor.DARK_GRAY);
  /**
   * Character and format pair representing {@link NamedTextColor#BLUE}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat BLUE = new CharacterAndFormat('9', NamedTextColor.BLUE);
  /**
   * Character and format pair representing {@link NamedTextColor#GREEN}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat GREEN = new CharacterAndFormat('a', NamedTextColor.GREEN);
  /**
   * Character and format pair representing {@link NamedTextColor#AQUA}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat AQUA = new CharacterAndFormat('b', NamedTextColor.AQUA);
  /**
   * Character and format pair representing {@link NamedTextColor#RED}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat RED = new CharacterAndFormat('c', NamedTextColor.RED);
  /**
   * Character and format pair representing {@link NamedTextColor#LIGHT_PURPLE}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat LIGHT_PURPLE = new CharacterAndFormat('d', NamedTextColor.LIGHT_PURPLE);
  /**
   * Character and format pair representing {@link NamedTextColor#YELLOW}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat YELLOW = new CharacterAndFormat('e', NamedTextColor.YELLOW);
  /**
   * Character and format pair representing {@link NamedTextColor#WHITE}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat WHITE = new CharacterAndFormat('f', NamedTextColor.WHITE);

  /**
   * Character and format pair representing {@link TextDecoration#OBFUSCATED}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat OBFUSCATED = new CharacterAndFormat('k', TextDecoration.OBFUSCATED);
  /**
   * Character and format pair representing {@link TextDecoration#BOLD}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat BOLD = new CharacterAndFormat('l', TextDecoration.BOLD);
  /**
   * Character and format pair representing {@link TextDecoration#STRIKETHROUGH}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat STRIKETHROUGH = new CharacterAndFormat('m', TextDecoration.STRIKETHROUGH);
  /**
   * Character and format pair representing {@link TextDecoration#UNDERLINED}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat UNDERLINED = new CharacterAndFormat('n', TextDecoration.UNDERLINED);
  /**
   * Character and format pair representing {@link TextDecoration#ITALIC}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat ITALIC = new CharacterAndFormat('o', TextDecoration.ITALIC);

  /**
   * Character and format pair representing {@link Reset#INSTANCE}.
   *
   * @since 4.14.0
   */
  public static final CharacterAndFormat RESET = new CharacterAndFormat('r', Reset.INSTANCE);

  /**
   * A list of character and format pairs containing all default vanilla formats.
   *
   * @since 4.14.0
   */
  public static final List<CharacterAndFormat> DEFAULTS = createDefault();

  private final char character;
  private final TextFormat format;

  @SuppressWarnings("DuplicatedCode")
  private static List<CharacterAndFormat> createDefault() {
    final List<CharacterAndFormat> formats = new ArrayList<>(16 + 5 + 1); // colours + decorations + reset

    formats.add(BLACK);
    formats.add(DARK_BLUE);
    formats.add(DARK_GREEN);
    formats.add(DARK_AQUA);
    formats.add(DARK_RED);
    formats.add(DARK_PURPLE);
    formats.add(GOLD);
    formats.add(GRAY);
    formats.add(DARK_GRAY);
    formats.add(BLUE);
    formats.add(GREEN);
    formats.add(AQUA);
    formats.add(RED);
    formats.add(LIGHT_PURPLE);
    formats.add(YELLOW);
    formats.add(WHITE);

    formats.add(OBFUSCATED);
    formats.add(BOLD);
    formats.add(STRIKETHROUGH);
    formats.add(UNDERLINED);
    formats.add(ITALIC);

    formats.add(RESET);

    return Collections.unmodifiableList(formats);
  }

  /**
   * Creates a new combination of a {@code character} and a {@link TextFormat}.
   *
   * @param character the character
   * @param format the format
   * @since 4.14.0
   */
  public CharacterAndFormat(final char character, final @NotNull TextFormat format) {
    this.character = character;
    this.format = requireNonNull(format, "format");
  }

  /**
   * Gets the character.
   *
   * @return the character
   * @since 4.14.0
   */
  public char character() {
    return this.character;
  }

  /**
   * Gets the format.
   *
   * @return the format
   * @since 4.14.0
   */
  public @NotNull TextFormat format() {
    return this.format;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof CharacterAndFormat)) return false;
    final CharacterAndFormat that = (CharacterAndFormat) other;
    return this.character == that.character
      && this.format.equals(that.format);
  }

  @Override
  public int hashCode() {
    int result = this.character;
    result = 31 * result + this.format.hashCode();
    return result;
  }

  @Override
  public @NotNull String toString() {
    return "CharacterAndFormat{" +
      "character=" + this.character +
      ", format=" + this.format +
      '}';
  }
}
