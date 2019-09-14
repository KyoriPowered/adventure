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
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StyleTest extends AbstractSerializeDeserializeTest<Style> {
  @Test
  void testWithDecorationAsColor() throws IOException {
    final Style s0 = JacksonComponentSerializer.MAPPER.convertValue(object(object -> {
      object.put(StyleSerializer.COLOR, TextDecoration.NAMES.name(TextDecoration.BOLD));
    }), Style.class);
    assertNull(s0.color());
    assertTrue(s0.hasDecoration(TextDecoration.BOLD));
  }

  @Test
  void testWithResetAsColor() throws IOException {
    final Style s0 = JacksonComponentSerializer.MAPPER.convertValue(object(object -> {
      object.put(StyleSerializer.COLOR, "reset");
    }), Style.class);
    assertNull(s0.color());
    assertThat(s0.decorations()).isEmpty();
  }

  @Override
  Stream<Map.Entry<Style, JsonNode>> tests() {
    return Stream.of(
      entry(Style.empty(), json -> {
      }),
      entry(Style.of(TextColor.LIGHT_PURPLE), json -> json.put(StyleSerializer.COLOR, TextColor.NAMES.name(TextColor.LIGHT_PURPLE))),
      entry(Style.of(TextDecoration.BOLD), json -> json.put(TextDecoration.NAMES.name(TextDecoration.BOLD), true)),
      entry(Style.builder().insertion("honk").build(), json -> json.put(StyleSerializer.INSERTION, "honk"))
    );
  }

  @Override
  Style deserialize(final JsonNode json) {
    try {
      return JacksonComponentSerializer.MAPPER.treeToValue(json, Style.class);
    } catch(final Exception e) {
      throw new RuntimeException("Can't deserialize", e);
    }
  }

  @Override
  JsonNode serialize(final Style object) {
    return JacksonComponentSerializer.MAPPER.valueToTree(object);
  }
}
