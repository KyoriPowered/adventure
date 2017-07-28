/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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
package net.kyori.text.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.text.BuildableComponent;
import net.kyori.text.Component;
import net.kyori.text.ScoreComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link Component} serializer and deserializer.
 */
public final class ComponentSerializer implements JsonDeserializer<Component>, JsonSerializer<Component> {

  private static final Gson GSON = new GsonBuilder()
    .registerTypeHierarchyAdapter(Component.class, new ComponentSerializer())
    .create();

  /**
   * Deserialize a json string into a component.
   *
   * @param string the json string
   * @return the component
   */
  public static Component deserialize(@Nonnull final String string) {
    return GSON.fromJson(string, Component.class);
  }

  /**
   * Serializes a component into a json string.
   *
   * @param component the component
   * @return the json string
   */
  public static String serialize(@Nonnull final Component component) {
    return GSON.toJson(component);
  }

  @Override
  public Component deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
    if(element.isJsonPrimitive()) {
      return TextComponent.of(element.getAsString());
    }
    if(!element.isJsonObject()) {
      if(element.isJsonArray()) {
        Component parent = null;
        for(final JsonElement childElement : element.getAsJsonArray()) {
          final Component child = this.deserialize(childElement, childElement.getClass(), context);
          if(parent == null) {
            parent = child;
          } else {
            parent.append(child);
          }
        }
        return parent;
      }

      throw new JsonParseException("Don't know how to turn " + element + " into a Component");
    }
    final JsonObject object = element.getAsJsonObject();
    final BuildableComponent.Builder component;
    if(object.has("text")) {
      component = TextComponent.builder(object.get("text").getAsString());
    } else if(object.has("translate")) {
      final String key = object.get("translate").getAsString();
      if(!object.has("with")) {
        component = TranslatableComponent.builder(key);
      } else {
        final JsonArray with = object.getAsJsonArray("with");
        final List<Component> args = new ArrayList<>(with.size());
        for(int i = 0, size = args.size(); i < size; i++) {
          final JsonElement argElement = with.get(i);
          args.add(this.deserialize(argElement, argElement.getClass(), context));
        }
        component = TranslatableComponent.builder(key).args(args);
      }
    } else if(object.has("score")) {
      final JsonObject score = object.getAsJsonObject("score");
      if(!score.has("name") && !score.has("objective")) {
        throw new JsonParseException("A score component requires a name and objective");
      }
      // score components can have a value sometimes, let's grab it
      if(score.has("value")) {
        component = ScoreComponent.builder().name(score.get("name").getAsString()).objective(score.get("objective").getAsString()).value(score.get("value").getAsString());
      } else {
        component = ScoreComponent.builder().name(score.get("name").getAsString()).objective(score.get("objective").getAsString());
      }
    } else if(object.has("selector")) {
      component = SelectorComponent.builder().pattern(object.get("selector").getAsString());
    } else {
      throw new JsonParseException("Don't know how to turn " + element + " into a Component");
    }

    if(object.has("extra")) {
      final JsonArray extra = object.getAsJsonArray("extra");
      for(int i = 0, size = extra.size(); i < size; i++) {
        final JsonElement extraElement = extra.get(i);
        component.append(this.deserialize(extraElement, extraElement.getClass(), context));
      }
    }

    if(object.has("bold")) component.decoration(TextDecoration.BOLD, object.get("bold").getAsBoolean());
    if(object.has("italic")) component.decoration(TextDecoration.ITALIC, object.get("italic").getAsBoolean());
    if(object.has("underlined")) component.decoration(TextDecoration.UNDERLINE, object.get("underlined").getAsBoolean());
    if(object.has("strikethrough")) component.decoration(TextDecoration.STRIKETHROUGH, object.get("strikethrough").getAsBoolean());
    if(object.has("obfuscated")) component.decoration(TextDecoration.OBFUSCATED, object.get("obfuscated").getAsBoolean());
    if(object.has("color")) component.color(context.deserialize(object.get("color"), TextColor.class));
    if(object.has("insertion")) component.insertion(object.get("insertion").getAsString());
    if(object.has("clickEvent")) {
      final JsonObject clickEvent = object.getAsJsonObject("clickEvent");
      if(clickEvent != null) {
        @Nullable final JsonPrimitive rawAction = clickEvent.getAsJsonPrimitive("action");
        @Nullable final ClickEvent.Action action = rawAction == null ? null : context.deserialize(rawAction, ClickEvent.Action.class);
        @Nullable final JsonPrimitive rawValue = clickEvent.getAsJsonPrimitive("value");
        @Nullable final String value = rawValue == null ? null : rawValue.getAsString();
        if(action != null && value != null && action.isReadable()) {
          component.clickEvent(new ClickEvent(action, value));
        }
      }
    }

    if(object.has("hoverEvent")) {
      final JsonObject hoverEvent = object.getAsJsonObject("hoverEvent");
      if(hoverEvent != null) {
        @Nullable final JsonPrimitive rawAction = hoverEvent.getAsJsonPrimitive("action");
        @Nullable final HoverEvent.Action action = rawAction == null ? null : context.deserialize(rawAction, HoverEvent.Action.class);
        if(action != null && action.isReadable()) {
          @Nullable final JsonElement rawValue = hoverEvent.get("value");
          @Nullable final Component value = rawValue == null ? null : this.deserialize(rawValue, rawValue.getClass(), context);
          if(value != null) component.hoverEvent(new HoverEvent(action, value));
        }
      }
    }

    return component.build();
  }

  @Override
  public JsonElement serialize(final Component component, final Type type, final JsonSerializationContext context) {
    final JsonObject object = new JsonObject();
    if(component instanceof TextComponent) {
      object.addProperty("text", ((TextComponent) component).content());
    } else if(component instanceof TranslatableComponent) {
      final TranslatableComponent tc = (TranslatableComponent) component;
      object.addProperty("translate", tc.key());
      if(!tc.args().isEmpty()) {
        final JsonArray with = new JsonArray();
        for(final Component arg : tc.args()) {
          with.add(this.serialize(arg, arg.getClass(), context));
        }
        object.add("with", with);
      }
    } else if(component instanceof ScoreComponent) {
      final ScoreComponent sc = (ScoreComponent) component;
      final JsonObject score = new JsonObject();
      score.addProperty("name", sc.name());
      score.addProperty("objective", sc.objective());
      // score component value is optional
      if(sc.value() != null) score.addProperty("value", sc.value());
      object.add("score", score);
    } else if(component instanceof SelectorComponent) {
      object.addProperty("selector", ((SelectorComponent) component).pattern());
    } else {
      throw new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
    }

    if(!component.children().isEmpty()) {
      final JsonArray extra = new JsonArray();
      for(final Component child : component.children()) {
        extra.add(this.serialize(child, child.getClass(), context));
      }
      object.add("extra", extra);
    }

    if(component.hasStyling()) {
      for(final TextDecoration decoration : TextDecoration.values()) {
        final TextDecoration.State flag = component.decoration(decoration);
        if(flag != TextDecoration.State.NOT_SET) object.addProperty(decoration.toString(), flag == TextDecoration.State.TRUE);
      }
      if(component.color() != null) object.add("color", context.serialize(component.color()));
      if(component.insertion() != null) object.add("insertion", context.serialize(component.insertion()));
      @Nullable final ClickEvent clickEvent = component.clickEvent();
      if(clickEvent != null) {
        final JsonObject clickEventO = new JsonObject();
        clickEventO.add("action", context.serialize(clickEvent.action()));
        clickEventO.addProperty("value", clickEvent.value());
        object.add("clickEvent", clickEventO);
      }
      @Nullable final HoverEvent hoverEvent = component.hoverEvent();
      if(hoverEvent != null) {
        final JsonObject hoverEventO = new JsonObject();
        hoverEventO.add("action", context.serialize(hoverEvent.action()));
        hoverEventO.add("value", this.serialize(hoverEvent.value(), type, context));
        object.add("hoverEvent", hoverEventO);
      }
    }

    return object;
  }
}
