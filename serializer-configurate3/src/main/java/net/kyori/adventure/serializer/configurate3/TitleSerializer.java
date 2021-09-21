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
import java.time.Duration;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage") // TypeToken
final class TitleSerializer implements TypeSerializer<Title> {
  static final TypeToken<Title> TYPE = TypeToken.of(Title.class);
  static final TitleSerializer INSTANCE = new TitleSerializer();

  private TitleSerializer() {
  }

  static final Duration KEEP = Duration.ofSeconds(-1);
  static final String TITLE = "title";
  static final String SUBTITLE = "subtitle";
  static final String TIMES = "times";
  static final String FADE_IN = "fade-in";
  static final String STAY = "stay";
  static final String FADE_OUT = "fade-out";

  @Override
  public @Nullable Title deserialize(final @NotNull TypeToken<?> type, final @NotNull ConfigurationNode value) throws ObjectMappingException {
    if (value.isEmpty()) {
      return null;
    }
    final Component title = value.getNode(TITLE).getValue(ComponentTypeSerializer.TYPE, Component.empty());
    final Component subtitle = value.getNode(SUBTITLE).getValue(ComponentTypeSerializer.TYPE, Component.empty());

    final Duration fadeIn = value.getNode(TIMES, FADE_IN).getValue(DurationSerializer.INSTANCE.type(), KEEP);
    final Duration stay = value.getNode(TIMES, STAY).getValue(DurationSerializer.INSTANCE.type(), KEEP);
    final Duration fadeOut = value.getNode(TIMES, FADE_OUT).getValue(DurationSerializer.INSTANCE.type(), KEEP);

    if (!Objects.equals(fadeIn, KEEP) || !Objects.equals(stay, KEEP) || !Objects.equals(fadeOut, KEEP)) {
      return Title.title(title, subtitle, Title.Times.times(fadeIn, stay, fadeOut));
    } else {
      return Title.title(title, subtitle);
    }
  }

  @Override
  public void serialize(final @NotNull TypeToken<?> type, final @Nullable Title obj, final @NotNull ConfigurationNode value) throws ObjectMappingException {
    if (obj == null) {
      value.setValue(null);
      return;
    }

    value.getNode(TITLE).setValue(ComponentTypeSerializer.TYPE, obj.title());
    value.getNode(SUBTITLE).setValue(ComponentTypeSerializer.TYPE, obj.subtitle());
    final Title.@Nullable Times times = obj.times();
    value.getNode(TIMES, FADE_IN).setValue(DurationSerializer.INSTANCE.type(), times == null || times == Title.DEFAULT_TIMES ? null : times.fadeIn());
    value.getNode(TIMES, STAY).setValue(DurationSerializer.INSTANCE.type(), times == null || times == Title.DEFAULT_TIMES ? null : times.stay());
    value.getNode(TIMES, FADE_OUT).setValue(DurationSerializer.INSTANCE.type(), times == null || times == Title.DEFAULT_TIMES ? null : times.fadeOut());
  }
}
