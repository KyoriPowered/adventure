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
package net.kyori.adventure.text.minimessage.transformation.inbuild;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.Token;
import net.kyori.adventure.text.minimessage.parser.TokenType;
import net.kyori.adventure.text.minimessage.transformation.Inserting;
import net.kyori.adventure.text.minimessage.transformation.OneTimeTransformation;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TranslatableTransformation extends OneTimeTransformation implements Inserting {

  private static final Pattern dumSplitPattern = Pattern.compile("['\"]:['\"]");

  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.TRANSLATABLE) || name.equalsIgnoreCase(Tokens.TRANSLATABLE_2);
  }

  private String key;
  private final List<Component> inners = new ArrayList<>();

  @Override
  public void load(String name, List<Token> args) {
    super.load(name, args);

    if (args.isEmpty() || args.get(0).type() != TokenType.STRING) {
      throw new ParsingException("Doesn't know how to turn " + args + " into a click event", -1);
    }

    this.key = args.get(0).value();
    if (args.size() > 1) {
      String string = Token.asValueString(args.subList(2, args.size()));
      if (string.startsWith("'") || string.startsWith("\"")) {
        string = string.substring(1).substring(0, string.length() - 2);
      }
      for (String in : dumSplitPattern.split(string)) {
        inners.add(MiniMessage.get().parse(in)); // TODO this uses a hardcoded instance, there gotta be a better way
      }
    }
  }

  @Override
  public void applyOneTime(Component current, TextComponent.Builder parent, ArrayDeque<Transformation> transformations) {
    if (!inners.isEmpty()) {
      parent.append(Component.translatable(key, inners).mergeStyle(current));
    } else {
      parent.append(Component.translatable(key).mergeStyle(current));
    }
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
            ExaminableProperty.of("key", this.key),
            ExaminableProperty.of("inners", this.inners)
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TranslatableTransformation that = (TranslatableTransformation) o;
    return Objects.equals(key, that.key) && Objects.equals(inners, that.inners);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, inners);
  }

  public static class Parser implements TransformationParser<TranslatableTransformation> {
    @Override
    public TranslatableTransformation parse() {
      return new TranslatableTransformation();
    }
  }
}
