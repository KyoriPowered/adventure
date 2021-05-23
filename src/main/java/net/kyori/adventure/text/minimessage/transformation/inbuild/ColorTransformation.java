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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Tokens;
import net.kyori.adventure.text.minimessage.parser.Element;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationParser;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A transformation applying a single text color.
 *
 * @since 4.1.0
 */
public final class ColorTransformation extends Transformation {

  private static Map<String, String> colorAliases = new HashMap<>();

  static {
    colorAliases.put("dark_grey", "dark_gray");
    colorAliases.put("grey", "gray");
  }

  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.1.0
   */
  public static boolean canParse(final String name) {
    return name.equalsIgnoreCase(Tokens.COLOR)
      || name.equalsIgnoreCase(Tokens.COLOR_2)
      || name.equalsIgnoreCase(Tokens.COLOR_3)
      || TextColor.fromHexString(name) != null
      || NamedTextColor.NAMES.value(name.toLowerCase(Locale.ROOT)) != null
      || colorAliases.containsKey(name);
  }

  private TextColor color;

  private ColorTransformation() {
  }

  @Override
  public void load(String name, final List<Element.TagPart> args) {
    super.load(name, args);

    if(name.equalsIgnoreCase(Tokens.COLOR)) {
      if(args.size() == 1) {
        name = args.get(0).getValue();
      } else {
        throw new ParsingException("Expected to find a color parameter, but found " + args, -1);
      }
    }

    if(colorAliases.containsKey(name)) {
      name = colorAliases.get(name);
    }

    if(name.charAt(0) == '#') {
      this.color = TextColor.fromHexString(name);
    } else {
      this.color = NamedTextColor.NAMES.value(name.toLowerCase(Locale.ROOT));
    }

    if(this.color == null) {
      throw new ParsingException("Don't know how to turn '" + name + "' into a color", -1);
    }
  }

  @Override
  public Component apply() {
    return Component.empty().color(this.color);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("color", this.color));
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final ColorTransformation that = (ColorTransformation) other;
    return Objects.equals(this.color, that.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color);
  }

  /**
   * Factory for {@link ColorTransformation} instances.
   *
   * @since 4.1.0
   */
  public static class Parser implements TransformationParser<ColorTransformation> {
    @Override
    public ColorTransformation parse() {
      return new ColorTransformation();
    }
  }
}
