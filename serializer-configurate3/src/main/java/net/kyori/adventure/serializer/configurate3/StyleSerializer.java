/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.serializer.configurate3;

import com.google.common.reflect.TypeToken;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage") // TypeToken
final class StyleSerializer implements TypeSerializer<Style> {
  static final TypeToken<Style> TYPE = TypeToken.of(Style.class);
  static final StyleSerializer INSTANCE = new StyleSerializer();

  private static final TextDecoration[] DECORATIONS = TextDecoration.values();
  private static final TypeToken<TextDecoration.State> DECORATION_STATE = TypeToken.of(TextDecoration.State.class);

  static final String FONT = "font";
  static final String COLOR = "color";
  static final String INSERTION = "insertion";
  static final String CLICK_EVENT = "clickEvent";
  static final String CLICK_EVENT_ACTION = "action";
  static final TypeToken<ClickEvent.Action> CLICK_EVENT_ACTION_TYPE = TypeToken.of(ClickEvent.Action.class);
  static final String CLICK_EVENT_VALUE = "value";
  static final String HOVER_EVENT = "hoverEvent";
  static final String HOVER_EVENT_ACTION = "action";
  @SuppressWarnings("serial")
  static final TypeToken<HoverEvent.Action<?>> HOVER_EVENT_ACTION_TYPE = new TypeToken<HoverEvent.Action<?>>() {};
  static final String HOVER_EVENT_CONTENTS = "contents";
  static final @Deprecated String HOVER_EVENT_VALUE = "value";

  private StyleSerializer() {
  }

  @Override
  public @NotNull Style deserialize(final @NotNull TypeToken<?> type, final @NotNull ConfigurationNode value) throws ObjectMappingException {
    if (value.isVirtual()) {
      return Style.empty();
    }

    final Style.Builder builder = Style.style();

    final @Nullable Key font = value.getNode(FONT).getValue(KeySerializer.INSTANCE.type());
    if (font != null) {
      builder.font(font);
    }
    final @Nullable TextColor color = value.getNode(COLOR).getValue(TextColorSerializer.INSTANCE.type());
    if (color != null) {
      builder.color(color);
    }

    for (final TextDecoration decoration : DECORATIONS) {
      final TextDecoration.State state = value.getNode(nonNull(TextDecoration.NAMES.key(decoration), "decoration")).getValue(DECORATION_STATE);
      if (state != null) {
        builder.decoration(decoration, state);
      }
    }

    final @Nullable String insertion = value.getNode(INSERTION).getString();
    if (insertion != null) {
      builder.insertion(insertion);
    }

    final ConfigurationNode clickEvent = value.getNode(CLICK_EVENT);
    if (!clickEvent.isVirtual()) {
      final ClickEvent.Action action = nonNull(clickEvent.getNode(CLICK_EVENT_ACTION).getValue(CLICK_EVENT_ACTION_TYPE), "click event action");
      builder.clickEvent(ClickEvent.clickEvent(action, nonNull(clickEvent.getNode(CLICK_EVENT_VALUE).getString(), "click event value")));
    }

    final ConfigurationNode hoverEvent = value.getNode(HOVER_EVENT);
    if (!hoverEvent.isVirtual()) {
      final HoverEvent.Action<?> action = hoverEvent.getNode(HOVER_EVENT_ACTION).getValue(HOVER_EVENT_ACTION_TYPE);
      final ConfigurationNode contents = hoverEvent.getNode(HOVER_EVENT_CONTENTS);
      if (contents.isVirtual()) {
        final Component legacyValue = hoverEvent.getNode(HOVER_EVENT_VALUE).getValue(ComponentTypeSerializer.TYPE);
        if (legacyValue == null) {
          throw new ObjectMappingException("No modern contents or legacy value present for hover event");
        }
        if (action == HoverEvent.Action.SHOW_TEXT) {
          builder.hoverEvent(HoverEvent.showText(legacyValue));
        } else {
          throw new ObjectMappingException("Unable to deserialize legacy hover event of type " + action);
        }
        // TODO: Legacy hover event
      } else {
        if (action == HoverEvent.Action.SHOW_TEXT) {
          builder.hoverEvent(HoverEvent.showText(nonNull(contents.getValue(ComponentTypeSerializer.TYPE), "hover event text contents")));
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
          builder.hoverEvent(HoverEvent.showEntity(nonNull(contents.getValue(HoverEventShowEntitySerializer.TYPE), "hover event show entity contents")));
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
          builder.hoverEvent(HoverEvent.showItem(nonNull(contents.getValue(HoverEventShowItemSerializer.TYPE), "hover event show item contents")));
        } else {
          throw new ObjectMappingException("Unsupported hover event action " + action);
        }
      }
    }

    return builder.build();
  }

  @Override
  public void serialize(final @NotNull TypeToken<?> type, @Nullable Style obj, final @NotNull ConfigurationNode value) throws ObjectMappingException {
    if (obj == null) {
      obj = Style.empty();
    }
    value.getNode(FONT).setValue(KeySerializer.INSTANCE.type(), obj.font());
    value.getNode(COLOR).setValue(TextColorSerializer.INSTANCE.type(), obj.color());
    for (final TextDecoration decoration : DECORATIONS) {
      final ConfigurationNode decorationNode = value.getNode(nonNull(TextDecoration.NAMES.key(decoration), "decoration"));
      final TextDecoration.State state = obj.decoration(decoration);
      if (state == TextDecoration.State.NOT_SET) {
        decorationNode.setValue(null);
      } else {
        decorationNode.setValue(state == TextDecoration.State.TRUE);
      }
    }
    value.getNode(INSERTION).setValue(obj.insertion());

    final ConfigurationNode clickNode = value.getNode(CLICK_EVENT);
    final ClickEvent clickEvent = obj.clickEvent();
    if (clickEvent == null) {
      clickNode.setValue(null);
    } else {
      clickNode.getNode(CLICK_EVENT_ACTION).setValue(CLICK_EVENT_ACTION_TYPE, clickEvent.action());
      clickNode.getNode(CLICK_EVENT_VALUE).setValue(clickEvent.value());
    }

    final ConfigurationNode hoverNode = value.getNode(HOVER_EVENT);
    if (obj.hoverEvent() == null) {
      hoverNode.setValue(null);
    } else {
      final HoverEvent<?> event = obj.hoverEvent();
      hoverNode.getNode(HOVER_EVENT_ACTION).setValue(HOVER_EVENT_ACTION_TYPE, event.action());
      final ConfigurationNode contentsNode = hoverNode.getNode(HOVER_EVENT_CONTENTS);
      if (event.action() == HoverEvent.Action.SHOW_TEXT) {
        contentsNode.setValue(ComponentTypeSerializer.TYPE, (Component) event.value());
      } else if (event.action() == HoverEvent.Action.SHOW_ENTITY) {
        contentsNode.setValue(HoverEventShowEntitySerializer.TYPE, (HoverEvent.ShowEntity) event.value());
      } else if (event.action() == HoverEvent.Action.SHOW_ITEM) {
        contentsNode.setValue(HoverEventShowItemSerializer.TYPE, (HoverEvent.ShowItem) event.value());
      }
    }
  }

  private static <T> @NotNull T nonNull(final @Nullable T value, final @NotNull String type) throws ObjectMappingException {
    if (value == null) {
      throw new ObjectMappingException(type + " was null in an unexpected location");
    }
    return value;
  }
}
