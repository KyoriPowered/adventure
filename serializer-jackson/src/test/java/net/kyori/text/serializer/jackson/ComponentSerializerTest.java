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

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentSerializerTest {
  @Test
  void testDeserializePrimitive() throws IOException {
    assertEquals(TextComponent.of("potato"), JacksonComponentSerializer.MAPPER.convertValue("potato", Component.class));
  }

  @Test
  void testDeserializeArray_empty() {
    assertThrows(MismatchedInputException.class, () -> JacksonComponentSerializer.MAPPER.readValue("[]", Component.class));
  }

  @Test
  void testDeserializeArray() throws IOException {
    assertEquals(TextComponent.of("Hello, ").append(TextComponent.of("world.")), JacksonComponentSerializer.MAPPER.convertValue(AbstractSerializeDeserializeTest.array(array -> {
      array.add(AbstractSerializeDeserializeTest.object(object -> object.put(ComponentSerializer.TEXT, "Hello, ")));
      array.add(AbstractSerializeDeserializeTest.object(object -> object.put(ComponentSerializer.TEXT, "world.")));
    }), Component.class));
  }
}
