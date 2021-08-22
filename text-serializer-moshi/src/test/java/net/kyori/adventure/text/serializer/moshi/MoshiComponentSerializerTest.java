/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.serializer.moshi;

import com.squareup.moshi.JsonDataException;
import java.io.IOException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.junit.jupiter.api.Test;

import static net.kyori.adventure.text.serializer.moshi.MoshiTest.array;
import static net.kyori.adventure.text.serializer.moshi.MoshiTest.object;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoshiComponentSerializerTest {
  @Test
  void testDeserializeNull() {
    final JsonDataException jde = assertThrows(JsonDataException.class, () -> MoshiComponentSerializer.moshi().deserialize("null"));
    assertTrue(jde.getMessage().contains("turn null into a Component"));
  }

  @Test
  void testDeserializePrimitive() throws IOException {
    assertEquals(Component.text("potato"), MoshiComponentSerializer.moshi().serializer().adapter(SerializerFactory.COMPONENT_TYPE).fromJson("potato"));
  }

  @Test
  void testDeserializeArray_empty() throws IOException {
    assertThrows(JsonDataException.class, () -> MoshiComponentSerializer.moshi().serializer().adapter(SerializerFactory.COMPONENT_TYPE).fromJson("[]"));
  }

  @Test
  void testDeserializeArray() {
    assertEquals(
      Component.text("Hello, ").append(Component.text("world.")),
      MoshiComponentSerializer.moshi().serializer().adapter(SerializerFactory.COMPONENT_TYPE).fromJsonValue(array(array -> {
        array.add(object(object -> object.put(ComponentAdapter.TEXT, "Hello, ")));
        array.add(object(object -> object.put(ComponentAdapter.TEXT, "world.")));
      }))
    );
  }

  @Test
  public void testPre116Downsamples() {
    final TextColor original = TextColor.color(0xAB2211);
    final NamedTextColor downsampled = NamedTextColor.nearestTo(original);
    final Component test = Component.text("meow", original);
    assertEquals(
      "{\"color\":\"" + name(downsampled) + "\",\"text\":\"meow\"}",
      MoshiComponentSerializer.colorDownsamplingMoshi().serializer().adapter(SerializerFactory.COMPONENT_TYPE).toJson(test)
    );
  }

  @Test
  public void testPre116DownsamplesInChildren() {
    final TextColor original = TextColor.color(0xEC41AA);
    final NamedTextColor downsampled = NamedTextColor.nearestTo(original);
    final Component test = Component.text(builder -> builder.content("hey").append(Component.text("there", original)));

    assertEquals(
      "{\"extra\":[{\"color\":\"" + name(downsampled) + "\",\"text\":\"there\"}],\"text\":\"hey\"}",
      MoshiComponentSerializer.colorDownsamplingMoshi().serializer().adapter(SerializerFactory.COMPONENT_TYPE).toJson(test)
    );
  }

  private static String name(final NamedTextColor color) {
    return NamedTextColor.NAMES.key(color);
  }
}
