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

import java.util.UUID;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
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
  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final String actionName = args.popOr("Hover event requires an action as its first argument").value();
    final HoverEvent.Action<Object> action = (HoverEvent.Action<Object>) HoverEvent.Action.NAMES.value(actionName);
    final Object value;
    if (action == (Object) HoverEvent.Action.SHOW_TEXT) {
      value = ctx.parse(args.popOr("show_text action requires a message").value());
    } else if (action == (Object) HoverEvent.Action.SHOW_ITEM) {
      value = parseShowItem(args, ctx);
    } else if (action == (Object) HoverEvent.Action.SHOW_ENTITY) {
      value = parseShowEntity(args, ctx);
    } else {
      throw ctx.newError("Don't know how to turn '" + args + "' into a hover event", args);
    }

    return Tag.styling(HoverEvent.hoverEvent(action, value));
  }

  private static HoverEvent.@NotNull ShowItem parseShowItem(final @NotNull ArgumentQueue args, final Context ctx) throws ParsingException {
    try {
      final Key key = Key.key(args.popOr("Show item hover needs at least an item ID").value());
      final int count = args.hasNext() ? args.pop().asInt().orElseThrow(() -> ctx.newError("The count argument was not a valid integer")) : 1;
      if (args.hasNext()) {
        return HoverEvent.ShowItem.of(key, count, BinaryTagHolder.binaryTagHolder(args.pop().value()));
      } else {
        return HoverEvent.ShowItem.of(key, count);
      }
    } catch (final InvalidKeyException | NumberFormatException ex) {
      throw ctx.newError("Exception parsing show_item hover", ex, args);
    }
  }

  private static HoverEvent.@NotNull ShowEntity parseShowEntity(final @NotNull ArgumentQueue args, final Context context) throws ParsingException {
    try {
      final Key key = Key.key(args.popOr("Show entity needs a type argument").value());
      final UUID id = UUID.fromString(args.popOr("Show entity needs an entity UUID").value());
      if (args.hasNext()) {
        final Component name = context.parse(args.pop().value());
        return HoverEvent.ShowEntity.of(key, id, name);
      }
      return HoverEvent.ShowEntity.of(key, id);
    } catch (final IllegalArgumentException | InvalidKeyException ex) {
      throw context.newError("Exception parsing show_entity hover", ex, args);
    }
  }
}
