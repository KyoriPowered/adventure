/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * A transformation applying a single text color.
 *
 * @since 4.10.0
 */
public final class ColorTransformation extends Transformation {
  public static final String HEX = "#";
  public static final String COLOR_3 = "c";
  public static final String COLOR_2 = "colour";
  public static final String COLOR = "color";

  private static final Map<String, TextColor> COLOR_ALIASES = new HashMap<>();

  static {
    COLOR_ALIASES.put("dark_grey", NamedTextColor.DARK_GRAY);
    COLOR_ALIASES.put("grey", NamedTextColor.GRAY);
  }

  private final TextColor color;

  private static boolean isColorOrAbbreviation(final String name) {
    return name.equals(COLOR) || name.equals(COLOR_2) || name.equals(COLOR_3);
  }

  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.10.0
   */
  public static boolean canParse(final String name) {
    return isColorOrAbbreviation(name)
      || TextColor.fromHexString(name) != null
      || NamedTextColor.NAMES.value(name) != null
      || COLOR_ALIASES.containsKey(name);
  }

  /**
   * Create a new color name.
   *
   * @param name the tag name
   * @param args the tag arguments
   * @return a new transformation
   * @since 4.10.0
   */
  public static ColorTransformation create(final String name, final List<TagPart> args) {
    final String colorName;
    if (isColorOrAbbreviation(name)) {
      if (args.size() == 1) {
        colorName = args.get(0).value().toLowerCase(Locale.ROOT);
      } else {
        throw new ParsingException("Expected to find a color parameter, but found " + args, args);
      }
    } else {
      colorName = name;
    }

    final TextColor color;
    if (COLOR_ALIASES.containsKey(colorName)) {
      color = COLOR_ALIASES.get(colorName);
    } else if (colorName.charAt(0) == '#') {
      color = TextColor.fromHexString(colorName);
    } else {
      color = NamedTextColor.NAMES.value(colorName);
    }

    if (color == null) {
      throw new ParsingException("Don't know how to turn '" + name + "' into a color", args);
    }

    return new ColorTransformation(color);
  }

  /**
   * Create a new color transformation type for parsing the supplied color map.
   *
   * @param aliases map of color names to their {@link TextColor} values
   * @return a new color transformation
   * @since 4.10.0
   */
  public static TransformationType<ColorTransformation> color(final Map<String, TextColor> aliases) {
    return TransformationType.transformationType(
      TransformationType.acceptingNames(aliases.keySet()),
      (name, args) -> {
        final TextColor color = aliases.get(name.toLowerCase(Locale.ROOT));
        if (color == null) {
          throw new ParsingException("Expected to find a color name, but found " + name, args);
        }
        return new ColorTransformation(color);
      }
    );
  }

  /**
   * Create a new color transformation type for parsing the supplied color map
   * The identifier allows you to use an alias that is already present, such as 'gray',
   * by specifying 'identifier:gray' in the tag to ensure your supplied color is used.
   *
   * @param identifier the alias identifier
   * @param aliases map of color names to their {@link TextColor} values
   * @return a new color transformation
   * @since 4.10.0
   */
  public static TransformationType<ColorTransformation> color(final String identifier, final Map<String, TextColor> aliases) {
    final Set<String> names = new HashSet<>(aliases.keySet());
    names.add(identifier);
    return TransformationType.transformationType(
      TransformationType.acceptingNames(names),
      (name, args) -> {
        final TextColor color;
        if (name.equalsIgnoreCase(identifier)) {
          if (args.size() == 1) {
            color = aliases.get(args.get(0).value().toLowerCase(Locale.ROOT));
          } else {
            throw new ParsingException("Expected to find a color name, but found " + args, args);
          }
        } else {
          color = aliases.get(name.toLowerCase(Locale.ROOT));
          if (color == null) {
            throw new ParsingException("Expected to find a color name, but found " + name, args);
          }
        }
        return new ColorTransformation(color);
      }
    );
  }

  private ColorTransformation(final TextColor color) {
    this.color = color;
  }

  @Override
  public Component apply() {
    return Component.empty().color(this.color);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("color", this.color));
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    final ColorTransformation that = (ColorTransformation) other;
    return Objects.equals(this.color, that.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color);
  }
}
