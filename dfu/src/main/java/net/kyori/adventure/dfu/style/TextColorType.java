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
package net.kyori.adventure.dfu.style;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class TextColorType {
  public static final Codec<TextColor> CODEC = Codec.STRING.comapFlatMap(TextColorType::parse, TextColorType::getName);

  public static DataResult<TextColor> parse(final String name) {
    if (name.startsWith("#")) {
      try {
        int i = Integer.parseInt(name.substring(1), 16);
        return i >= 0 && i <= 16777215 ? DataResult.success(TextColor.color(i), Lifecycle.stable()) : DataResult.error(() -> {
          return "Color value out of range: " + name;
        });
      } catch (NumberFormatException var2) {
        return DataResult.error(() -> {
          return "Invalid color value: " + name;
        });
      }
    } else {
      TextColor textColor = NamedTextColor.NAMES.value(name);
      return textColor == null ? DataResult.error(() -> {
        return "Invalid color name: " + name;
      }) : DataResult.success(textColor, Lifecycle.stable());
    }
  }

  public static String getName(final TextColor input) {
    return input instanceof NamedTextColor ? ((NamedTextColor) input).toString() : input.asHexString();
  }
}
