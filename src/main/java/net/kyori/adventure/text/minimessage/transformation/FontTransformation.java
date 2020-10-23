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

import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

public class FontTransformation extends Transformation {
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.FONT);
  }

  private Key font;

  private FontTransformation() {
  }

  @Override
  public void load(final String name, final List<Token> args) {
    super.load(name, args);

    if(Token.oneString(args)) {
      this.font = Key.key(args.get(0).value());
    }

    if(args.size() != 3 || args.get(0).type() != TokenType.STRING || args.get(2).type() != TokenType.STRING) {
      throw new ParsingException("Doesn't know how to turn " + args + " into a click event", -1);
    }

    this.font = Key.key(args.get(0).value(), args.get(2).value());
  }

  @Override
  public Component apply(final Component component, final TextComponent.Builder parent) {
    return component.style(component.style().font(this.font));
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("font", this.font));
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final FontTransformation that = (FontTransformation) other;
    return Objects.equals(this.font, that.font);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.font);
  }

  static class Parser implements TransformationParser<FontTransformation> {
    @Override
    public FontTransformation parse() {
      return new FontTransformation();
    }
  }
}
