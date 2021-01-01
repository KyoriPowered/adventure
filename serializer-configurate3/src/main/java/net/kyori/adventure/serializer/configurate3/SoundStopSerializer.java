/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
import java.util.Collections;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("UnstableApiUsage") // TypeToken
final class SoundStopSerializer implements TypeSerializer<SoundStop> {
  static final TypeToken<SoundStop> TYPE = TypeToken.of(SoundStop.class);
  static final SoundStopSerializer INSTANCE = new SoundStopSerializer();

  static final String SOUND = "sound";
  static final String SOURCE = "source";

  private SoundStopSerializer() {
  }

  @Override
  public SoundStop deserialize(final @NonNull TypeToken<?> type, final @NonNull ConfigurationNode value) throws ObjectMappingException {
    if(value.isEmpty()) {
      return SoundStop.all();
    } else {
      final Key sound = value.getNode(SOUND).getValue(KeySerializer.INSTANCE.type());
      final Sound.Source source = value.getNode(SOURCE).getValue(SoundSerializer.SOURCE_TYPE);
      if(sound == null) {
        return source == null ? SoundStop.all() : SoundStop.source(source);
      } else {
        return source == null ? SoundStop.named(sound) : SoundStop.namedOnSource(sound, source);
      }
    }
  }

  @Override
  public void serialize(final @NonNull TypeToken<?> type, final @Nullable SoundStop obj, final @NonNull ConfigurationNode value) throws ObjectMappingException {
    value.getNode(SOUND).setValue(KeySerializer.INSTANCE.type(), obj == null ? null : obj.sound());
    value.getNode(SOURCE).setValue(SoundSerializer.SOURCE_TYPE, obj == null ? null : obj.source());
    if(value.isEmpty()) {
      value.setValue(Collections.emptyMap());
    }
  }
}
