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
package net.kyori.adventure.text.minimessage.transformation;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class FontTransformation extends Transformation {

    private Key font;

    @Override
    public void load(String name, List<Token> args) {
        super.load(name, args);

        if (args.size() == 1 && args.get(0).getType() == TokenType.STRING) {
            font = Key.key(args.get(0).getValue());
        }

        if (args.size() != 3 || args.get(0).getType() != TokenType.STRING || args.get(2).getType() != TokenType.STRING) {
            throw new ParsingException("Doesn't know how to turn " + args + " into a click event", -1);
        }

        font = Key.key(args.get(0).getValue(), args.get(2).getValue());
    }

    @Override
    public Component apply(Component component, TextComponent.Builder parent) {
        return component.style(component.style().font(font));
    }

    public static boolean isApplicable(String name) {
        return name.equalsIgnoreCase(Tokens.FONT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontTransformation that = (FontTransformation) o;
        return Objects.equals(font, that.font);
    }

    @Override
    public int hashCode() {
        return Objects.hash(font);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FontTransformation.class.getSimpleName() + "[", "]")
                .add("font=" + font)
                .toString();
    }
}
