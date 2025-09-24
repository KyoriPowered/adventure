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

import java.lang.reflect.Type;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_NAME;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_SIGNATURE;
import static net.kyori.adventure.text.serializer.commons.ComponentTreeConstants.PROFILE_PROPERTY_VALUE;

final class ProfilePropertySerializer implements TypeSerializer<PlayerHeadObjectContents.ProfileProperty> {
  static final TypeSerializer<PlayerHeadObjectContents.ProfileProperty> INSTANCE = new ProfilePropertySerializer();

  private ProfilePropertySerializer() {
  }

  @Override
  public PlayerHeadObjectContents.ProfileProperty deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
    final String name = node.node(PROFILE_PROPERTY_NAME).getString();
    final String value = node.node(PROFILE_PROPERTY_VALUE).getString();
    if (name == null || value == null) {
      throw new SerializationException("A profile property requires both a " + PROFILE_PROPERTY_NAME
        + " and " + PROFILE_PROPERTY_VALUE);
    }
    return PlayerHeadObjectContents.property(name, value, node.node(PROFILE_PROPERTY_SIGNATURE).getString());
  }

  @Override
  public void serialize(final Type type, final PlayerHeadObjectContents.@Nullable ProfileProperty obj, final ConfigurationNode node) throws SerializationException {
    if (obj == null) {
      return;
    }

    node.node(PROFILE_PROPERTY_NAME).set(obj.name());
    node.node(PROFILE_PROPERTY_VALUE).set(obj.value());
    if (obj.signature() != null) {
      node.node(PROFILE_PROPERTY_SIGNATURE).set(obj.signature());
    }
  }
}
