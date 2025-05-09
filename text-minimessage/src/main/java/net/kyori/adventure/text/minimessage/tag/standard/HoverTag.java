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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
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

/**
 * Hover tag.
 *
 * @since 4.10.0
 */
final class HoverTag {
  private static final String HOVER = "hover";

  static final TagResolver RESOLVER = SerializableResolver.claimingStyle(
    HOVER,
    HoverTag::create,
    StyleClaim.claim(HOVER, Style::hoverEvent, HoverTag::emit)
  );

  private HoverTag() {
  }

  @SuppressWarnings("unchecked")
  static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
    final String actionName = args.popOr("Hover event requires an action as its first argument").value();
    final HoverEvent.Action<Object> action = (HoverEvent.Action<Object>) HoverEvent.Action.NAMES.value(actionName);
    final ActionHandler<Object> value = actionHandler(action);
    if (value == null) {
      throw ctx.newException("Don't know how to turn '" + args + "' into a hover event", args);
    }

    return Tag.styling(HoverEvent.hoverEvent(action, value.parse(args, ctx)));
  }

  @SuppressWarnings("unchecked")
  static void emit(final HoverEvent<?> event, final TokenEmitter emitter) {
    final ActionHandler<Object> handler = (ActionHandler<Object>) actionHandler(event.action());
    emitter.tag(HOVER).argument(HoverEvent.Action.NAMES.key(event.action()));
    handler.emit(event.value(), emitter);
  }

  @SuppressWarnings("unchecked")
  static <V> @Nullable ActionHandler<V> actionHandler(final HoverEvent.Action<V> action) {
    ActionHandler<?> ret = null;
    if (action == HoverEvent.Action.SHOW_TEXT) {
      ret = ShowText.INSTANCE;
    } else if (action == HoverEvent.Action.SHOW_ITEM) {
      ret = ShowItem.INSTANCE;
    } else if (action == HoverEvent.Action.SHOW_ENTITY) {
      ret = ShowEntity.INSTANCE;
    }

    return (ActionHandler<V>) ret;
  }

  interface ActionHandler<V> {
    @NotNull V parse(final @NotNull ArgumentQueue args, final @NotNull Context ctx) throws ParsingException;

    void emit(final V event, final TokenEmitter emit);
  }

  static final class ShowText implements ActionHandler<Component> {
    private static final ShowText INSTANCE = new ShowText();

    private ShowText() {
    }

    @Override
    public @NotNull Component parse(final @NotNull ArgumentQueue args, final @NotNull Context ctx) throws ParsingException {
      return ctx.deserialize(args.popOr("show_text action requires a message").value());
    }

    @Override
    public void emit(final Component event, final TokenEmitter emit) {
      emit.argument(event);
    }
  }

  static final class ShowItem implements ActionHandler<HoverEvent.ShowItem> {
    private static final ShowItem INSTANCE = new ShowItem();

    private ShowItem() {
    }

    @Override
    public HoverEvent.@NotNull ShowItem parse(final @NotNull ArgumentQueue args, final @NotNull Context ctx) throws ParsingException {
      try {
        @SuppressWarnings("PatternValidation")
        final Key key = Key.key(args.popOr("Show item hover needs at least an item ID").value());
        final int count = args.hasNext() ? args.pop().asInt().orElseThrow(() -> ctx.newException("The count argument was not a valid integer")) : 1;
        if (args.hasNext()) {
          // Compatibility with legacy versions:
          // if the value starts with a '{' we assume it's SNBT, and parse it as such to create a legacy holder
          // otherwise, we'll parse argument pairs as a map of ResourceLocation -> SNBT value
          final String value = args.peek().value();
          if (value.startsWith("{")) {
            args.pop();
            return legacyShowItem(key, count, value);
          }

          final Map<Key, DataComponentValue> datas = new HashMap<>();
          while (args.hasNext()) {
            @SuppressWarnings("PatternValidation")
            final Key dataKey = Key.key(args.pop().value());
            final String dataVal = args.popOr("a value was expected for key " + dataKey).value();
            datas.put(dataKey, BinaryTagHolder.binaryTagHolder(dataVal));
          }
          return HoverEvent.ShowItem.showItem(key, count, datas);
        } else {
          return HoverEvent.ShowItem.showItem(key, count);
        }
      } catch (final InvalidKeyException | NumberFormatException ex) {
        throw ctx.newException("Exception parsing show_item hover", ex, args);
      }
    }

    @SuppressWarnings("deprecation")
    private static HoverEvent.@NotNull ShowItem legacyShowItem(final Key id, final int count, final String value) {
      return HoverEvent.ShowItem.showItem(id, count, BinaryTagHolder.binaryTagHolder(value));
    }

    @Override
    public void emit(final HoverEvent.ShowItem event, final TokenEmitter emit) {
      emit.argument(compactAsString(event.item()));

      if (event.count() != 1 || hasLegacy(event) || !event.dataComponents().isEmpty()) {
        emit.argument(Integer.toString(event.count()));

        if (hasLegacy(event)) {
          emitLegacyHover(event, emit);
        } else {
          for (final Map.Entry<Key, DataComponentValue.TagSerializable> entry : event.dataComponentsAs(DataComponentValue.TagSerializable.class).entrySet()) {
            emit.argument(entry.getKey().asMinimalString());
            emit.argument(entry.getValue().asBinaryTag().string());
          }
        }
      }
    }

    @SuppressWarnings("deprecation")
    static boolean hasLegacy(final HoverEvent.ShowItem event) {
      return event.nbt() != null;
    }

    @SuppressWarnings("deprecation")
    static void emitLegacyHover(final HoverEvent.ShowItem event, final TokenEmitter emit) {
      if (event.nbt() != null) {
        emit.argument(event.nbt().string());
      }
    }
  }

  static final class ShowEntity implements ActionHandler<HoverEvent.ShowEntity> {
    static final ShowEntity INSTANCE = new ShowEntity();

    private ShowEntity() {
    }

    @Override
    public HoverEvent.@NotNull ShowEntity parse(final @NotNull ArgumentQueue args, final @NotNull Context ctx) throws ParsingException {
      try {
        final Key key = Key.key(args.popOr("Show entity needs a type argument").value());
        final UUID id = UUID.fromString(args.popOr("Show entity needs an entity UUID").value());
        if (args.hasNext()) {
          final Component name = ctx.deserialize(args.pop().value());
          return HoverEvent.ShowEntity.showEntity(key, id, name);
        }
        return HoverEvent.ShowEntity.showEntity(key, id);
      } catch (final IllegalArgumentException | InvalidKeyException ex) {
        throw ctx.newException("Exception parsing show_entity hover", ex, args);
      }
    }

    @Override
    public void emit(final HoverEvent.ShowEntity event, final TokenEmitter emit) {
      emit.argument(compactAsString(event.type()))
       .argument(event.id().toString());

      if (event.name() != null) {
        emit.argument(event.name());
      }
    }
  }

  static @NotNull String compactAsString(final @NotNull Key key) {
    if (key.namespace().equals(Key.MINECRAFT_NAMESPACE)) {
      return key.value();
    } else {
      return key.asString();
    }
  }
}
