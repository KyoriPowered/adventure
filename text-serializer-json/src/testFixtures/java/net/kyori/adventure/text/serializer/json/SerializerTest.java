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
package net.kyori.adventure.text.serializer.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class SerializerTest {
  private final Gson gson = new Gson();
  private JSONComponentSerializer serializer;

  @BeforeEach
  @SuppressWarnings("checkstyle:MethodName")
  void setUpSerializer() {
    this.serializer = this.createSerializer();
  }

  protected JSONComponentSerializer createSerializer() {
    return JSONComponentSerializer.json();
  }

  final JsonElement serialize(final Component component) {
    return this.serialize(this.serializer, component);
  }

  final JsonElement serialize(final JSONComponentSerializer cereal, final Component component) {
    return this.gson.fromJson(cereal.serialize(component), JsonElement.class);
  }

  final Component deserialize(final JsonElement json) {
    return this.deserialize(this.serializer, json);
  }

  final Component deserialize(final JSONComponentSerializer cereal, final JsonElement json) {
    return cereal.deserialize(json.toString());
  }

  final void testStyle(final Style style, final Consumer<? super JsonObject> consumer) {
    this.testStyle(this.serializer, style, consumer);
  }

  final void testStyle(final JSONComponentSerializer serial, final Style style, final Consumer<? super JsonObject> consumer) {
    this.testObject(serial, Component.text("", style), object -> {
      object.addProperty(JSONComponentConstants.TEXT, "");
      consumer.accept(object);
    });
  }

  final void testArray(final Component component, final Consumer<? super JsonArray> consumer) {
    final JsonArray json = array(consumer);

    assertEquals(json, this.serialize(component));
    assertEquals(component, this.deserialize(json));
  }

  final void testObject(final Component component, final Consumer<? super JsonObject> consumer) {
    this.testObject(this.serializer, component, consumer);
  }

  final void testObject(final JSONComponentSerializer serializer, final Component component, final Consumer<? super JsonObject> consumer) {
    final JsonObject json = object(consumer);

    assertEquals(json, this.serialize(serializer, component));
    assertEquals(component, this.deserialize(serializer, json));
  }

  static JsonArray array(final Consumer<? super JsonArray> consumer) {
    final JsonArray json = new JsonArray();
    consumer.accept(json);
    return json;
  }

  static JsonObject object(final Consumer<? super JsonObject> consumer) {
    final JsonObject json = new JsonObject();
    consumer.accept(json);
    return json;
  }

  static String name(final NamedTextColor color) {
    return NamedTextColor.NAMES.key(color);
  }

  static String name(final TextDecoration decoration) {
    return TextDecoration.NAMES.key(decoration);
  }

  static String name(final ClickEvent.Action action) {
    return ClickEvent.Action.NAMES.key(action);
  }

  static <V> String name(final HoverEvent.Action<V> action) {
    return HoverEvent.Action.NAMES.key(action);
  }
}
