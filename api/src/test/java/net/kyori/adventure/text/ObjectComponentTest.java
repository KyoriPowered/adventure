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
package net.kyori.adventure.text;

import java.util.UUID;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectComponentTest extends AbstractComponentTest<ObjectComponent, ObjectComponent.Builder> {
  @Override
  ObjectComponent.Builder builder() {
    return Component.object().contents(ObjectComponent.Contents.sprite(Key.key("sprite")));
  }

  @Test
  void testSpriteContents() {
    final ObjectComponent.SpriteContents sprite = ObjectComponent.Contents.sprite(Key.key("atlas"), Key.key("sprite"));
    final ObjectComponent c0 = Component.object(sprite);
    assertEquals(sprite, c0.contents());

    final ObjectComponent.SpriteContents sprite1 = ObjectComponent.Contents.sprite(Key.key("atlas"), Key.key("sprite1"));
    final ObjectComponent c1 = c0.contents(sprite1);
    assertEquals(sprite1, c1.contents());
  }

  @Test
  void testPlayerHeadContents() {
    final ObjectComponent.PlayerHeadContents head = ObjectComponent.Contents.playerHead("fortnite", UUID.randomUUID(), true);
    final ObjectComponent c0 = Component.object(head);
    assertEquals(head, c0.contents());

    final ObjectComponent.PlayerHeadContents head1 = ObjectComponent.Contents.playerHead()
      .id(UUID.randomUUID())
      .property("textures", "texture_value", "texture_signature")
      .build();
    final ObjectComponent c1 = c0.contents(head1);
    assertEquals(head1, c1.contents());
  }
}
