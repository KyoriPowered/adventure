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

import java.util.List;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A combination of a {@code character} and a {@link TextFormat}.
 *
 * @since 4.14.0
 */
@ApiStatus.NonExtendable
public interface CharacterAndFormat extends Examinable {
  /**
   * Character and format pair representing {@link NamedTextColor#BLACK}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat BLACK = characterAndFormat('0', NamedTextColor.BLACK);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_BLUE}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat DARK_BLUE = characterAndFormat('1', NamedTextColor.DARK_BLUE);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_GREEN}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat DARK_GREEN = characterAndFormat('2', NamedTextColor.DARK_GREEN);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_AQUA}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat DARK_AQUA = characterAndFormat('3', NamedTextColor.DARK_AQUA);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_RED}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat DARK_RED = characterAndFormat('4', NamedTextColor.DARK_RED);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_PURPLE}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat DARK_PURPLE = characterAndFormat('5', NamedTextColor.DARK_PURPLE);
  /**
   * Character and format pair representing {@link NamedTextColor#GOLD}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat GOLD = characterAndFormat('6', NamedTextColor.GOLD);
  /**
   * Character and format pair representing {@link NamedTextColor#GRAY}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat GRAY = characterAndFormat('7', NamedTextColor.GRAY);
  /**
   * Character and format pair representing {@link NamedTextColor#DARK_GRAY}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat DARK_GRAY = characterAndFormat('8', NamedTextColor.DARK_GRAY);
  /**
   * Character and format pair representing {@link NamedTextColor#BLUE}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat BLUE = characterAndFormat('9', NamedTextColor.BLUE);
  /**
   * Character and format pair representing {@link NamedTextColor#GREEN}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat GREEN = characterAndFormat('a', NamedTextColor.GREEN);
  /**
   * Character and format pair representing {@link NamedTextColor#AQUA}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat AQUA = characterAndFormat('b', NamedTextColor.AQUA);
  /**
   * Character and format pair representing {@link NamedTextColor#RED}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat RED = characterAndFormat('c', NamedTextColor.RED);
  /**
   * Character and format pair representing {@link NamedTextColor#LIGHT_PURPLE}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat LIGHT_PURPLE = characterAndFormat('d', NamedTextColor.LIGHT_PURPLE);
  /**
   * Character and format pair representing {@link NamedTextColor#YELLOW}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat YELLOW = characterAndFormat('e', NamedTextColor.YELLOW);
  /**
   * Character and format pair representing {@link NamedTextColor#WHITE}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat WHITE = characterAndFormat('f', NamedTextColor.WHITE);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MINECOIN_GOLD}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MINECOIN_GOLD = characterAndFormat('g', BedrockNamedTextColor.MINECOIN_GOLD);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_QUARTZ}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_QUARTZ = characterAndFormat('h', BedrockNamedTextColor.MATERIAL_QUARTZ);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_IRON}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_IRON = characterAndFormat('i', BedrockNamedTextColor.MATERIAL_IRON);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_NETHERITE}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_NETHERITE = characterAndFormat('j', BedrockNamedTextColor.MATERIAL_NETHERITE);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_REDSTONE}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_REDSTONE = characterAndFormat('m', BedrockNamedTextColor.MATERIAL_REDSTONE);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_COPPER}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_COPPER = characterAndFormat('n', BedrockNamedTextColor.MATERIAL_COPPER);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_GOLD}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_GOLD = characterAndFormat('p', BedrockNamedTextColor.MATERIAL_GOLD);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_EMERALD}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_EMERALD = characterAndFormat('q', BedrockNamedTextColor.MATERIAL_EMERALD);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_DIAMOND}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_DIAMOND = characterAndFormat('s', BedrockNamedTextColor.MATERIAL_DIAMOND);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_LAPIS}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_LAPIS = characterAndFormat('t', BedrockNamedTextColor.MATERIAL_LAPIS);

  /**
   * Character and format pair representing {@link BedrockNamedTextColor#MATERIAL_AMETHYST}.
   *
   * @since 4.15.0
   */
  CharacterAndFormat MATERIAL_AMETHYST = characterAndFormat('u', BedrockNamedTextColor.MATERIAL_AMETHYST);

  /**
   * Character and format pair representing {@link TextDecoration#OBFUSCATED}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat OBFUSCATED = characterAndFormat('k', TextDecoration.OBFUSCATED);
  /**
   * Character and format pair representing {@link TextDecoration#BOLD}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat BOLD = characterAndFormat('l', TextDecoration.BOLD);
  /**
   * Character and format pair representing {@link TextDecoration#STRIKETHROUGH}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat STRIKETHROUGH = characterAndFormat('m', TextDecoration.STRIKETHROUGH);
  /**
   * Character and format pair representing {@link TextDecoration#UNDERLINED}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat UNDERLINED = characterAndFormat('n', TextDecoration.UNDERLINED);
  /**
   * Character and format pair representing {@link TextDecoration#ITALIC}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat ITALIC = characterAndFormat('o', TextDecoration.ITALIC);

  /**
   * Character and format pair representing {@link Reset#INSTANCE}.
   *
   * @since 4.14.0
   */
  CharacterAndFormat RESET = characterAndFormat('r', Reset.INSTANCE);

  /**
   * Creates a new combination of a {@code character} and a {@link TextFormat}.
   *
   * @param character the character
   * @param format the format
   * @return a new character and format pair.
   * @since 4.14.0
   */
  static @NotNull CharacterAndFormat characterAndFormat(final char character, final @NotNull TextFormat format) {
    return new CharacterAndFormatImpl(character, format);
  }

  /**
   * Gets an unmodifiable list of character and format pairs containing all default vanilla formats.
   *
   * @return am unmodifiable list of character and format pairs containing all default vanilla formats
   * @since 4.14.0
   * @deprecated for removal since 4.15.0, use {@link #javaDefaults()} instead
   */
  @Deprecated
  @Unmodifiable
  static @NotNull List<CharacterAndFormat> defaults() {
    return javaDefaults();
  }

  /**
   * Gets an unmodifiable list of character and format pairs containing all default vanilla java formats.
   *
   * @return am unmodifiable list of character and format pairs containing all default vanilla java formats
   * @since 4.15.0
   */
  @Unmodifiable
  static @NotNull List<CharacterAndFormat> javaDefaults() {
    return CharacterAndFormatImpl.Defaults.JAVA;
  }

  /**
   * Gets an unmodifiable list of character and format pairs containing all default vanilla bedrock formats.
   *
   * @return am unmodifiable list of character and format pairs containing all default vanilla bedrock formats
   * @since 4.14.0
   */
  @Unmodifiable
  static @NotNull List<CharacterAndFormat> bedrockDefaults() {
    return CharacterAndFormatImpl.Defaults.BEDROCK;
  }

  /**
   * Gets the character.
   *
   * @return the character
   * @since 4.14.0
   */
  char character();

  /**
   * Gets the format.
   *
   * @return the format
   * @since 4.14.0
   */
  @NotNull TextFormat format();

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("character", this.character()),
      ExaminableProperty.of("format", this.format())
    );
  }
}
