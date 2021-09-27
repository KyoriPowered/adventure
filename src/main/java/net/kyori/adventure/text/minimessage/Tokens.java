/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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
package net.kyori.adventure.text.minimessage;

/**
 * Tokens used in the MiniMessage format.
 *
 * @since 4.0.0
 */
public final class Tokens {
  // vanilla components
  public static final String CLICK = "click";
  public static final String HOVER = "hover";
  public static final String KEYBIND = "key";
  public static final String TRANSLATABLE = "lang";
  public static final String TRANSLATABLE_2 = "translate";
  public static final String TRANSLATABLE_3 = "tr";
  public static final String INSERTION = "insert";
  public static final String COLOR = "color";
  public static final String COLOR_2 = "colour";
  public static final String COLOR_3 = "c";
  public static final String HEX = "#";
  public static final String FONT = "font";

  // vanilla decoration
  public static final String UNDERLINED = "underlined";
  public static final String UNDERLINED_2 = "u";
  public static final String STRIKETHROUGH = "strikethrough";
  public static final String STRIKETHROUGH_2 = "st";
  public static final String OBFUSCATED = "obfuscated";
  public static final String OBFUSCATED_2 = "obf";
  public static final String ITALIC = "italic";
  public static final String ITALIC_2 = "em";
  public static final String ITALIC_3 = "i";
  public static final String BOLD = "bold";
  public static final String BOLD_2 = "b";
  public static final String RESET = "reset";
  public static final String RESET_2 = "r";
  public static final String PRE = "pre";

  // minimessage components
  public static final String RAINBOW = "rainbow";
  public static final String REVERSE = "!";
  public static final String GRADIENT = "gradient";

  // minimessage tags
  public static final String TAG_START = "<";
  public static final String TAG_END = ">";
  public static final String CLOSE_TAG = "/";
  public static final String SEPARATOR = ":";

  private Tokens() {
  }
}
