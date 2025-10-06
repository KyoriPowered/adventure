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
package net.kyori.adventure.text.serializer.legacy;

import java.util.List;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.TextFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record CharacterAndFormatImpl(char character, TextFormat format, boolean caseInsensitive) implements CharacterAndFormat {
  CharacterAndFormatImpl(final char character, final @NotNull TextFormat format, final boolean caseInsensitive) {
    this.character = character;
    this.format = requireNonNull(format, "format");
    this.caseInsensitive = caseInsensitive;
  }

  @Override
  public @NotNull TextFormat format() {
    return this.format;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CharacterAndFormatImpl(char otherCharacter, TextFormat otherFormat, boolean otherCaseInsensitive))) {
      return false;
    }
    return this.character == otherCharacter
      && this.format.equals(otherFormat)
      && this.caseInsensitive == otherCaseInsensitive;
  }

  @Override
  public int hashCode() {
    int result = this.character;
    result = 31 * result + this.format.hashCode();
    result = 31 * result + Boolean.hashCode(this.caseInsensitive);
    return result;
  }

  @Override
  public @NotNull String toString() {
    return Internals.toString(this);
  }

  static final class Defaults {
    static final List<CharacterAndFormat> DEFAULTS = createDefaults();

    private Defaults() {
    }

    static List<CharacterAndFormat> createDefaults() {
      return List.of(
        BLACK,
        DARK_BLUE,
        DARK_GREEN,
        DARK_AQUA,
        DARK_RED,
        DARK_PURPLE,
        GOLD,
        GRAY,
        DARK_GRAY,
        BLUE,
        GREEN,
        AQUA,
        RED,
        LIGHT_PURPLE,
        YELLOW,
        WHITE,

        OBFUSCATED,
        BOLD,
        STRIKETHROUGH,
        UNDERLINED,
        ITALIC,

        RESET
      );
    }
  }
}
