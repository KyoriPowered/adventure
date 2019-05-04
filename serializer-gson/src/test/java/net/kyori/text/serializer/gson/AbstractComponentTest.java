/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.serializer.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.text.Component;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractComponentTest<C extends Component> {
  private static final JsonParser PARSER = new JsonParser();

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

  @Test
  void testDeserialize() {
    this.forEach((component, json) -> assertEquals(component, GsonComponentSerializer.INSTANCE.deserialize(json.toString())));
  }

  @Test
  void testSerialize() {
    this.forEach((component, json) -> assertEquals(json, PARSER.parse(GsonComponentSerializer.INSTANCE.serialize(component))));
  }

  private void forEach(final BiConsumer<? super C, JsonElement> consumer) {
    this.tests().forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
  }

  abstract Stream<Map.Entry<C, JsonElement>> tests();

  static <C extends Component> Map.Entry<C, JsonElement> entry(final C component, final Consumer<? super JsonObject> json) {
    return new AbstractMap.SimpleImmutableEntry<>(component, object(json));
  }
}
