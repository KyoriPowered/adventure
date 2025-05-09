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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextFormat;

final class CharacterAndFormatSet {
  static final CharacterAndFormatSet DEFAULT = of(CharacterAndFormat.defaults());
  final List<TextFormat> formats;
  final List<TextColor> colors;
  final String characters;

  static CharacterAndFormatSet of(final List<CharacterAndFormat> pairs) {
    final int size = pairs.size();
    final List<TextColor> colors = new ArrayList<>();
    final List<TextFormat> formats = new ArrayList<>(size);
    final StringBuilder characters = new StringBuilder(size);
    for (int i = 0; i < size; i++) {
      final CharacterAndFormat pair = pairs.get(i);
      final char character = pair.character();
      final TextFormat format = pair.format();
      final boolean formatIsTextColor = format instanceof TextColor;

      // First, add the "standard" character.
      characters.append(character);
      formats.add(format);
      if (formatIsTextColor) {
        colors.add((TextColor) format);
      }

      // If the character is case-insensitive, we need to add the other character too.
      if (pair.caseInsensitive()) {
        boolean added = false;

        if (Character.isUpperCase(character)) {
          characters.append(Character.toLowerCase(character));
          added = true;
        } else if (Character.isLowerCase(character)) {
          characters.append(Character.toUpperCase(character));
          added = true;
        }

        if (added) {
          formats.add(format);
          if (formatIsTextColor) {
            colors.add((TextColor) format);
          }
        }
      }
    }
    if (formats.size() != characters.length()) {
      throw new IllegalStateException("formats length differs from characters length");
    }
    return new CharacterAndFormatSet(Collections.unmodifiableList(formats), Collections.unmodifiableList(colors), characters.toString());
  }

  CharacterAndFormatSet(final List<TextFormat> formats, final List<TextColor> colors, final String characters) {
    this.formats = formats;
    this.colors = colors;
    this.characters = characters;
  }
}
