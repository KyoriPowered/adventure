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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.AbstractTest;
import org.junit.jupiter.api.Test;

class NbtTagTest extends AbstractTest {
  @Test
  void testParseBlockNbt() {
    final String input = "That block has <nbt:block:^0 ^0 ^0:Items[0].display.Name/> in its first slot";

    final Component expected = Component.text()
      .content("That block has ")
      .append(Component.blockNBT("Items[0].display.Name", BlockNBTComponent.LocalPos.localPos(0, 0, 0)))
      .append(Component.text(" in its first slot"))
      .build();

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testSerializeBlockNbt() {
    final String expected = "That block has <nbt:block:'^0.0 ^0.0 ^0.0':Items[0].display.Name/> in its first slot";

    final Component input = Component.text()
      .content("That block has ")
      .append(Component.blockNBT("Items[0].display.Name", BlockNBTComponent.LocalPos.localPos(0, 0, 0)))
      .append(Component.text(" in its first slot"))
      .build();

    this.assertSerializedEquals(expected, input);
  }

  @Test
  void testParseInvalidPos() {
    final String input = "<nbt:block:abc:Data.Value/>";
    final Component expected = Component.text(input);

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testParseEntityNbt() {
    final String input = "<nbt:entity:@s:Health/>";
    final Component expected = Component.entityNBT("Health", "@s");

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testSerializeEntityNbt() {
    final Component input = Component.entityNBT("Health", "@s");
    final String expected = "<nbt:entity:@s:Health>";

    this.assertSerializedEquals(expected, input);
  }

  @Test
  void testParseStorageNbt() {
    final String input = "<nbt:storage:'adventure:persist':'Users{cat: true}'/>";
    final Component expected = Component.storageNBT("Users{cat: true}", Key.key("adventure", "persist"));

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testParseInvalidKey() {
    final String input = "<nbt:storage:'adventure:pe rsist':'Users{cat: true}'/>";
    final Component expected = Component.text(input);

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testSerializeStorageNbt() {
    final String expected = "<nbt:storage:'adventure:persist':'Users{cat: true}'>";
    final Component input = Component.storageNBT("Users{cat: true}", Key.key("adventure", "persist"));

    this.assertSerializedEquals(expected, input);
  }

  @Test
  void testParseInterpret() {
    final String input = "<nbt:entity:@s:sth:interpret/>";
    final Component expected = Component.entityNBT()
      .selector("@s")
      .nbtPath("sth")
      .interpret(true)
      .build();

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testSerializeInterpret() {
    final Component input = Component.entityNBT()
      .selector("@s")
      .nbtPath("sth")
      .separator(Component.text("! ", NamedTextColor.BLUE))
      .interpret(true)
      .build();
    final String expected = "<nbt:entity:@s:sth:'<blue>! ':interpret>";

    this.assertSerializedEquals(expected, input);
  }

  @Test
  void testParseSeparator() {
    final String input = "<nbt:entity:@s:sth:'<blue>! '/>";
    final Component expected = Component.entityNBT()
      .selector("@s")
      .nbtPath("sth")
      .separator(Component.text("! ", NamedTextColor.BLUE))
      .build();

    this.assertParsedEquals(expected, input);
  }

  @Test
  void testSerializeSeparator() {
    final Component input = Component.entityNBT()
      .selector("@s")
      .nbtPath("sth")
      .separator(Component.text("! ", NamedTextColor.BLUE))
      .build();
    final String expected = "<nbt:entity:@s:sth:'<blue>! '>";

    this.assertSerializedEquals(expected, input);
  }
}
