/*
 * This file is part of adventure-text-minimessage, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tokens;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Locale;
import java.util.StringJoiner;

public class MiniMessageToken {

    public static class Color extends Token {

        private NamedTextColor color;
        private boolean closed;

        public Color(String color, boolean closed) {
            super(0, "", 0);
            this.color = NamedTextColor.NAMES.value(color.toLowerCase(Locale.ROOT));
            this.closed = closed;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Color.class.getSimpleName() + "[", "]")
                    .add("color=" + color)
                    .add("closed=" + closed)
                    .toString();
        }
    }

    public static class Text extends Token {

        private String line;

        public Text(String line) {
            super(0, "", 0);
            this.line = line;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Text.class.getSimpleName() + "[", "]")
                    .add("line='" + line + "'")
                    .toString();
        }
    }
}
