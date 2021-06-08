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
 * MiniMessage's original markdown flavour.
 *
 * @deprecated The legacy flavor never made any sense, don't use
 * @since 4.1.0
 */
@Deprecated
public final class LegacyFlavor implements MarkdownFlavor {
  private static final LegacyFlavor INSTANCE = new LegacyFlavor();

  private LegacyFlavor() {
  }

  /**
   * Get an instance of the legacy markdown flavour.
   *
   * @return the flavour instance
   * @since 4.1.0
   */
  public static MarkdownFlavor get() {
    return INSTANCE;
  }

  @Override
  public boolean isBold(final char current, final char next) {
    return (current == '*' && next == current) || (current == '_' && next == current);
  }

  @Override
  public boolean isItalic(final char current, final char next) {
    return (current == '*' && next != current) || (current == '_' && next != current);
  }

  @Override
  public boolean isUnderline(final char current, final char next) {
    return current == '~' && next == current;
  }

  @Override
  public boolean isStrikeThrough(final char current, final char next) {
    return false;
  }

  @Override
  public boolean isObfuscate(final char current, final char next) {
    return current == '|' && next == current;
  }
}
