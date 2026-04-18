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
package net.kyori.adventure.text.serializer.nbt;

import java.util.List;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.ByteBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ObjectComponent;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_ATLAS;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_FALLBACK;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_HAT;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER_ID;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER_PROPERTIES;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_PLAYER_TEXTURE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.OBJECT_SPRITE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_SIGNATURE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_VALUE;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.asBoolean;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.optionalTag;
import static net.kyori.adventure.text.serializer.nbt.NBTSerializerUtils.requiredTag;

final class ObjectComponentSerializer {

  private ObjectComponentSerializer() {
  }

  static @NotNull ObjectComponent deserialize(final @NotNull CompoundBinaryTag compound,
                                              final @NotNull NBTComponentSerializerImpl serializer) {
    final ObjectContents contents;
    final StringBinaryTag spriteTag = optionalTag(compound, OBJECT_SPRITE, BinaryTagTypes.STRING);
    if (spriteTag != null) {
      final StringBinaryTag atlasTag = optionalTag(compound, OBJECT_ATLAS, BinaryTagTypes.STRING);
      contents = ObjectContents.sprite(
        atlasTag == null ? SpriteObjectContents.DEFAULT_ATLAS : KeySerializer.deserialize(atlasTag),
        KeySerializer.deserialize(spriteTag)
      );
    } else if (compound.get(OBJECT_PLAYER) != null) {
      final PlayerHeadObjectContents.Builder playerHead = ObjectContents.playerHead();

      final BinaryTag playerTag = requiredTag(compound, OBJECT_PLAYER);
      if (playerTag instanceof StringBinaryTag) {
        playerHead.name(((StringBinaryTag) playerTag).value());
      } else if (playerTag instanceof CompoundBinaryTag) {
        final CompoundBinaryTag playerCompound = (CompoundBinaryTag) playerTag;

        final StringBinaryTag nameTag = optionalTag(playerCompound, OBJECT_PLAYER_NAME, BinaryTagTypes.STRING);
        if (nameTag != null) playerHead.name(nameTag.value());

        final BinaryTag idTag = playerCompound.get(OBJECT_PLAYER_ID);
        if (idTag != null) playerHead.id(UUIDSerializer.deserialize(idTag));

        final BinaryTag propertiesTag = playerCompound.get(OBJECT_PLAYER_PROPERTIES);
        if (propertiesTag instanceof ListBinaryTag) {
          for (final BinaryTag propertyTag : (ListBinaryTag) propertiesTag) {
            if (propertyTag instanceof CompoundBinaryTag) {
              playerHead.profileProperty(deserializeProperty((CompoundBinaryTag) propertyTag));
            }
          }
        }

        final StringBinaryTag textureTag = optionalTag(playerCompound, OBJECT_PLAYER_TEXTURE, BinaryTagTypes.STRING);
        if (textureTag != null) playerHead.texture(KeySerializer.deserialize(textureTag));
      } else {
        throw new IllegalArgumentException("The " + OBJECT_PLAYER + " field must be either a string or a compound tag");
      }

      final ByteBinaryTag hatTag = optionalTag(compound, OBJECT_HAT, BinaryTagTypes.BYTE);
      if (hatTag != null) playerHead.hat(asBoolean(hatTag));

      contents = playerHead.build();
    } else {
      throw new IllegalArgumentException("Unable to determine object component contents: neither " + OBJECT_SPRITE + " nor " + OBJECT_PLAYER + " field is present");
    }

    final ObjectComponent.Builder builder = Component.object().contents(contents);

    final BinaryTag fallbackTag = compound.get(OBJECT_FALLBACK);
    if (fallbackTag != null) {
      builder.fallback(serializer.deserialize(fallbackTag));
    }

    return builder.build();
  }

  static void serialize(final @NotNull ObjectComponent component,
                        final @NotNull CompoundBinaryTag.Builder builder,
                        final @NotNull NBTComponentSerializerImpl serializer) {
    final Component fallback = component.fallback();
    if (fallback != null) {
      builder.put(OBJECT_FALLBACK, serializer.serialize(fallback));
    }

    final ObjectContents contents = component.contents();
    if (contents instanceof SpriteObjectContents) {
      final SpriteObjectContents spriteContents = (SpriteObjectContents) contents;
      if (!spriteContents.atlas().equals(SpriteObjectContents.DEFAULT_ATLAS)) {
        builder.put(OBJECT_ATLAS, KeySerializer.serialize(spriteContents.atlas()));
      }
      builder.put(OBJECT_SPRITE, KeySerializer.serialize(spriteContents.sprite()));
    } else if (contents instanceof PlayerHeadObjectContents) {
      final PlayerHeadObjectContents playerHead = (PlayerHeadObjectContents) contents;

      if (playerHead.hat() != PlayerHeadObjectContents.DEFAULT_HAT) {
        builder.putBoolean(OBJECT_HAT, playerHead.hat());
      }

      final String playerName = playerHead.name();
      final UUID playerId = playerHead.id();
      final List<PlayerHeadObjectContents.ProfileProperty> properties = playerHead.profileProperties();
      final Key texture = playerHead.texture();

      if (playerName != null && playerId == null && properties.isEmpty() && texture == null) {
        builder.putString(OBJECT_PLAYER, playerName);
      } else {
        final CompoundBinaryTag.Builder playerBuilder = CompoundBinaryTag.builder();
        if (playerName != null) playerBuilder.putString(OBJECT_PLAYER_NAME, playerName);
        if (playerId != null) playerBuilder.put(OBJECT_PLAYER_ID, UUIDSerializer.serialize(playerId));
        if (!properties.isEmpty()) {
          final ListBinaryTag.Builder<CompoundBinaryTag> propertiesBuilder = ListBinaryTag.builder(BinaryTagTypes.COMPOUND);
          for (final PlayerHeadObjectContents.ProfileProperty property : properties) {
            propertiesBuilder.add(serializeProperty(property));
          }
          playerBuilder.put(OBJECT_PLAYER_PROPERTIES, propertiesBuilder.build());
        }
        if (texture != null) playerBuilder.put(OBJECT_PLAYER_TEXTURE, KeySerializer.serialize(texture));
        builder.put(OBJECT_PLAYER, playerBuilder.build());
      }
    } else {
      throw new IllegalArgumentException("Don't know how to serialize object contents: " + contents);
    }
  }

  private static PlayerHeadObjectContents.@NotNull ProfileProperty deserializeProperty(final @NotNull CompoundBinaryTag compound) {
    final String name = requiredTag(compound, PROFILE_PROPERTY_NAME, BinaryTagTypes.STRING).value();
    final String value = requiredTag(compound, PROFILE_PROPERTY_VALUE, BinaryTagTypes.STRING).value();
    final StringBinaryTag signatureTag = optionalTag(compound, PROFILE_PROPERTY_SIGNATURE, BinaryTagTypes.STRING);
    return PlayerHeadObjectContents.property(name, value, signatureTag == null ? null : signatureTag.value());
  }

  private static @NotNull CompoundBinaryTag serializeProperty(final PlayerHeadObjectContents.@NotNull ProfileProperty property) {
    final CompoundBinaryTag.Builder builder = CompoundBinaryTag.builder()
      .putString(PROFILE_PROPERTY_NAME, property.name())
      .putString(PROFILE_PROPERTY_VALUE, property.value());
    final String signature = property.signature();
    if (signature != null) builder.putString(PROFILE_PROPERTY_SIGNATURE, signature);
    return builder.build();
  }
}
