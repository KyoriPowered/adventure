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
package net.kyori.adventure.text.serializer.json;

import com.google.gson.JsonObject;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class ObjectComponentTest extends SerializerTest {
  @Test
  void testSprite() {
    this.testObject(
      Component.object(ObjectContents.sprite(Key.key("item/diamond_sword"))),
      json -> json.addProperty(ComponentTreeConstants.OBJECT_SPRITE, "minecraft:item/diamond_sword")
    );
  }

  @Test
  void testSpriteAtlas() {
    this.testObject(
      Component.object(ObjectContents.sprite(Key.key("gui"), Key.key("icon/checkmark"))),
      json -> {
        json.addProperty(ComponentTreeConstants.OBJECT_ATLAS, "minecraft:gui");
        json.addProperty(ComponentTreeConstants.OBJECT_SPRITE, "minecraft:icon/checkmark");
      }
    );
  }

  @Test
  void testPlayerEmpty() {
    this.testObject(
      Component.object(ObjectContents.playerHead().build()),
      json -> {
        json.add(ComponentTreeConstants.OBJECT_PLAYER, new JsonObject());
        json.addProperty(ComponentTreeConstants.OBJECT_HAT, true);
      }
    );
  }

  @Test
  void testPlayerHat() {
    this.testObject(
      Component.object(ObjectContents.playerHead().hat(false).build()),
      json -> {
        json.add(ComponentTreeConstants.OBJECT_PLAYER, new JsonObject());
        json.addProperty(ComponentTreeConstants.OBJECT_HAT, false);
      }
    );
  }

  @Test
  void testPlayerName() {
    this.testObject(
      Component.object(ObjectContents.playerHead().name("Player123").build()),
      json -> {
        json.addProperty(ComponentTreeConstants.OBJECT_PLAYER, "Player123");
        json.addProperty(ComponentTreeConstants.OBJECT_HAT, true);
      }
    );
  }

  @Test
  void testPlayerId() {
    final UUID id = UUID.randomUUID();
    this.testObject(
      Component.object(ObjectContents.playerHead().id(id).build()),
      json -> {
        json.add(ComponentTreeConstants.OBJECT_PLAYER, object(profile ->
          profile.add(ComponentTreeConstants.OBJECT_PLAYER_ID, uuidArray(id))
        ));
        json.addProperty(ComponentTreeConstants.OBJECT_HAT, true);
      }
    );
  }

  @Test
  void testPlayerProperties() {
    this.testObject(
      Component.object(ObjectContents.playerHead().profileProperty(
        PlayerHeadObjectContents.property("textures", "cool_value", "cool_signature")
      ).build()),
      json -> {
        json.add(ComponentTreeConstants.OBJECT_PLAYER, object(profile ->
          profile.add(ComponentTreeConstants.OBJECT_PLAYER_PROPERTIES, array(properties ->
            properties.add(object(property -> {
              property.addProperty(ComponentTreeConstants.PROFILE_PROPERTY_NAME, "textures");
              property.addProperty(ComponentTreeConstants.PROFILE_PROPERTY_VALUE, "cool_value");
              property.addProperty(ComponentTreeConstants.PROFILE_PROPERTY_SIGNATURE, "cool_signature");
            }))
          ))
        ));
        json.addProperty(ComponentTreeConstants.OBJECT_HAT, true);
      }
    );
  }

  @Test
  void testMapPropertyFormat() {
    assertEquals(
      Component.object(ObjectContents.playerHead()
        .profileProperty(PlayerHeadObjectContents.property("textures", "cool_value"))
        .profileProperty(PlayerHeadObjectContents.property("textures", "cooler_value"))
        .build()),
      deserialize(object(json -> {
        json.add(ComponentTreeConstants.OBJECT_PLAYER, object(profile ->
          profile.add(ComponentTreeConstants.OBJECT_PLAYER_PROPERTIES, object(properties ->
            properties.add("textures", array(textures -> {
              textures.add("cool_value");
              textures.add("cooler_value");
            }))
          ))
        ));
        json.addProperty(ComponentTreeConstants.OBJECT_HAT, true);
      }))
    );
  }

  @Test
  void testPlayerTexture() {
    this.testObject(
      Component.object(ObjectContents.playerHead().texture(Key.key("entity/player/wide/steve")).build()),
      json -> {
        json.add(ComponentTreeConstants.OBJECT_PLAYER, object(profile ->
          profile.addProperty(ComponentTreeConstants.OBJECT_PLAYER_TEXTURE, "minecraft:entity/player/wide/steve"))
        );
        json.addProperty(ComponentTreeConstants.OBJECT_HAT, true);
      }
    );
  }
}
