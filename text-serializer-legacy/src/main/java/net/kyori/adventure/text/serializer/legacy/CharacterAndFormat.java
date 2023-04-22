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

import static java.util.Objects.requireNonNull;

/**
 * A combination of a {@code character} and a {@link TextFormat}.
 *
 * @since 4.14.0
 */
public final class CharacterAndFormat {
  /**
   * A list of vanilla default formats.
   *
   * @since 4.14.0
   */
  public static final List<CharacterAndFormat> DEFAULT = createDefault();
  private final char character;
  private final TextFormat format;

  private static List<CharacterAndFormat> createDefault() {
    final List<CharacterAndFormat> formats = new ArrayList<>(16 + 5 + 1); // colours + decorations + reset

    formats.add(new CharacterAndFormat('0', NamedTextColor.BLACK));
    formats.add(new CharacterAndFormat('1', NamedTextColor.DARK_BLUE));
    formats.add(new CharacterAndFormat('2', NamedTextColor.DARK_GREEN));
    formats.add(new CharacterAndFormat('3', NamedTextColor.DARK_AQUA));
    formats.add(new CharacterAndFormat('4', NamedTextColor.DARK_RED));
    formats.add(new CharacterAndFormat('5', NamedTextColor.DARK_PURPLE));
    formats.add(new CharacterAndFormat('6', NamedTextColor.GOLD));
    formats.add(new CharacterAndFormat('7', NamedTextColor.GRAY));
    formats.add(new CharacterAndFormat('8', NamedTextColor.DARK_GRAY));
    formats.add(new CharacterAndFormat('9', NamedTextColor.BLUE));
    formats.add(new CharacterAndFormat('a', NamedTextColor.GREEN));
    formats.add(new CharacterAndFormat('b', NamedTextColor.AQUA));
    formats.add(new CharacterAndFormat('c', NamedTextColor.RED));
    formats.add(new CharacterAndFormat('d', NamedTextColor.LIGHT_PURPLE));
    formats.add(new CharacterAndFormat('e', NamedTextColor.YELLOW));
    formats.add(new CharacterAndFormat('f', NamedTextColor.WHITE));

    formats.add(new CharacterAndFormat('k', TextDecoration.OBFUSCATED));
    formats.add(new CharacterAndFormat('l', TextDecoration.BOLD));
    formats.add(new CharacterAndFormat('m', TextDecoration.STRIKETHROUGH));
    formats.add(new CharacterAndFormat('n', TextDecoration.UNDERLINED));
    formats.add(new CharacterAndFormat('o', TextDecoration.ITALIC));

    formats.add(new CharacterAndFormat('r', Reset.INSTANCE));

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
}
