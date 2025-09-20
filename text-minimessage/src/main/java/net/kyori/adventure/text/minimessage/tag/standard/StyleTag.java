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

import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.NamedArgumentMap;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.TriState;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Nullable;

/**
 * A style tag for setting multiple styles.
 *
 * @since 4.25.0
 */
final class StyleTag {
  private static final String STYLE = "style";

  static final TagResolver RESOLVER = SerializableResolver.claimingStyleNamed(
    STYLE,
    StyleTag::create,
    StyleClaim.claim(
      "style_tag",
      style -> style,
      style -> false, // this tag should never be emitted
      (style, emitter) -> {}
    )
  );

  private StyleTag() {
  }

  static Tag create(final NamedArgumentMap args, final Context ctx) throws ParsingException {
    final TriState bold = args.isFlagPresent("bold") ? args.flag("bold") : args.flag("b");
    final TriState italic = args.isFlagPresent("italic") ? args.flag("italic") : args.flag("i");
    final TriState underlined = args.isFlagPresent("underlined") ? args.flag("underlined") : args.flag("u");
    final TriState obfuscated = args.isFlagPresent("obfuscated") ? args.flag("obfuscated") : args.flag("obf");
    final TriState strikethrough = args.isFlagPresent("strikethrough") ? args.flag("strikethrough") : args.flag("st");

    final Tag.@Nullable Argument colorArgument = args.isPresent("color") ? args.get("color") : args.get("c");
    final TextColor color;
    if (colorArgument != null) {
      color = TextColor.fromCSSHexString(colorArgument.value());
      if (color == null) {
        throw ctx.newException("Color '" + colorArgument.value() + "' is in an invalid format. Please use #RRGGBB or #RGB.");
      }
    } else {
      color = null;
    }

    final Tag.@Nullable Argument shadowArgument = args.isPresent("shadow") ? args.get("shadow") : args.get("s");
    final ShadowColor shadow;
    if (shadowArgument != null) {
      final @Subst("#00000000") String value = shadowArgument.value();
      shadow = ShadowColor.fromHexString(value);
      if (shadow == null) {
        throw ctx.newException("Shadow color '" + value + "' is in an invalid format. Please use #RRGGBBAA.");
      }
    } else {
      shadow = null;
    }

    return Tag.styling(builder -> {
      builder.decoration(TextDecoration.BOLD, TextDecoration.State.byTriState(bold));
      builder.decoration(TextDecoration.ITALIC, TextDecoration.State.byTriState(italic));
      builder.decoration(TextDecoration.UNDERLINED, TextDecoration.State.byTriState(underlined));
      builder.decoration(TextDecoration.OBFUSCATED, TextDecoration.State.byTriState(obfuscated));
      builder.decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.byTriState(strikethrough));

      if (color != null) {
        builder.color(color);
      }

      if (shadow != null) {
        builder.shadowColor(shadow);
      }
    });
  }
}
