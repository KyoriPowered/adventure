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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

public class ClickTransformation extends Transformation {

    private ClickEvent.Action action;
    private String value;

    @Override
    public void load(String name, List<Token> args) {
        super.load(name, args);

        if (args.size() != 3 || args.get(0).getType() != TokenType.STRING || args.get(2).getType() != TokenType.STRING) {
            throw new ParsingException("Doesn't know how to turn " + args + " into a click event", -1);
        }

        action = ClickEvent.Action.NAMES.value(args.get(0).getValue().toLowerCase(Locale.ROOT));
        value = args.get(2).getValue();
    }

    @Override
    public Component apply(Component component, TextComponent.Builder parent) {
        return component.clickEvent(ClickEvent.clickEvent(action, value));
    }

    public static boolean isApplicable(String name) {
        return name.equalsIgnoreCase(Tokens.CLICK);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClickTransformation that = (ClickTransformation) o;
        return action == that.action && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClickTransformation.class.getSimpleName() + "[", "]")
                .add("action=" + action)
                .add("value='" + value + "'")
                .toString();
    }
}
