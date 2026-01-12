/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Applies pride flags to a component.
 *
 * @since 4.18.0
 */
final class PrideTag extends GradientTag {
  static final String PRIDE = "pride";

  static final TagResolver RESOLVER = TagResolver.resolver(PRIDE, PrideTag::create);

  private static final Map<String, List<TextColor>> FLAGS;

  static {
    FLAGS = Map.ofEntries(
      // Colours taken from https://www.kapwing.com/resources/official-pride-colors-2021-exact-color-codes-for-15-pride-flags.
      Map.entry(PRIDE, colors(0xE50000, 0xFF8D00, 0xFFEE00, 0x28121, 0x004CFF, 0x770088)),
      Map.entry("progress", colors(0xFFFFFF, 0xFFAFC7, 0x73D7EE, 0x613915, 0x000000, 0xE50000, 0xFF8D00, 0xFFEE00, 0x28121, 0x004CFF, 0x770088)),
      Map.entry("trans", colors(0x5BCFFB, 0xF5ABB9, 0xFFFFFF, 0xF5ABB9, 0x5BCFFB)),
      Map.entry("bi", colors(0xD60270, 0x9B4F96, 0x0038A8)),
      Map.entry("pan", colors(0xFF1C8D, 0xFFD700, 0x1AB3FF)),
      Map.entry("nb", colors(0xFCF431, 0xFCFCFC, 0x9D59D2, 0x282828)),
      Map.entry("lesbian", colors(0xD62800, 0xFF9B56, 0xFFFFFF, 0xD462A6, 0xA40062)),
      Map.entry("ace", colors(0x000000, 0xA4A4A4, 0xFFFFFF, 0x810081)),
      Map.entry("agender", colors(0x000000, 0xBABABA, 0xFFFFFF, 0xBAF484, 0xFFFFFF, 0xBABABA, 0x000000)),
      Map.entry("demisexual", colors(0x000000, 0xFFFFFF, 0x6E0071, 0xD3D3D3)),
      Map.entry("genderqueer", colors(0xB57FDD, 0xFFFFFF, 0x49821E)),
      Map.entry("genderfluid", colors(0xFE76A2, 0xFFFFFF, 0xBF12D7, 0x000000, 0x303CBE)),
      Map.entry("intersex", colors(0xFFD800, 0x7902AA, 0xFFD800)),
      Map.entry("aro", colors(0x3BA740, 0xA8D47A, 0xFFFFFF, 0xABABAB, 0x000000)),

      Map.entry("femboy", colors(0xD260A5, 0xE4AFCD, 0xFEFEFE, 0x57CEF8, 0xFEFEFE, 0xE4AFCD, 0xD260A5)),

      // Colours taken from https://www.hrc.org/resources/lgbtq-pride-flags.
      Map.entry("baker", colors(0xCD66FF, 0xFF6599, 0xFE0000, 0xFE9900, 0xFFFF01, 0x009900, 0x0099CB, 0x350099, 0x990099)),
      Map.entry("philly", colors(0x000000, 0x784F17, 0xFE0000, 0xFD8C00, 0xFFE500, 0x119F0B, 0x0644B3, 0xC22EDC)),
      Map.entry("queer", colors(0x000000, 0x9AD9EA, 0x00A3E8, 0xB5E51D, 0xFFFFFF, 0xFFC90D, 0xFC6667, 0xFEAEC9, 0x000000)),
      Map.entry("gay", colors(0x078E70, 0x26CEAA, 0x98E8C1, 0xFFFFFF, 0x7BADE2, 0x5049CB, 0x3D1A78)),
      Map.entry("bigender", colors(0xC479A0, 0xECA6CB, 0xD5C7E8, 0xFFFFFF, 0xD5C7E8, 0x9AC7E8, 0x6C83CF)),
      Map.entry("demigender", colors(0x7F7F7F, 0xC3C3C3, 0xFBFF74, 0xFFFFFF, 0xFBFF74, 0xC3C3C3, 0x7F7F7F))
    );
  }

  static Tag create(final ArgumentQueue args, final Context ctx) {
    double phase = 0;
    String flag = PRIDE;

    if (args.hasNext()) {
      final String value = args.pop().value().toLowerCase(Locale.ROOT);
      if (FLAGS.containsKey(value)) {
        flag = value;
      } else if (!value.isEmpty()) {
        try {
          phase = Double.parseDouble(value);
        } catch (final NumberFormatException ex) {
          throw ctx.newException("Expected phase, got " + value);
        }

        if (phase < -1d || phase > 1d) {
          throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0, 1.0] (inclusive).", phase), args);
        }
      }
    }

    return new PrideTag(phase, FLAGS.get(flag), flag, ctx);
  }

  private final String flag;

  PrideTag(final double phase, final List<TextColor> colors, final String flag, final Context ctx) {
    super(phase, colors, ctx);
    this.flag = flag;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.flag, this.phase);
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof final PrideTag that)) return false;
    return this.phase == that.phase
      && this.flag.equals(that.flag);
  }

  @Override
  public String toString() {
    return "PrideTag{" +
      "flag='" + this.flag + '\'' +
      ", phase=" + this.phase +
      '}';
  }

  private static List<TextColor> colors(final int ... colors) {
    return Arrays.stream(colors).mapToObj(TextColor::color).collect(Collectors.toList());
  }
}
