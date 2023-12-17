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
import com.google.gson.JsonParseException;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class JSONComponentSerializerTest extends SerializerTest {
  @Test
  void testDeserializeNull() {
    assertThrows(RuntimeException.class, () -> JSONComponentSerializer.json().deserialize("null"));
  }

  @Test
  void testDeserializePrimitive() {
    assertEquals(Component.text("potato"), JSONComponentSerializer.json().deserialize("potato"));
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
          array.add(object(object -> object.addProperty(JSONComponentConstants.TEXT, "Hello, ")));
          array.add(object(object -> object.addProperty(JSONComponentConstants.TEXT, "world.")));
        })
      )
    );
  }

  // https://github.com/KyoriPowered/adventure/issues/414
  @Test
  @SuppressWarnings("deprecation")
  void testSkipInvalidHoverEventWhenLenient() {
    final JSONComponentSerializer serializer = JSONComponentSerializer.builder()
      .editFeatures(b -> b.value(JSONFlags.VALIDATE_STRICT_EVENTS, false))
      .build();

    final Component expected = Component.text("hello");
    assertEquals(
      expected,
      deserialize(serializer, object(object -> {
        object.addProperty(JSONComponentConstants.TEXT, "hello");
        object.add(JSONComponentConstants.HOVER_EVENT, object(hover -> {
          hover.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, "show_text");
          hover.add(JSONComponentConstants.HOVER_EVENT_VALUE, new JsonArray());
        }));
      }))
    );
  }

  @Test
  @SuppressWarnings("deprecation")
  void testFailOnInvalidHoverEvents() {
    assertThrows(JsonParseException.class, () -> {
      deserialize(object(object -> {
        object.addProperty(JSONComponentConstants.TEXT, "hello");
        object.add(JSONComponentConstants.HOVER_EVENT, object(hover -> {
          hover.addProperty(JSONComponentConstants.HOVER_EVENT_ACTION, "show_text");
          hover.add(JSONComponentConstants.HOVER_EVENT_VALUE, new JsonArray());
        }));
      }));
    });
  }

}
