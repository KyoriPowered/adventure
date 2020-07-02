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
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A gson component serializer.
 *
 * <p>Use {@link Builder#downsampleColors()} to support platforms
 * that do not understand hex colors that were introduced in Minecraft 1.16.</p>
 */
public interface GsonComponentSerializer extends ComponentSerializer<Component, Component, String>, Buildable<GsonComponentSerializer, GsonComponentSerializer.Builder> {
  /**
   * Gets a component serializer for gson serialization and deserialization.
   *
   * @return a gson component serializer
   */
  static @NonNull GsonComponentSerializer gson() {
    return GsonComponentSerializerImpl.INSTANCE;
  }

  /**
   * Gets a component serializer for gson serialization and deserialization.
   *
   * <p>Hex colors are coerced to the nearest named color, and legacy hover events are
   * emitted for action {@link net.kyori.adventure.text.event.HoverEvent.Action#SHOW_TEXT}.</p>
   *
   * @return a gson component serializer
   */
  static @NonNull GsonComponentSerializer colorDownsamplingGson() {
    return GsonComponentSerializerImpl.LEGACY_INSTANCE;
  }

  /**
   * Creates a new {@link GsonComponentSerializer.Builder}.
   *
   * @return a builder
   */
  static Builder builder() {
    return new GsonComponentSerializerImpl.BuilderImpl();
  }

  /**
   * Gets the underlying gson serializer.
   *
   * @return a gson serializer
   */
  Gson serializer();

  /**
   * Gets the underlying gson populator.
   *
   * @return a gson populator
   */
  UnaryOperator<GsonBuilder> populator();

  /**
   * A builder for {@link GsonComponentSerializer}.
   */
  interface Builder extends Buildable.AbstractBuilder<GsonComponentSerializer> {
    /**
     * Sets that the serializer should downsample hex colors to named colors.
     *
     * @return this builder
     */
    @NonNull Builder downsampleColors();

    /**
     * Sets a serializer that will be used to interpret legacy hover event {@code value} payloads
     * @param serializer serializer
     * @return this builder
     */
    @NonNull Builder legacyHoverEventSerializer(final @NonNull LegacyHoverEventSerializer serializer);

    /**
     * Output a legacy hover event {@code value} in addition to the modern {@code contents}
     *
     * <p>Calling {@link #build()} after calling this method will only succeed if a
     * {@link #legacyHoverEventSerializer(LegacyHoverEventSerializer)  hover event adapter} has been set</p>
     *
     * @return this builder
     */
    @NonNull Builder emitLegacyHoverEvent();

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     */
    @Override
    @NonNull GsonComponentSerializer build();
  }
}
