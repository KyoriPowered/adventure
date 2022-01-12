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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.kyori.adventure.text.minimessage.parser.node.TagPart;
import net.kyori.adventure.text.minimessage.transformation.Transformation;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

/**
 * A transformation applying a single css color.
 *
 * @since 4.10.0
 */
public final class CSSColorTransformation extends Transformation {

  public static final Map<String, TextColor> CSS_COLORS = new HashMap<>();

  static {
    CSS_COLORS.put("aliceblue", TextColor.color(0xf0f8ff));
    CSS_COLORS.put("antiquewhite", TextColor.color(0xfaebd7));
    CSS_COLORS.put("aqua", TextColor.color(0x00ffff));
    CSS_COLORS.put("aquamarine", TextColor.color(0x7fffd4));
    CSS_COLORS.put("azure", TextColor.color(0xf0ffff));
    CSS_COLORS.put("beige", TextColor.color(0xf5f5dc));
    CSS_COLORS.put("bisque", TextColor.color(0xffe4c4));
    CSS_COLORS.put("black", TextColor.color(0x000000));
    CSS_COLORS.put("blanchedalmond", TextColor.color(0xffebcd));
    CSS_COLORS.put("blue", TextColor.color(0x0000ff));
    CSS_COLORS.put("blueviolet", TextColor.color(0x8a2be2));
    CSS_COLORS.put("brown", TextColor.color(0xa52a2a));
    CSS_COLORS.put("burlywood", TextColor.color(0xdeb887));
    CSS_COLORS.put("cadetblue", TextColor.color(0x5f9ea0));
    CSS_COLORS.put("chartreuse", TextColor.color(0x7fff00));
    CSS_COLORS.put("chocolate", TextColor.color(0xd2691e));
    CSS_COLORS.put("coral", TextColor.color(0xff7f50));
    CSS_COLORS.put("cornflowerblue", TextColor.color(0x6495ed));
    CSS_COLORS.put("cornsilk", TextColor.color(0xfff8dc));
    CSS_COLORS.put("crimson", TextColor.color(0xdc143c));
    CSS_COLORS.put("cyan", TextColor.color(0x00ffff));
    CSS_COLORS.put("darkblue", TextColor.color(0x00008b));
    CSS_COLORS.put("darkcyan", TextColor.color(0x008b8b));
    CSS_COLORS.put("darkgoldenrod", TextColor.color(0xb8860b));
    CSS_COLORS.put("darkgray", TextColor.color(0xa9a9a9));
    CSS_COLORS.put("darkgreen", TextColor.color(0x006400));
    CSS_COLORS.put("darkgrey", TextColor.color(0xa9a9a9));
    CSS_COLORS.put("darkkhaki", TextColor.color(0xbdb76b));
    CSS_COLORS.put("darkmagenta", TextColor.color(0x8b008b));
    CSS_COLORS.put("darkolivegreen", TextColor.color(0x556b2f));
    CSS_COLORS.put("darkorange", TextColor.color(0xff8c00));
    CSS_COLORS.put("darkorchid", TextColor.color(0x9932cc));
    CSS_COLORS.put("darkred", TextColor.color(0x8b0000));
    CSS_COLORS.put("darksalmon", TextColor.color(0xe9967a));
    CSS_COLORS.put("darkseagreen", TextColor.color(0x8fbc8f));
    CSS_COLORS.put("darkslateblue", TextColor.color(0x483d8b));
    CSS_COLORS.put("darkslategray", TextColor.color(0x2f4f4f));
    CSS_COLORS.put("darkslategrey", TextColor.color(0x2f4f4f));
    CSS_COLORS.put("darkturquoise", TextColor.color(0x00ced1));
    CSS_COLORS.put("darkviolet", TextColor.color(0x9400d3));
    CSS_COLORS.put("deeppink", TextColor.color(0xff1493));
    CSS_COLORS.put("deepskyblue", TextColor.color(0x00bfff));
    CSS_COLORS.put("dimgray", TextColor.color(0x696969));
    CSS_COLORS.put("dimgrey", TextColor.color(0x696969));
    CSS_COLORS.put("dodgerblue", TextColor.color(0x1e90ff));
    CSS_COLORS.put("firebrick", TextColor.color(0xb22222));
    CSS_COLORS.put("floralwhite", TextColor.color(0xfffaf0));
    CSS_COLORS.put("forestgreen", TextColor.color(0x228b22));
    CSS_COLORS.put("fuchsia", TextColor.color(0xff00ff));
    CSS_COLORS.put("gainsboro", TextColor.color(0xdcdcdc));
    CSS_COLORS.put("ghostwhite", TextColor.color(0xf8f8ff));
    CSS_COLORS.put("gold", TextColor.color(0xffd700));
    CSS_COLORS.put("goldenrod", TextColor.color(0xdaa520));
    CSS_COLORS.put("gray", TextColor.color(0x808080));
    CSS_COLORS.put("green", TextColor.color(0x008000));
    CSS_COLORS.put("greenyellow", TextColor.color(0xadff2f));
    CSS_COLORS.put("grey", TextColor.color(0x808080));
    CSS_COLORS.put("honeydew", TextColor.color(0xf0fff0));
    CSS_COLORS.put("hotpink", TextColor.color(0xff69b4));
    CSS_COLORS.put("indianred", TextColor.color(0xcd5c5c));
    CSS_COLORS.put("indigo", TextColor.color(0x4b0082));
    CSS_COLORS.put("ivory", TextColor.color(0xfffff0));
    CSS_COLORS.put("khaki", TextColor.color(0xf0e68c));
    CSS_COLORS.put("lavender", TextColor.color(0xe6e6fa));
    CSS_COLORS.put("lavenderblush", TextColor.color(0xfff0f5));
    CSS_COLORS.put("lawngreen", TextColor.color(0x7cfc00));
    CSS_COLORS.put("lemonchiffon", TextColor.color(0xfffacd));
    CSS_COLORS.put("lightblue", TextColor.color(0xadd8e6));
    CSS_COLORS.put("lightcoral", TextColor.color(0xf08080));
    CSS_COLORS.put("lightcyan", TextColor.color(0xe0ffff));
    CSS_COLORS.put("lightgoldenrodyellow", TextColor.color(0xfafad2));
    CSS_COLORS.put("lightgray", TextColor.color(0xd3d3d3));
    CSS_COLORS.put("lightgreen", TextColor.color(0x90ee90));
    CSS_COLORS.put("lightgrey", TextColor.color(0xd3d3d3));
    CSS_COLORS.put("lightpink", TextColor.color(0xffb6c1));
    CSS_COLORS.put("lightsalmon", TextColor.color(0xffa07a));
    CSS_COLORS.put("lightseagreen", TextColor.color(0x20b2aa));
    CSS_COLORS.put("lightskyblue", TextColor.color(0x87cefa));
    CSS_COLORS.put("lightslategray", TextColor.color(0x778899));
    CSS_COLORS.put("lightslategrey", TextColor.color(0x778899));
    CSS_COLORS.put("lightsteelblue", TextColor.color(0xb0c4de));
    CSS_COLORS.put("lightyellow", TextColor.color(0xffffe0));
    CSS_COLORS.put("lime", TextColor.color(0x00ff00));
    CSS_COLORS.put("limegreen", TextColor.color(0x32cd32));
    CSS_COLORS.put("linen", TextColor.color(0xfaf0e6));
    CSS_COLORS.put("magenta", TextColor.color(0xff00ff));
    CSS_COLORS.put("maroon", TextColor.color(0x800000));
    CSS_COLORS.put("mediumaquamarine", TextColor.color(0x66cdaa));
    CSS_COLORS.put("mediumblue", TextColor.color(0x0000cd));
    CSS_COLORS.put("mediumorchid", TextColor.color(0xba55d3));
    CSS_COLORS.put("mediumpurple", TextColor.color(0x9370db));
    CSS_COLORS.put("mediumseagreen", TextColor.color(0x3cb371));
    CSS_COLORS.put("mediumslateblue", TextColor.color(0x7b68ee));
    CSS_COLORS.put("mediumspringgreen", TextColor.color(0x00fa9a));
    CSS_COLORS.put("mediumturquoise", TextColor.color(0x48d1cc));
    CSS_COLORS.put("mediumvioletred", TextColor.color(0xc71585));
    CSS_COLORS.put("midnightblue", TextColor.color(0x191970));
    CSS_COLORS.put("mintcream", TextColor.color(0xf5fffa));
    CSS_COLORS.put("mistyrose", TextColor.color(0xffe4e1));
    CSS_COLORS.put("moccasin", TextColor.color(0xffe4b5));
    CSS_COLORS.put("navajowhite", TextColor.color(0xffdead));
    CSS_COLORS.put("navy", TextColor.color(0x000080));
    CSS_COLORS.put("oldlace", TextColor.color(0xfdf5e6));
    CSS_COLORS.put("olive", TextColor.color(0x808000));
    CSS_COLORS.put("olivedrab", TextColor.color(0x6b8e23));
    CSS_COLORS.put("orange", TextColor.color(0xffa500));
    CSS_COLORS.put("orangered", TextColor.color(0xff4500));
    CSS_COLORS.put("orchid", TextColor.color(0xda70d6));
    CSS_COLORS.put("palegoldenrod", TextColor.color(0xeee8aa));
    CSS_COLORS.put("palegreen", TextColor.color(0x98fb98));
    CSS_COLORS.put("paleturquoise", TextColor.color(0xafeeee));
    CSS_COLORS.put("palevioletred", TextColor.color(0xdb7093));
    CSS_COLORS.put("papayawhip", TextColor.color(0xffefd5));
    CSS_COLORS.put("peachpuff", TextColor.color(0xffdab9));
    CSS_COLORS.put("peru", TextColor.color(0xcd853f));
    CSS_COLORS.put("pink", TextColor.color(0xffc0cb));
    CSS_COLORS.put("plum", TextColor.color(0xdda0dd));
    CSS_COLORS.put("powderblue", TextColor.color(0xb0e0e6));
    CSS_COLORS.put("purple", TextColor.color(0x800080));
    CSS_COLORS.put("red", TextColor.color(0xff0000));
    CSS_COLORS.put("rosybrown", TextColor.color(0xbc8f8f));
    CSS_COLORS.put("royalblue", TextColor.color(0x4169e1));
    CSS_COLORS.put("saddlebrown", TextColor.color(0x8b4513));
    CSS_COLORS.put("salmon", TextColor.color(0xfa8072));
    CSS_COLORS.put("sandybrown", TextColor.color(0xf4a460));
    CSS_COLORS.put("seagreen", TextColor.color(0x2e8b57));
    CSS_COLORS.put("seashell", TextColor.color(0xfff5ee));
    CSS_COLORS.put("sienna", TextColor.color(0xa0522d));
    CSS_COLORS.put("silver", TextColor.color(0xc0c0c0));
    CSS_COLORS.put("skyblue", TextColor.color(0x87ceeb));
    CSS_COLORS.put("slateblue", TextColor.color(0x6a5acd));
    CSS_COLORS.put("slategray", TextColor.color(0x708090));
    CSS_COLORS.put("slategrey", TextColor.color(0x708090));
    CSS_COLORS.put("snow", TextColor.color(0xfffafa));
    CSS_COLORS.put("springgreen", TextColor.color(0x00ff7f));
    CSS_COLORS.put("steelblue", TextColor.color(0x4682b4));
    CSS_COLORS.put("tan", TextColor.color(0xd2b48c));
    CSS_COLORS.put("teal", TextColor.color(0x008080));
    CSS_COLORS.put("thistle", TextColor.color(0xd8bfd8));
    CSS_COLORS.put("tomato", TextColor.color(0xff6347));
    CSS_COLORS.put("turquoise", TextColor.color(0x40e0d0));
    CSS_COLORS.put("violet", TextColor.color(0xee82ee));
    CSS_COLORS.put("wheat", TextColor.color(0xf5deb3));
    CSS_COLORS.put("white", TextColor.color(0xffffff));
    CSS_COLORS.put("whitesmoke", TextColor.color(0xf5f5f5));
    CSS_COLORS.put("yellow", TextColor.color(0xffff00));
    CSS_COLORS.put("yellowgreen", TextColor.color(0x9acd32));
  }

  private final TextColor color;

  /**
   * Get if this transformation can handle the provided tag name.
   *
   * @param name tag name to test
   * @return if this transformation is applicable
   * @since 4.10.0
   */
  public static boolean canParse(final String name) {
    return CSS_COLORS.containsKey(name) || name.equalsIgnoreCase("css");
  }

  /**
   * Create a new css color.
   *
   * @param name the tag name
   * @param args the tag arguments
   * @return a new transformation
   * @since 4.10.0
   */
  public static CSSColorTransformation create(final String name, final List<TagPart> args) {
    final TextColor color;
    if (name.equalsIgnoreCase("css")) {
      if (args.size() == 1) {
        color = CSS_COLORS.get(args.get(0).value().toLowerCase(Locale.ROOT));
      } else {
        throw new ParsingException("Expected to find a css color name, but found " + args, args);
      }
    } else {
      color = CSS_COLORS.get(name.toLowerCase(Locale.ROOT));
      if (color == null) {
        throw new ParsingException("Expected to find a css color name, but found " + name, args);
      }
    }
    return new CSSColorTransformation(color);
  }

  private CSSColorTransformation(final TextColor color) {
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
    final CSSColorTransformation that = (CSSColorTransformation) other;
    return Objects.equals(this.color, that.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color);
  }
}
