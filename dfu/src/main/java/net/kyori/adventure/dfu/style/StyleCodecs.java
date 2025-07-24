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
package net.kyori.adventure.dfu.style;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.dfu.Codecs;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class StyleCodecs {
  public static final MapCodec<Style> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TextColorType.CODEC.optionalFieldOf("color").forGetter(style -> Optional.ofNullable(style.color())), Codecs.SHADOW_COLOR.optionalFieldOf("shadow_color").forGetter(style -> Optional.ofNullable(style.shadowColor())), Codec.BOOL.optionalFieldOf("bold").forGetter(style -> {
      TextDecoration.State state = style.decoration(TextDecoration.BOLD);
      if (state == TextDecoration.State.NOT_SET) {
        return Optional.empty();
      }
      return Optional.of(state == TextDecoration.State.TRUE);
    }), Codec.BOOL.optionalFieldOf("italic").forGetter(style -> {
      TextDecoration.State state = style.decoration(TextDecoration.ITALIC);
      if (state == TextDecoration.State.NOT_SET) {
        return Optional.empty();
      }
      return Optional.of(state == TextDecoration.State.TRUE);
    }), Codec.BOOL.optionalFieldOf("underlined").forGetter(style -> {
      TextDecoration.State state = style.decoration(TextDecoration.UNDERLINED);
      if (state == TextDecoration.State.NOT_SET) {
        return Optional.empty();
      }
      return Optional.of(state == TextDecoration.State.TRUE);
    }), Codec.BOOL.optionalFieldOf("strikethrough").forGetter(style -> {
      TextDecoration.State state = style.decoration(TextDecoration.STRIKETHROUGH);
      if (state == TextDecoration.State.NOT_SET) {
        return Optional.empty();
      }
      return Optional.of(state == TextDecoration.State.TRUE);
    }), Codec.BOOL.optionalFieldOf("obfuscated").forGetter(style -> {
      TextDecoration.State state = style.decoration(TextDecoration.OBFUSCATED);
      if (state == TextDecoration.State.NOT_SET) {
        return Optional.empty();
      }
      return Optional.of(state == TextDecoration.State.TRUE);
    }),

    ClickEventCodecs.CODEC.optionalFieldOf("click_event").forGetter(style -> Optional.ofNullable(style.clickEvent())),

    HoverEventCodecs.CODEC.optionalFieldOf("hover_event").forGetter(style -> Optional.ofNullable(style.hoverEvent())), Codec.STRING.optionalFieldOf("insertion").forGetter(style -> Optional.ofNullable(style.insertion())), AdventureCodecs.KEY.optionalFieldOf("font").forGetter(style -> Optional.ofNullable(style.font()))).apply(instance, StyleCodecs::of));

  private static Style of(Optional<TextColor> textColor, Optional<ShadowColor> shadowColor, Optional<Boolean> bold, Optional<Boolean> italic, Optional<Boolean> underlined, Optional<Boolean> strikethrough, Optional<Boolean> obfuscated, Optional<ClickEvent> clickEvent, Optional<HoverEvent<?>> hoverEvent, Optional<String> insertion, Optional<Key> font) {
    final Style.Builder builder = Style.style();
    textColor.ifPresent(builder::color);
    shadowColor.ifPresent(builder::shadowColor);
    bold.ifPresent(b -> builder.decoration(TextDecoration.BOLD, b ? TextDecoration.State.TRUE : TextDecoration.State.FALSE));
    italic.ifPresent(b -> builder.decoration(TextDecoration.ITALIC, b ? TextDecoration.State.TRUE : TextDecoration.State.FALSE));
    underlined.ifPresent(b -> builder.decoration(TextDecoration.UNDERLINED, b ? TextDecoration.State.TRUE : TextDecoration.State.FALSE));
    strikethrough.ifPresent(b -> builder.decoration(TextDecoration.STRIKETHROUGH, b ? TextDecoration.State.TRUE : TextDecoration.State.FALSE));
    obfuscated.ifPresent(b -> builder.decoration(TextDecoration.OBFUSCATED, b ? TextDecoration.State.TRUE : TextDecoration.State.FALSE));
    clickEvent.ifPresent(builder::clickEvent);
    hoverEvent.ifPresent(builder::hoverEvent);
    insertion.ifPresent(builder::insertion);
    font.ifPresent(builder::font);
    return null;
  }

}
