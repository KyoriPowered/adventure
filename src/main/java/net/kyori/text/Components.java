/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2018 KyoriPowered
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
package net.kyori.text;

public interface Components {
  /**
   * Gets an immutable component with empty content.
   *
   * @return a component with empty content
   */
  static Component empty() {
    return Components0.EMPTY;
  }

  /**
   * Gets an immutable component with a new line character as the content.
   *
   * @return a component with a new line character as the content
   */
  static Component newline() {
    return Components0.NEWLINE;
  }

  /**
   * Gets an immutable component with a single space as the content.
   *
   * @return a component with a single space as the content
   */
  static Component space() {
    return Components0.SPACE;
  }
}

final class Components0 {
  /**
   * A component with empty content.
   */
  static final Component EMPTY = TextComponent.of("");
  /**
   * A component with a new line character as the content.
   */
  static final Component NEWLINE = TextComponent.of("\n");
  /**
   * A component with a single space as the content.
   */
  static final Component SPACE = TextComponent.of(" ");
}
