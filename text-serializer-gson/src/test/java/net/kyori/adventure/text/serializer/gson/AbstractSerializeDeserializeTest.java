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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractSerializeDeserializeTest<C> {
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

  abstract Stream<Map.Entry<C, JsonElement>> tests();

  private void forEach(final BiConsumer<? super C, JsonElement> consumer) {
    this.tests().forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
  }

  @Test
  final void testDeserialize() {
    this.forEach((component, json) -> assertEquals(component, this.deserialize(json)));
  }

  abstract C deserialize(final JsonElement json);

  @Test
  final void testSerialize() {
    this.forEach((component, json) -> assertEquals(json, this.serialize(component)));
  }

  abstract JsonElement serialize(final C object);

  static <C> Map.Entry<C, JsonElement> entry(final C object, final Consumer<? super JsonObject> json) {
    return new AbstractMap.SimpleImmutableEntry<>(object, object(json));
  }
}
