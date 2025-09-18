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
    final String expected = "<head:entity/player/wide/steve>";

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

    final String input = "<head:entity/player/wide/steve>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithTexturesAndHatSerialization() {
    final String expected = "<head:entity/player/wide/steve:false>";

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
    final String expected = "<head:electronicboy>";

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

    final String input = "<head:electronicboy>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithUuidSerialisation() {
    final String expected = "<head:ef82a52d-c5a4-4b64-a811-2c28828cc120>";

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

    final String input = "<head:ef82a52d-c5a4-4b64-a811-2c28828cc120>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithNameAndUuidSerialisation() {
    final String expected = "<head:Strokkur24:ef82a52d-c5a4-4b64-a811-2c28828cc120>";

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

    final String input = "<head:Strokkur24:ef82a52d-c5a4-4b64-a811-2c28828cc120>";
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

    final String input = "<head:ef82a52d-c5a4-4b64-a811-2c28828cc120:Strokkur24>";
    this.assertParsedEquals(expected, input);
  }

  @Test
  void testWithNameAndTexturesAndHatSerialisation() {
    final String expected = "<head:Strokkur24:ef82a52d-c5a4-4b64-a811-2c28828cc120:false>";

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
    final String expected = "<head:Strokkur24:false>";

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
    final String expected = "<head:ef82a52d-c5a4-4b64-a811-2c28828cc120:false>";

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

    final String input = "<head:Strokkur24:ef82a52d-c5a4-4b64-a811-2c28828cc120:false>";
    this.assertParsedEquals(expected, input);
  }
}
