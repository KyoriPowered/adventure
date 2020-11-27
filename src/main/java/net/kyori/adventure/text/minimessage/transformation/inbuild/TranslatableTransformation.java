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

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;
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

/**
 * Insert a translation component into the result.
 *
 * @since 4.1.0
 */
public class TranslatableTransformation extends OneTimeTransformation implements Inserting {
  private static final Pattern DUM_SPLIT_PATTERN = Pattern.compile("['\"]:['\"]");

  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.1.0
   */
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.TRANSLATABLE)
           || name.equalsIgnoreCase(Tokens.TRANSLATABLE_2)
           || name.equalsIgnoreCase(Tokens.TRANSLATABLE_3);
  }

  private String key;
  private final List<Component> inners = new ArrayList<>();

  @Override
  public void load(final String name, final List<Token> args) {
    super.load(name, args);

    if(args.isEmpty() || args.get(0).type() != TokenType.STRING) {
      throw new ParsingException("Doesn't know how to turn " + args + " into a click event", -1);
    }

    this.key = args.get(0).value();
    if(args.size() > 1) {
      String string = Token.asValueString(args.subList(2, args.size()));
      if(string.startsWith("'") || string.startsWith("\"")) {
        string = string.substring(1).substring(0, string.length() - 2);
      }
      for(final String in : DUM_SPLIT_PATTERN.split(string)) {
        this.inners.add(MiniMessage.get().parse(in)); // TODO this uses a hardcoded instance, there gotta be a better way
      }
    }
  }

  @Override
  public Component applyOneTime(final @NonNull Component current, final TextComponent.@NonNull Builder parent, final @NonNull Deque<Transformation> transformations) {
    if(!this.inners.isEmpty()) {
      parent.append(this.merge(Component.translatable(this.key, this.inners), current));
    } else {
      parent.append(this.merge(Component.translatable(this.key), current));
    }
    return current;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
            ExaminableProperty.of("key", this.key),
            ExaminableProperty.of("inners", this.inners)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final TranslatableTransformation that = (TranslatableTransformation) other;
    return Objects.equals(this.key, that.key)
      && Objects.equals(this.inners, that.inners);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.key, this.inners);
  }

  /**
   * Factory for {@link TranslatableTransformation} instances.
   *
   * @since 4.1.0
   */
  public static class Parser implements TransformationParser<TranslatableTransformation> {
    @Override
    public TranslatableTransformation parse() {
      return new TranslatableTransformation();
    }
  }
}
