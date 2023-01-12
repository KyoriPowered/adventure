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

import com.google.gson.JsonArray;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class JsonComponentSerializerTest extends SerializerTest {
  @Test
  void testDeserializeNull() {
    assertThrows(RuntimeException.class, () -> JsonComponentSerializer.json().deserialize("null"));
  }

  @Test
  void testDeserializePrimitive() {
    assertEquals(Component.text("potato"), JsonComponentSerializer.json().deserialize("potato"));
  }

  @Test
  void testDeserializeArray_empty() {
    assertThrows(RuntimeException.class, () -> deserialize(new JsonArray()));
  }

  @Test
  void testDeserializeArray() {
    assertEquals(
      Component.text()
        .content("Hello, ")
        .append(Component.text("world."))
        .build(),
      deserialize(
        array(array -> {
          array.add(object(object -> object.addProperty(JsonComponentConstants.TEXT, "Hello, ")));
          array.add(object(object -> object.addProperty(JsonComponentConstants.TEXT, "world.")));
        })
      )
    );
  }
  // https://github.com/KyoriPowered/adventure/issues/414
  @Test
  @SuppressWarnings("deprecation")
  void testSkipInvalidHoverEvent() {
    final Component expected = Component.text("hello");
    assertEquals(
      expected,
      deserialize(object(object -> {
        object.addProperty(JsonComponentConstants.TEXT, "hello");
        object.add(JsonComponentConstants.HOVER_EVENT, object(hover -> {
          hover.addProperty(JsonComponentConstants.HOVER_EVENT_ACTION, "show_text");
          hover.add(JsonComponentConstants.HOVER_EVENT_VALUE, new JsonArray());
        }));
      }))
    );
  }

}
