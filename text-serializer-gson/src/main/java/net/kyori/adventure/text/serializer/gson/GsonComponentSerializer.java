/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.kyori.adventure.text.BlockNbtComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GsonComponentSerializer implements ComponentSerializer<Component, Component, String>, JsonDeserializer<Component>, JsonSerializer<Component> {
  /**
   * A component serializer for JSON-based serialization and deserialization.
   */
  public static final GsonComponentSerializer INSTANCE = new GsonComponentSerializer();
  static final Gson GSON = populate(new GsonBuilder()).create();

  /**
   * Populate a builder with our serializers.
   *
   * @param builder the gson builder
   * @return the gson builder
   */
  public static @NonNull GsonBuilder populate(final @NonNull GsonBuilder builder) {
    builder
      .registerTypeHierarchyAdapter(Component.class, ComponentSerializerImpl.INSTANCE)
      .registerTypeAdapter(Style.class, StyleSerializer.INSTANCE)
      .registerTypeAdapter(ClickEvent.Action.class, new NameMapSerializer<>("click action", ClickEvent.Action.NAMES))
      .registerTypeAdapter(HoverEvent.Action.class, new NameMapSerializer<>("hover action", HoverEvent.Action.NAMES))
      .registerTypeAdapter(TextColorWrapper.class, new TextColorWrapper.Serializer())
      .registerTypeAdapter(TextColor.class, new NameMapSerializer<>("text color", TextColor.NAMES))
      .registerTypeAdapter(TextDecoration.class, new NameMapSerializer<>("text decoration", TextDecoration.NAMES))
      .registerTypeHierarchyAdapter(BlockNbtComponent.Pos.class, BlockNbtComponentPosSerializer.INSTANCE);
    return builder;
  }

  @Override
  public @NonNull Component deserialize(final @NonNull String string) {
    return GSON.fromJson(string, Component.class);
  }

  @Override
  public @NonNull String serialize(final @NonNull Component component) {
    return GSON.toJson(component);
  }

  // Not part of the API.
  @Deprecated
  @Override
  public Component deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
    return ComponentSerializerImpl.INSTANCE.deserialize(json, typeOfT, context);
  }

  // Not part of the API.
  @Deprecated
  @Override
  public JsonElement serialize(final Component src, final Type typeOfSrc, final JsonSerializationContext context) {
    return ComponentSerializerImpl.INSTANCE.serialize(src, typeOfSrc, context);
  }
}
