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
package net.kyori.text.serializer.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractSerializeDeserializeTest<C> {
  static ArrayNode array(final Consumer<? super ArrayNode> consumer) {
    final ArrayNode json = JacksonComponentSerializer.MAPPER.getNodeFactory().arrayNode();
    consumer.accept(json);
    return json;
  }

  static ObjectNode object(final Consumer<? super ObjectNode> consumer) {
    final ObjectNode json = JacksonComponentSerializer.MAPPER.getNodeFactory().objectNode();
    consumer.accept(json);
    return json;
  }

  static <C> Map.Entry<C, JsonNode> entry(final C object, final Consumer<? super ObjectNode> json) {
    return new AbstractMap.SimpleImmutableEntry<>(object, object(json));
  }

  abstract Stream<Map.Entry<C, JsonNode>> tests();

  private void forEach(final BiConsumer<? super C, JsonNode> consumer) {
    this.tests().forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
  }

  @Test
  final void testDeserialize() {
    this.forEach((component, json) -> assertEquals(component, this.deserialize(json)));
  }

  abstract C deserialize(final JsonNode json);

  @Test
  final void testSerialize() {
    this.forEach((component, json) -> assertEquals(json, this.serialize(component)));
  }

  abstract JsonNode serialize(final C object);
}
