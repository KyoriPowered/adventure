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

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ShadowColorTag {
  private static final String SHADOW_COLOR = "shadow";
  private static final String SHADOW_NONE = "!" + SHADOW_COLOR;
  private static final float DEFAULT_ALPHA = 0.25f;

  static final TagResolver RESOLVER = TagResolver.resolver(
    SerializableResolver.claimingStyle(
      SHADOW_COLOR,
      ShadowColorTag::create,
      StyleClaim.claim(SHADOW_COLOR, Style::shadowColor, ShadowColorTag::emit)
    ),
    TagResolver.resolver(SHADOW_NONE, Tag.styling(ShadowColor.none()))
  );

  static Tag create(final @NotNull ArgumentQueue args, final @NotNull Context ctx) throws ParsingException {
    final String colorString = args.popOr("Expected to find a color parameter: #RRGGBBAA").lowerValue();
    final ShadowColor color;
    if (colorString.startsWith(TextColor.HEX_PREFIX) && colorString.length() == 9) {
      color = ShadowColor.fromHexString(colorString);
      if (color == null) {
        throw ctx.newException(String.format("Unable to parse a shadow color from '%s'. Please use #RRGGBBAA formatting.", colorString));
      }
    } else {
      final TextColor text = ColorTagResolver.resolveColor(colorString, ctx);
      final float alpha = args.hasNext() ? (float) args.pop().asDouble().orElseThrow(() -> ctx.newException("Number was expected to be a double")) : DEFAULT_ALPHA;
      color = ShadowColor.shadowColor(text, (int) (alpha * 0xff));
    }

    return Tag.styling(color);
  }

  static void emit(final @NotNull ShadowColor color, final @NotNull TokenEmitter emitter) {
    if (ShadowColor.none().equals(color)) {
      emitter.tag(SHADOW_NONE);
      return;
    }

    emitter.tag(SHADOW_COLOR);

    final @Nullable NamedTextColor possibleMatch = NamedTextColor.namedColor(TextColor.color(color).value());
    if (possibleMatch != null) {
      emitter.argument(NamedTextColor.NAMES.key(possibleMatch)).argument(Float.toString((float) color.alpha() / 0xff));
    } else {
      emitter.argument(color.asHexString());
    }
  }

  private ShadowColorTag() {
  }
}
