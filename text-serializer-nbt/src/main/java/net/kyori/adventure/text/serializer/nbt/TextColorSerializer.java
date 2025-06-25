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
package net.kyori.adventure.text.serializer.nbt;

import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

final class TextColorSerializer {

  private TextColorSerializer() {
  }

  static @NotNull TextColor deserialize(@NotNull StringBinaryTag tag) {
    String value = tag.value();
    if (value.startsWith(TextColor.HEX_PREFIX)) {
      return TextColor.fromHexString(value);
    } else {
      return NamedTextColor.NAMES.value(value);
    }
  }

  static @NotNull StringBinaryTag serialize(@NotNull TextColor color) {
    String value = color instanceof NamedTextColor
      ? NamedTextColor.NAMES.keyOrThrow((NamedTextColor) color)
      : asUpperCaseHexString(color);
    return StringBinaryTag.stringBinaryTag(value);
  }

  private static String asUpperCaseHexString(final TextColor color) {
    return String.format(Locale.ROOT, "%c%06X", TextColor.HEX_CHARACTER, color.value()); // to be consistent with vanilla
  }
}
