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
package net.kyori.adventure.serializer.configurate4;

import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_CAMEL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PAGE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_PAYLOAD;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.CLICK_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.COLOR;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.FONT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_ACTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_CAMEL;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_CONTENTS;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.HOVER_EVENT_VALUE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.INSERTION;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.SHADOW_COLOR;

final class StyleSerializer implements TypeSerializer<Style> {
  static final StyleSerializer INSTANCE = new StyleSerializer();

  private static final TextDecoration[] DECORATIONS = TextDecoration.values();

  static final TypeToken<HoverEvent.Action<?>> HOVER_EVENT_ACTION_TYPE = new TypeToken<HoverEvent.Action<?>>() {};
  static final TypeToken<ClickEvent.Action<?>> CLICK_EVENT_ACTION_TYPE = new TypeToken<ClickEvent.Action<?>>() {};

  private StyleSerializer() {
  }

  @Override
  public Style deserialize(final Type type, final ConfigurationNode value) throws SerializationException {
    if (value.virtual()) {
      return Style.empty();
    }

    final Style.Builder builder = Style.style();

    final Key font = value.node(FONT).get(Key.class);
    if (font != null) {
      builder.font(font);
    }
    final TextColor color = value.node(COLOR).get(TextColor.class);
    if (color != null) {
      builder.color(color);
    }
    final ShadowColor shadowColor = value.node(SHADOW_COLOR).get(ShadowColor.class);
    if (shadowColor != null) {
      builder.shadowColor(shadowColor);
    }

    for (final TextDecoration decoration : DECORATIONS) {
      final TextDecoration.State state = value.node(nonNull(TextDecoration.NAMES.key(decoration), "decoration")).get(TextDecoration.State.class);
      if (state != null) {
        builder.decoration(decoration, state);
      }
    }

    final String insertion = value.node(INSERTION).getString();
    if (insertion != null) {
      builder.insertion(insertion);
    }

    final ConfigurationNode clickEvent = value.node(CLICK_EVENT_CAMEL);
    if (!clickEvent.virtual()) {
      final ClickEvent.Action<?> action = nonNull(clickEvent.node(CLICK_EVENT_ACTION).get(CLICK_EVENT_ACTION_TYPE), "click event action");
      switch (action) {
        case ClickEvent.Action.ChangePage ignored -> {
          ConfigurationNode page;
          page = clickEvent.node(CLICK_EVENT_PAGE);
          if (page.virtual()) page = clickEvent.node(CLICK_EVENT_VALUE);
          nonNull(page, "click event page");
          builder.clickEvent(ClickEvent.changePage(page.getInt()));
        }
        case ClickEvent.Action.Custom ignored -> {
          builder.clickEvent(
            ClickEvent.custom(
              nonNull(clickEvent.node(CLICK_EVENT_ID).get(Key.class), "click event id"),
              BinaryTagHolder.binaryTagHolder(nonNull(clickEvent.node(CLICK_EVENT_PAYLOAD).getString(), "click event payload"))
            )
          );
        }
        case ClickEvent.Action.ShowDialog ignored -> {
          throw new SerializationException("Unable to serialize show_dialog click event");
        }
        case ClickEvent.Action.TextCarrier textCarrier -> {
          builder.clickEvent(ClickEvent.clickEvent(textCarrier, ClickEvent.Payload.string(nonNull(clickEvent.node(CLICK_EVENT_VALUE).getString(), "click event value"))));
        }
      }
    }

    final ConfigurationNode hoverEvent = value.node(HOVER_EVENT_CAMEL);
    if (!hoverEvent.virtual()) {
      final HoverEvent.Action<?> action = hoverEvent.node(HOVER_EVENT_ACTION).get(HOVER_EVENT_ACTION_TYPE);
      final ConfigurationNode contents = hoverEvent.node(HOVER_EVENT_CONTENTS);
      if (contents.virtual()) {
        final Component legacyValue = hoverEvent.node(HOVER_EVENT_VALUE).get(Component.class);
        if (legacyValue == null) {
          throw new SerializationException("No modern contents or legacy value present for hover event");
        }
        if (action == HoverEvent.Action.SHOW_TEXT) {
          builder.hoverEvent(HoverEvent.showText(legacyValue));
        } else {
          throw new SerializationException("Unable to deserialize legacy hover event of type " + action);
        }
        // TODO: Legacy hover event
      } else {
        if (action == HoverEvent.Action.SHOW_TEXT) {
          builder.hoverEvent(HoverEvent.showText(nonNull(contents.get(Component.class), "hover event text contents")));
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
          builder.hoverEvent(HoverEvent.showEntity(nonNull(contents.get(HoverEvent.ShowEntity.class), "hover event show entity contents")));
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
          builder.hoverEvent(HoverEvent.showItem(nonNull(contents.get(HoverEvent.ShowItem.class), "hover event show item contents")));
        } else {
          throw new SerializationException("Unsupported hover event action " + action);
        }
      }
    }

    return builder.build();
  }

  @Override
  public void serialize(final Type type, @Nullable Style obj, final ConfigurationNode value) throws SerializationException {
    if (obj == null) {
      obj = Style.empty();
    }
    value.node(FONT).set(Key.class, obj.font());
    value.node(COLOR).set(TextColor.class, obj.color());
    value.node(SHADOW_COLOR).set(ShadowColor.class, obj.shadowColor());
    for (final TextDecoration decoration : DECORATIONS) {
      final ConfigurationNode decorationNode = value.node(nonNull(TextDecoration.NAMES.key(decoration), "decoration"));
      final TextDecoration.State state = obj.decoration(decoration);
      if (state == TextDecoration.State.NOT_SET) {
        decorationNode.set(null);
      } else {
        decorationNode.set(state == TextDecoration.State.TRUE);
      }
    }
    value.node(INSERTION).set(obj.insertion());

    final ConfigurationNode clickNode = value.node(CLICK_EVENT_CAMEL);
    final ClickEvent<?> clickEvent = obj.clickEvent();
    if (clickEvent == null) {
      clickNode.set(null);
    } else {
      clickNode.node(CLICK_EVENT_ACTION).set(clickEvent.action().toString());
      switch (clickEvent.payload()) {
        case ClickEvent.Payload.Custom custom -> {
          clickNode.node(CLICK_EVENT_ID).set(Key.class, custom.key());
          clickNode.set(CLICK_EVENT_PAYLOAD).set(custom.nbt().string());
        }
        case ClickEvent.Payload.Dialog ignored -> {
          throw new SerializationException("Unable to serialize show_dialog click event");
        }
        case ClickEvent.Payload.Int integer -> {
          // currently only change_page uses this, might need more complicated logic in the future
          clickNode.node(CLICK_EVENT_PAGE).set(integer.integer());
        }
        case ClickEvent.Payload.Text text -> {
          clickNode.node(CLICK_EVENT_VALUE).set(text.value());
        }
      }
    }

    final ConfigurationNode hoverNode = value.node(HOVER_EVENT_CAMEL);
    if (obj.hoverEvent() == null) {
      hoverNode.set(null);
    } else {
      final HoverEvent<?> event = obj.hoverEvent();
      hoverNode.node(HOVER_EVENT_ACTION).set(HOVER_EVENT_ACTION_TYPE, event.action());
      final ConfigurationNode contentsNode = hoverNode.node(HOVER_EVENT_CONTENTS);
      if (event.action() == HoverEvent.Action.SHOW_TEXT) {
        contentsNode.set(Component.class, (Component) event.value());
      } else if (event.action() == HoverEvent.Action.SHOW_ENTITY) {
        contentsNode.set(HoverEvent.ShowEntity.class, (HoverEvent.ShowEntity) event.value());
      } else if (event.action() == HoverEvent.Action.SHOW_ITEM) {
        contentsNode.set(HoverEvent.ShowItem.class, (HoverEvent.ShowItem) event.value());
      }
    }
  }

  private static <T> T nonNull(final @Nullable T value, final String type) throws SerializationException {
    if (value == null) {
      throw new SerializationException(type + " was null in an unexpected location");
    }
    return value;
  }

  @Override
  public Style emptyValue(final Type specificType, final ConfigurationOptions options) {
    return Style.empty();
  }
}
