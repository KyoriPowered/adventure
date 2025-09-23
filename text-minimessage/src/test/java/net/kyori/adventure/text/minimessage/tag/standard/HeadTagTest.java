/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text.minimessage.tag.standard;

import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.AbstractTest;
import net.kyori.adventure.text.object.ObjectContents;
import org.junit.jupiter.api.Test;

class HeadTagTest extends AbstractTest {

  @Test
  void testEmptySerialization() {
    final String expected = "<head>";

    final Component component = Component.object(
      ObjectContents.playerHead().build()
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testEmptyDeserialization() {
    final Component expected = Component.object(
      ObjectContents.playerHead().build()
    );

    final String input = "<head>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithTexturesSerialization() {
    final String expected = "<head texture=entity/player/wide/steve>";

    final Component component = Component.object(
      ObjectContents.playerHead()
        .texture(Key.key("entity/player/wide/steve"))
        .build()
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testWithTexturesDeserialization() {
    final Component expected = Component.object(
      ObjectContents.playerHead()
        .texture(Key.key("entity/player/wide/steve"))
        .build()
    );

    final String input = "<head texture=entity/player/wide/steve>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithTexturesAndHatSerialization() {
    final String expected = "<head texture=entity/player/wide/steve !hat>";

    final Component component = Component.object(
      ObjectContents.playerHead()
        .texture(Key.key("entity/player/wide/steve"))
        .hat(false)
        .build()
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testWithNameSerialisation() {
    final String expected = "<head name=electronicboy>";

    final Component component = Component.object(
      ObjectContents.playerHead("electronicboy")
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testWithNameDeserialization() {
    final Component expected = Component.object(
      ObjectContents.playerHead("electronicboy")
    );

    final String input = "<head name=electronicboy>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithUuidSerialisation() {
    final String expected = "<head uuid=ef82a52d-c5a4-4b64-a811-2c28828cc120>";

    final Component component = Component.object(
      ObjectContents.playerHead(UUID.fromString("ef82a52d-c5a4-4b64-a811-2c28828cc120"))
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testWithUuidDeserialization() {
    final Component expected = Component.object(
      ObjectContents.playerHead(UUID.fromString("ef82a52d-c5a4-4b64-a811-2c28828cc120"))
    );

    final String input = "<head uuid=ef82a52d-c5a4-4b64-a811-2c28828cc120>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithNameAndUuidSerialisation() {
    final String expected = "<head name=Strokkur24 uuid=ef82a52d-c5a4-4b64-a811-2c28828cc120>";

    final Component component = Component.object(
      ObjectContents.playerHead()
        .id(UUID.fromString("ef82a52d-c5a4-4b64-a811-2c28828cc120"))
        .name("Strokkur24")
        .build()
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testWithNameAndUuidDeserialization() {
    final Component expected = Component.object(
      ObjectContents.playerHead()
        .id(UUID.fromString("ef82a52d-c5a4-4b64-a811-2c28828cc120"))
        .name("Strokkur24")
        .build()
    );

    final String input = "<head name=Strokkur24 uuid=ef82a52d-c5a4-4b64-a811-2c28828cc120>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithNameAndUuidReversedDeserialization() {
    final Component expected = Component.object(
      ObjectContents.playerHead()
        .id(UUID.fromString("ef82a52d-c5a4-4b64-a811-2c28828cc120"))
        .name("Strokkur24")
        .build()
    );

    final String input = "<head uuid=ef82a52d-c5a4-4b64-a811-2c28828cc120 name=Strokkur24>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithNameAndTexturesAndHatSerialisation() {
    final String expected = "<head name=Strokkur24 uuid=ef82a52d-c5a4-4b64-a811-2c28828cc120 !hat>";

    final Component component = Component.object(
      ObjectContents.playerHead()
        .id(UUID.fromString("ef82a52d-c5a4-4b64-a811-2c28828cc120"))
        .name("Strokkur24")
        .hat(false)
        .build()
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testWithNameAndHatDeserialization() {
    final String expected = "<head name=Strokkur24 !hat>";

    final Component component = Component.object(
      ObjectContents.playerHead()
        .name("Strokkur24")
        .hat(false)
        .build()
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testWithUuidAndHatDeserialization() {
    final String expected = "<head uuid=ef82a52d-c5a4-4b64-a811-2c28828cc120 !hat>";

    final Component component = Component.object(
      ObjectContents.playerHead()
        .id(UUID.fromString("ef82a52d-c5a4-4b64-a811-2c28828cc120"))
        .hat(false)
        .build()
    );

    this.assertSerializedEquals(expected, component);
  }

  @Test
  void testWithNameAndTexturesAndHatDeserialization() {
    final Component expected = Component.object(
      ObjectContents.playerHead()
        .id(UUID.fromString("ef82a52d-c5a4-4b64-a811-2c28828cc120"))
        .name("Strokkur24")
        .hat(false)
        .build()
    );

    final String input = "<head name=Strokkur24 uuid=ef82a52d-c5a4-4b64-a811-2c28828cc120 !hat>";
    this.assertParsedEquals(expected, input);
  }
}
