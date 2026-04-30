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
package net.kyori.adventure.serializer.configurate4;

import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.serializer.commons.ComponentTreeConstants;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComponentSerializerTest implements ConfigurateTestBase {
  @Test
  void testSerializeToString() {
    final ConfigurationNode serialized = ConfigurateComponentSerializer.builder()
      .scalarSerializer(GsonComponentSerializer.gson())
      .outputStringComponents(true)
      .build()
      .serialize(Component.text("Hello", Style.style(TextDecoration.BOLD)));

    assertEquals("{\"bold\":true,\"text\":\"Hello\"}", serialized.getString());
  }

  @Test
  void testTextComponent() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.TEXT).raw("Hello world");
    });
    final Component component = Component.text("Hello world");

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testTranslatableComponent() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.TRANSLATE).raw("translation.string");
      n.node(ComponentTreeConstants.TRANSLATE_WITH).act(w -> {
        w.appendListNode().node(ComponentTreeConstants.TEXT).raw("test1");
        w.appendListNode().node(ComponentTreeConstants.TEXT).raw("test2");
      });
    });
    final Component component = Component.translatable("translation.string", Component.text("test1"), Component.text("test2"));

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testScoreComponent() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.SCORE).act(s -> {
        s.node(ComponentTreeConstants.SCORE_NAME).raw("Holder");
        s.node(ComponentTreeConstants.SCORE_OBJECTIVE).raw("some.objective");
        s.node(ComponentTreeConstants.SCORE_VALUE).raw("Override");
      });
    });
    final Component component = Component.score("Holder", "some.objective", "Override");

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testScoreComponentNoValue() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.SCORE).act(s -> {
        s.node(ComponentTreeConstants.SCORE_NAME).raw("Holder");
        s.node(ComponentTreeConstants.SCORE_OBJECTIVE).raw("some.objective");
      });
    });

    final Component component = Component.score("Holder", "some.objective");

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testKeybindComponent() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.KEYBIND).raw("key.worldeditcui.toggle");
    });
    final Component component = Component.keybind("key.worldeditcui.toggle");

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testSelectorComponent() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.SELECTOR).raw("@e[limit=1]");
    });
    final Component component = Component.selector("@e[limit=1]");

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testBlockNBTComponent() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.NBT).raw("Something[1].CustomName");
      n.node(ComponentTreeConstants.NBT_INTERPRET).raw(true);
      n.node(ComponentTreeConstants.NBT_BLOCK).raw("^0.0 ^0.0 ^0.0");
      n.node(ComponentTreeConstants.NBT_PLAIN).raw(false);
    });
    final Component component = Component.blockNBT()
      .nbtPath("Something[1].CustomName")
      .interpret(true)
      .plain(false)
      .localPos(0, 0, 0)
      .build();

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testEntityNBTComponent() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.NBT).raw("Something[1].CustomName");
      n.node(ComponentTreeConstants.NBT_INTERPRET).raw(false);
      n.node(ComponentTreeConstants.NBT_ENTITY).raw("@e[limit=1]");
      n.node(ComponentTreeConstants.NBT_PLAIN).raw(false);
    });
    final Component component = Component.entityNBT()
      .nbtPath("Something[1].CustomName")
      .interpret(false)
      .plain(false)
      .selector("@e[limit=1]")
      .build();

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testStorageNBTComponent() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.NBT).raw("Kittens.Toes[0]");
      n.node(ComponentTreeConstants.NBT_INTERPRET).raw(false);
      n.node(ComponentTreeConstants.NBT_STORAGE).raw("adventure:purr");
      n.node(ComponentTreeConstants.NBT_PLAIN).raw(true);
    });
    final Component component = Component.storageNBT()
      .nbtPath("Kittens.Toes[0]")
      .interpret(false)
      .plain(true)
      .storage(Key.key("adventure", "purr"))
      .build();

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testComponentWithChildren() {
    final ConfigurationNode serialized = this.node(n -> {
      n.node(ComponentTreeConstants.TEXT).raw("Hello");
      n.node(ComponentTreeConstants.EXTRA).act(extra -> {
        extra.appendListNode().node(ComponentTreeConstants.TRANSLATE).raw("adventure.world");
        extra.appendListNode().node(ComponentTreeConstants.KEYBIND).raw("minecraft.key.jump");
      });
    });
    final Component component = Component.text().content("Hello")
      .append(Component.translatable("adventure.world"))
      .append(Component.keybind("minecraft.key.jump"))
      .build();

    this.assertRoundtrippable(component, serialized);
  }

  @Test
  void testArrayChildren() {
    final ConfigurationNode serialized = this.node(n -> {
      n.appendListNode().node(ComponentTreeConstants.TEXT).raw("first");
      n.appendListNode().act(child -> {
        child.node(ComponentTreeConstants.TRANSLATE).raw("keys.second");
        child.node(ComponentTreeConstants.COLOR).raw("#deadca");
      });
    });
    final Component deserialized = Component.text("first")
      .append(Component.translatable("keys.second", TextColor.color(0xdeadca)));

    assertEquals(deserialized, this.deserialize(serialized));
  }

  @Test
  void testSprite() {
    this.assertRoundtrippable(
      Component.object(ObjectContents.sprite(Key.key("item/diamond_sword"))),
      this.node(n -> n.node(ComponentTreeConstants.OBJECT_SPRITE).raw("minecraft:item/diamond_sword"))
    );
  }

  @Test
  void testSpriteAtlas() {
    this.assertRoundtrippable(
      Component.object(ObjectContents.sprite(Key.key("gui"), Key.key("icon/checkmark"))),
      this.node(n -> {
        n.node(ComponentTreeConstants.OBJECT_ATLAS).raw("minecraft:gui");
        n.node(ComponentTreeConstants.OBJECT_SPRITE).raw("minecraft:icon/checkmark");
      })
    );
  }

  @Test
  void testPlayerEmpty() {
    this.assertRoundtrippable(
      Component.object(ObjectContents.playerHead().build()),
      this.node(n -> {
        n.node(ComponentTreeConstants.OBJECT_PLAYER).raw(Map.of());
        n.node(ComponentTreeConstants.OBJECT_HAT).raw(true);
      })
    );
  }

  @Test
  void testPlayerName() {
    this.assertRoundtrippable(
      Component.object(ObjectContents.playerHead().name("Player123").build()),
      this.node(n -> {
        n.node(ComponentTreeConstants.OBJECT_PLAYER).raw("Player123");
        n.node(ComponentTreeConstants.OBJECT_HAT).raw(true);
      })
    );
  }

  @Test
  void testPlayerId() {
    final UUID id = UUID.randomUUID();
    this.assertRoundtrippable(
      Component.object(ObjectContents.playerHead().id(id).build()),
      this.node(n -> {
        n.node(ComponentTreeConstants.OBJECT_PLAYER).node(ComponentTreeConstants.OBJECT_PLAYER_ID).raw(id.toString());
        n.node(ComponentTreeConstants.OBJECT_HAT).raw(true);
      })
    );
  }

  @Test
  void testPlayerProperties() {
    this.assertRoundtrippable(
      Component.object(ObjectContents.playerHead().profileProperty(
        PlayerHeadObjectContents.property("textures", "cool_value", "cool_signature")
      ).build()),
      this.node(n -> {
        final BasicConfigurationNode property = n.node(ComponentTreeConstants.OBJECT_PLAYER)
          .node(ComponentTreeConstants.OBJECT_PLAYER_PROPERTIES)
          .appendListNode();
        property.node(ComponentTreeConstants.PROFILE_PROPERTY_NAME).raw("textures");
        property.node(ComponentTreeConstants.PROFILE_PROPERTY_VALUE).raw("cool_value");
        property.node(ComponentTreeConstants.PROFILE_PROPERTY_SIGNATURE).raw("cool_signature");
        n.node(ComponentTreeConstants.OBJECT_HAT).raw(true);
      })
    );
  }

  @Test
  void testPlayerMapPropertyFormat() {
    assertEquals(
      Component.object(ObjectContents.playerHead()
        .profileProperty(PlayerHeadObjectContents.property("textures", "cool_value"))
        .profileProperty(PlayerHeadObjectContents.property("textures", "cooler_value"))
        .build()),
      this.deserialize(this.node(n -> {
        final BasicConfigurationNode textures = n.node(ComponentTreeConstants.OBJECT_PLAYER)
          .node(ComponentTreeConstants.OBJECT_PLAYER_PROPERTIES)
          .node("textures");
        textures.appendListNode().raw("cool_value");
        textures.appendListNode().raw("cooler_value");
      }))
    );
  }

  @Test
  void testPlayerTexture() {
    this.assertRoundtrippable(
      Component.object(ObjectContents.playerHead().texture(Key.key("entity/player/wide/steve")).build()),
      this.node(n -> {
        n.node(ComponentTreeConstants.OBJECT_PLAYER)
          .node(ComponentTreeConstants.OBJECT_PLAYER_TEXTURE)
          .raw("minecraft:entity/player/wide/steve");
        n.node(ComponentTreeConstants.OBJECT_HAT).raw(true);
      })
    );
  }
}
