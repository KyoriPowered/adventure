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
package net.kyori.adventure.nbt;

final class Tokens {
  // Compounds
  static final char COMPOUND_BEGIN = '{';
  static final char COMPOUND_END = '}';
  static final char COMPOUND_KEY_TERMINATOR = ':';

  // Arrays
  static final char ARRAY_BEGIN = '[';
  static final char ARRAY_END = ']';
  static final char ARRAY_SIGNATURE_SEPARATOR = ';';

  static final char VALUE_SEPARATOR = ',';

  static final char SINGLE_QUOTE = '\'';
  static final char DOUBLE_QUOTE = '"';
  static final char ESCAPE_MARKER = '\\';

  static final char TYPE_BYTE = 'b';
  static final char TYPE_SHORT = 's';
  static final char TYPE_INT = 'i'; // array only
  static final char TYPE_LONG = 'l';
  static final char TYPE_FLOAT = 'f';
  static final char TYPE_DOUBLE = 'd';

  static final char TYPE_SIGNED = 's';
  static final char TYPE_UNSIGNED = 'u';

  static final String LITERAL_TRUE = "true";
  static final String LITERAL_FALSE = "false";

  static final String NEWLINE = System.getProperty("line.separator", "\n");
  static final char EOF = '\0';

  private Tokens() {
  }

  /**
   * Return if a character is a valid component in an identifier.
   *
   * <p>An identifier character must match the expression {@code [a-zA-Z0-9_+.-]}</p>
   *
   * @param c the character
   * @return identifier
   */
  static boolean id(final char c) {
    return (c >= 'a' && c <= 'z')
      || (c >= 'A' && c <= 'Z')
      || (c >= '0' && c <= '9')
      || c == '-' || c == '_'
      || c == '.' || c == '+';
  }

  /**
   * Return whether a character is a numeric type identifier.
   *
   * @param c character to check
   * @return if a numeric type identifier
   */
  static boolean numericType(char c) {
    c = Character.toLowerCase(c);
    return c == TYPE_BYTE
      || c == TYPE_SHORT
      || c == TYPE_INT
      || c == TYPE_LONG
      || c == TYPE_FLOAT
      || c == TYPE_DOUBLE;
  }
}
