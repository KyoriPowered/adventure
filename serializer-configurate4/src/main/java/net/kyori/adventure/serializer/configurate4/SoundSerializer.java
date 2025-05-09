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
import java.util.OptionalLong;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

final class SoundSerializer implements TypeSerializer<Sound> {
  static final SoundSerializer INSTANCE = new SoundSerializer();

  static final String NAME = "name";
  static final String SOURCE = "source";
  static final String PITCH = "pitch";
  static final String VOLUME = "volume";
  static final String SEED = "seed";

  private SoundSerializer() {
  }

  @Override
  public @Nullable Sound deserialize(final @NotNull Type type, final @NotNull ConfigurationNode value) throws SerializationException {
    if (value.empty()) {
      return null;
    }

    final Sound.Builder builder = Sound.sound();
    final Key name = value.node(NAME).get(Key.class);
    final Sound.Source source = value.node(SOURCE).get(Sound.Source.class);
    if (name == null || source == null) {
      throw new SerializationException("A name and source are required to deserialize a Sound");
    }
    builder
      .type(name)
      .source(source)
      .volume(value.node(VOLUME).getFloat(1.0f))
      .pitch(value.node(PITCH).getFloat(1.0f));
    final ConfigurationNode seed = value.node(SEED);
    if (!seed.virtual()) {
      builder.seed(OptionalLong.of(seed.getLong()));
    }

    return builder.build();
  }

  @Override
  public void serialize(final @NotNull Type type, final @Nullable Sound obj, final @NotNull ConfigurationNode value) throws SerializationException {
    if (obj == null) {
      value.set(null);
      return;
    }
    value.node(NAME).set(Key.class, obj.name());
    value.node(SOURCE).set(Sound.Source.class, obj.source());
    value.node(VOLUME).set(obj.volume());
    value.node(PITCH).set(obj.pitch());
    if (obj.seed().isPresent()) {
      value.node(SEED).set(obj.seed().getAsLong());
    } else {
      value.node(SEED).set(null);
    }
  }
}
