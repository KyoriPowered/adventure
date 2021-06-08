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
package net.kyori.adventure.text.minimessage.markdown;

/**
 * A type of markdown.
 *
 * @since 4.1.0
 */
public interface MarkdownFlavor {

  /**
   * Get the default markdown flavour.
   *
   * <p>This is currently the {@link LegacyFlavor} for backwards compatibility,
   * but will be changed to {@link DiscordFlavor in the near future}.</p>
   *
   * @return the default flavour
   * @since 4.1.0
   */
  static MarkdownFlavor defaultFlavor() {
    return LegacyFlavor.get(); // TODO: change the default to DiscordFlavor in a few releases
  }

  /**
   * Whether a pair of format characters indicate bolding.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit a bold block
   * @since 4.1.0
   */
  boolean isBold(final char current, final char next);

  /**
   * Whether a pair of format characters indicate italics.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit an italics block
   * @since 4.1.0
   */
  boolean isItalic(final char current, final char next);

  /**
   * Whether a pair of format characters indicate an underline.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit an underlined block
   * @since 4.1.0
   */
  boolean isUnderline(final char current, final char next);

  /**
   * Whether a pair of format characters indicate a strikethrough.
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit a strikethrough block
   * @since 4.1.0
   */
  boolean isStrikeThrough(final char current, final char next);

  /**
   * Whether a pair of format characters indicate an obfuscated block.
   *
   * <p>These may also be described as spoiler blocks.</p>
   *
   * @param current the character being inspected
   * @param next the next character
   * @return whether the characters delimit an obfuscated block
   * @since 4.1.0
   */
  boolean isObfuscate(final char current, final char next);
}
