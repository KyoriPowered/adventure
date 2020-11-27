/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
import java.util.Arrays;
import java.util.HashSet;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("UnstableApiUsage") // TypeToken
final class ConfigurateComponentSerializerImpl implements ConfigurateComponentSerializer {
  private final ConfigurationOptions ownNodeOptions;
  private final @Nullable ComponentSerializer<Component, ?, String> stringSerializer;
  private final boolean serializeStringComponents;
  static final ConfigurateComponentSerializer INSTANCE = new Builder().build();

  private ConfigurateComponentSerializerImpl(final @NonNull Builder builder) {
    this.stringSerializer = builder.stringSerializer;
    this.serializeStringComponents = builder.outputStringComponents;
    this.ownNodeOptions = ConfigurationOptions.defaults()
      .withSerializers(this::addSerializersTo)
      .withNativeTypes(new HashSet<>(Arrays.asList(String.class, Integer.class, Boolean.class, Double.class, Float.class)));
  }

  @Override
  public @NonNull Component deserialize(final @NonNull ConfigurationNode input) {
    try {
      final @Nullable Component deserialized = input.getValue(ComponentTypeSerializer.TYPE);
      if(deserialized != null) {
        return deserialized;
      }
    } catch(final ObjectMappingException e) {
      throw new IllegalArgumentException(e);
    }
    throw new IllegalArgumentException("No value present");
  }

  @Override
  public @NonNull ConfigurationNode serialize(final @NonNull Component component) {
    final ConfigurationNode base = ConfigurationNode.root(this.ownNodeOptions);
    try {
      base.setValue(ComponentTypeSerializer.TYPE, component);
    } catch(final ObjectMappingException e) {
      throw new IllegalStateException("Unable to serialize component " + component, e);
    }
    return base;
  }

  @Override
  @SuppressWarnings("serial")
  public @NonNull TypeSerializerCollection addSerializersTo(final @NonNull TypeSerializerCollection serializers) {
    return serializers
      .register(BookTypeSerializer.TYPE, BookTypeSerializer.INSTANCE)
      .register(TitleSerializer.TYPE, TitleSerializer.INSTANCE)
      .register(SoundSerializer.TYPE, SoundSerializer.INSTANCE)
      .register(SoundStopSerializer.TYPE, SoundStopSerializer.INSTANCE)
      .register(ComponentTypeSerializer.TYPE, new ComponentTypeSerializer(this.stringSerializer, this.serializeStringComponents))

      // shared types
      .register(KeySerializer.INSTANCE)
      .register(DurationSerializer.INSTANCE)
      .register(StyleSerializer.TYPE, StyleSerializer.INSTANCE)
      .register(TextColorSerializer.INSTANCE)
      .register(BlockNBTPosSerializer.INSTANCE)
      .register(new IndexSerializer<>(TypeToken.of(ClickEvent.Action.class), ClickEvent.Action.NAMES))
      .register(new IndexSerializer<>(new TypeToken<HoverEvent.Action<?>>() {}, HoverEvent.Action.NAMES))
      .register(new IndexSerializer<>(TypeToken.of(Sound.Source.class), Sound.Source.NAMES))
      .register(new IndexSerializer<>(TypeToken.of(TextDecoration.class), TextDecoration.NAMES))
      .register(HoverEventShowEntitySerializer.TYPE, HoverEventShowEntitySerializer.INSTANCE)
      .register(HoverEventShowItemSerializer.TYPE, HoverEventShowItemSerializer.INSTANCE);
  }

  static class Builder implements ConfigurateComponentSerializer.Builder {
    private @Nullable ComponentSerializer<Component, ?, String> stringSerializer;
    private boolean outputStringComponents = false;

    Builder() {
    }

    @Override
    public ConfigurateComponentSerializer.@NonNull Builder scalarSerializer(final @NonNull ComponentSerializer<Component, ?, String> stringSerializer) {
      this.stringSerializer = requireNonNull(stringSerializer, "stringSerializer");
      return this;
    }

    @Override
    public ConfigurateComponentSerializer.@NonNull Builder outputStringComponents(final boolean stringComponents) {
      this.outputStringComponents = stringComponents;
      return this;
    }

    @Override
    public @NonNull ConfigurateComponentSerializer build() {
      return new ConfigurateComponentSerializerImpl(this);
    }
  }
}
