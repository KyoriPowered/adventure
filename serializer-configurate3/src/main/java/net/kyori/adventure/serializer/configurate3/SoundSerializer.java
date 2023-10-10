/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.serializer.configurate3;

import com.google.common.reflect.TypeToken;
import java.util.OptionalLong;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage") // TypeToken
@NullMarked
final class SoundSerializer implements TypeSerializer<Sound> {
  static final TypeToken<Sound> TYPE = TypeToken.of(Sound.class);
  static final TypeToken<Sound.Source> SOURCE_TYPE = TypeToken.of(Sound.Source.class);
  static final SoundSerializer INSTANCE = new SoundSerializer();

  static final String NAME = "name";
  static final String SOURCE = "source";
  static final String PITCH = "pitch";
  static final String VOLUME = "volume";
  static final String SEED = "seed";

  private SoundSerializer() {
  }

  @Override
  public @Nullable Sound deserialize(final TypeToken<?> type, final ConfigurationNode value) throws ObjectMappingException {
    if (value.isEmpty()) {
      return null;
    }

    final Sound.Builder builder = Sound.sound();
    final Key name = value.getNode(NAME).getValue(KeySerializer.INSTANCE.type());
    final Sound.Source source = value.getNode(SOURCE).getValue(SOURCE_TYPE);
    if (name == null || source == null) {
      throw new ObjectMappingException("A name and source are required to deserialize a Sound");
    }

    builder
      .type(name)
      .source(source)
      .volume(value.getNode(VOLUME).getFloat(1.0f))
      .pitch(value.getNode(PITCH).getFloat(1.0f));
    final ConfigurationNode seed = value.getNode(SEED);
    if (!seed.isVirtual()) {
      builder.seed(OptionalLong.of(seed.getLong()));
    }

    return builder.build();
  }

  @Override
  public void serialize(final TypeToken<?> type, final @Nullable Sound obj, final ConfigurationNode value) throws ObjectMappingException {
    if (obj == null) {
      value.setValue(null);
      return;
    }
    value.getNode(NAME).setValue(KeySerializer.INSTANCE.type(), obj.name());
    value.getNode(SOURCE).setValue(SOURCE_TYPE, obj.source());
    value.getNode(VOLUME).setValue(obj.volume());
    value.getNode(PITCH).setValue(obj.pitch());
    if (obj.seed().isPresent()) {
      value.getNode(SEED).setValue(obj.seed().getAsLong());
    } else {
      value.getNode(SEED).setValue(null);
    }
  }
}
