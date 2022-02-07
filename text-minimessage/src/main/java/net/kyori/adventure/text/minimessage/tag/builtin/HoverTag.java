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
package net.kyori.adventure.text.minimessage.tag.builtin;

import java.util.List;
import java.util.UUID;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Hover tag.
 *
 * @since 4.10.0
 */
@ApiStatus.Internal
public final class HoverTag {
  public static final String HOVER = "hover";

  private HoverTag() {
  }

  @SuppressWarnings("unchecked")
  static Tag create(final List<? extends Tag.Argument> args, final Context ctx) throws ParsingException {
    if (args.size() < 2) {
      throw ctx.newError("Doesn't know how to turn " + args + " into a hover event", args);
    }
    final List<? extends Tag.Argument> newArgs = args.subList(1, args.size());

    final HoverEvent.Action<Object> action = (HoverEvent.Action<Object>) HoverEvent.Action.NAMES.value(args.get(0).value());
    final Object value;
    if (action == (Object) HoverEvent.Action.SHOW_TEXT) {
      value = ctx.parse(newArgs.get(0).value());
    } else if (action == (Object) HoverEvent.Action.SHOW_ITEM) {
      value = parseShowItem(newArgs, ctx);
    } else if (action == (Object) HoverEvent.Action.SHOW_ENTITY) {
      value = parseShowEntity(newArgs, ctx);
    } else {
      throw ctx.newError("Don't know how to turn '" + args + "' into a hover event", args);
    }

    return Tag.styling(HoverEvent.hoverEvent(action, value));
  }

  private static HoverEvent.@NotNull ShowItem parseShowItem(final @NotNull List<? extends Tag.Argument> args, final Context ctx) throws ParsingException {
    try {
      if (args.isEmpty()) {
        throw ctx.newError("Show item hover needs at least item id!");
      }
      final Key key = Key.key(args.get(0).value());
      final int count;
      if (args.size() >= 2) {
        count = Integer.parseInt(args.get(1).value());
      } else {
        count = 1;
      }
      if (args.size() == 3) {
        return HoverEvent.ShowItem.of(key, count, BinaryTagHolder.binaryTagHolder(args.get(2).value()));
      }
      return HoverEvent.ShowItem.of(key, count);
    } catch (final InvalidKeyException | NumberFormatException ex) {
      throw ctx.newError("Exception parsing show_item hover", ex, args);
    }
  }

  private static HoverEvent.@NotNull ShowEntity parseShowEntity(final @NotNull List<? extends Tag.Argument> args, final Context context) throws ParsingException {
    try {
      if (args.size() < 2) {
        throw context.newError("Show entity hover needs at least type and uuid!", args);
      }
      final Key key = Key.key(args.get(0).value());
      final UUID id = UUID.fromString(args.get(1).value());
      if (args.size() == 3) {
        final Component name = context.parse(args.get(2).value());
        return HoverEvent.ShowEntity.of(key, id, name);
      }
      return HoverEvent.ShowEntity.of(key, id);
    } catch (final IllegalArgumentException | InvalidKeyException ex) {
      throw context.newError("Exception parsing show_entity hover", ex, args);
    }
  }
}
