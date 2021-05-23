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

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.Element;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A transformation that applies any {@link TextDecoration}.
 *
 * @since 4.1.0
 */
public final class DecorationTransformation extends Transformation {
  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.1.0
   */
  public static boolean canParse(final String name) {
    return parseDecoration(name) != null;
  }

  private TextDecoration decoration;

  private DecorationTransformation() {
  }

  @Override
  public void load(final String name, final List<Element.TagPart> args) {
    super.load(name, args);

    this.decoration = parseDecoration(name);

    if(this.decoration == null) {
      throw new ParsingException("Don't know how to turn '" + name + "' into a decoration", -1);
    }
  }

  private static TextDecoration parseDecoration(String name) {
    name = name.toLowerCase(Locale.ROOT);
    switch(name) {
      case Tokens.BOLD_2:
        name = Tokens.BOLD;
        break;
      case Tokens.ITALIC_2:
      case Tokens.ITALIC_3:
        name = Tokens.ITALIC;
        break;
      case Tokens.UNDERLINED_2:
        name = Tokens.UNDERLINED;
        break;
      case Tokens.STRIKETHROUGH_2:
        name = Tokens.STRIKETHROUGH;
        break;
      case Tokens.OBFUSCATED_2:
        name = Tokens.OBFUSCATED;
        break;
      default:
        break;
    }
    return TextDecoration.NAMES.value(name);
  }

  @Override
  public Component apply() {
    return Component.empty().decorate(this.decoration);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("decoration", this.decoration));
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final DecorationTransformation that = (DecorationTransformation) other;
    return this.decoration == that.decoration;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.decoration);
  }

  /**
   * Factory for {@link DecorationTransformation} instances.
   *
   * @since 4.1.0
   */
  public static class Parser implements TransformationParser<DecorationTransformation> {
    @Override
    public DecorationTransformation parse() {
      return new DecorationTransformation();
    }
  }
}
