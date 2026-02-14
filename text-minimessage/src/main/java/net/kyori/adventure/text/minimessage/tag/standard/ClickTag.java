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

import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.internal.serializer.QuotingOverride;
import net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jspecify.annotations.Nullable;

/**
 * Click events.
 *
 * @since 4.10.0
 */
record ClickTag() {
  static final String CLICK = "click";

  static final TagResolver RESOLVER = SerializableResolver.claimingStyle(
    CLICK,
    ClickTag::create,
    StyleClaim.<ClickEvent<?>>claim(CLICK, Style::clickEvent, (event, emitter) -> {
      final ClickEvent.Payload payload = event.payload();
      emitter.tag(CLICK)
        .argument(ClickEvent.Action.NAMES.keyOrThrow(event.action()))
        .argument(
          switch (payload) {
            case ClickEvent.Payload.Custom custom -> custom.key().asString();
            case ClickEvent.Payload.Dialog ignored -> throw new UnsupportedOperationException("show_dialog click events cannot be serialized by MiniMessage yet");
            case ClickEvent.Payload.Int integer -> String.valueOf(integer.integer());
            case ClickEvent.Payload.Text text -> text.value();
          }, QuotingOverride.QUOTED);

      if (payload instanceof ClickEvent.Payload.Custom custom) {
        final BinaryTagHolder nbt = custom.nbt();
        if (nbt != null) {
          emitter.argument(nbt.string());
        }
      }
    })
  );

  @SuppressWarnings("PatternValidation") // We check the pattern of the key with a catch.
  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final String actionName = args.popOr(() -> "A click tag requires an action of one of " + ClickEvent.Action.NAMES.keys()).lowerValue();
    final ClickEvent.@Nullable Action<?> action = ClickEvent.Action.NAMES.value(actionName);
    if (action == null) {
      throw ctx.newException("Unknown click event action '" + actionName + "'", args);
    }

    final ClickEvent<?> event = switch (action) {
      case ClickEvent.Action.ChangePage ignored -> ClickEvent.changePage(
        args
          .popOr("'change_page' click event requires a page argument")
          .asInt()
          .orElseThrow(() -> ctx.newException("'change_page' click event requires an integer page argument", args)));
      case ClickEvent.Action.Custom ignored -> {
        final String keyString = args.popOr("'custom' click event requires a key argument").value();
        final Key key;
        try {
          key = Key.key(keyString);
        } catch (final InvalidKeyException ex) {
          throw ctx.newException("'custom' click event requires a valid key argument", ex, args);
        }

        final String nbt;
        if (args.hasNext()) {
          nbt = args.pop().value();
        } else {
          nbt = null;
        }

        yield ClickEvent.custom(key, nbt == null ? null : BinaryTagHolder.binaryTagHolder(nbt));
      }
      case ClickEvent.Action.ShowDialog ignored ->
        throw ctx.newException("'show_dialog' click events are not supported in MiniMessage yet");
      case ClickEvent.Action.TextCarrier textCarrier -> ClickEvent.clickEvent(textCarrier, ClickEvent.Payload.string(args.popOr("'" + textCarrier + "' click events require a value").value()));
    };

    return Tag.styling(event);
  }
}
