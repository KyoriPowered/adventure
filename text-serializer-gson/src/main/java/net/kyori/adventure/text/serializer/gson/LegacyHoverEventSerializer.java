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

import java.io.IOException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.util.Codec;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Adapter to convert between modern and legacy hover event formats.
 *
 * @since 4.0.0
 */
public interface LegacyHoverEventSerializer {
  /**
   * Convert a legacy hover event {@code show_item} value to its modern format.
   *
   * @param input component whose plain-text value is a SNBT string
   * @return the deserialized event
   * @throws IOException if the input is improperly formatted
   * @since 4.0.0
   */
  HoverEvent.@NonNull ShowItem deserializeShowItem(final @NonNull Component input) throws IOException;

  /**
   * Convert a legacy hover event {@code show_entity} value to its modern format.
   *
   * @param input component whose plain-text value is a SNBT string
   * @param componentDecoder A decoder that can take a JSON string and return a deserialized component
   * @return the deserialized event
   * @throws IOException if the input is improperly formatted
   * @since 4.0.0
   */
  HoverEvent.@NonNull ShowEntity deserializeShowEntity(final @NonNull Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentDecoder) throws IOException;

  /**
   * Convert a modern hover event {@code show_item} value to its legacy format.
   *
   * @param input modern hover event
   * @return component with the legacy value as a SNBT string
   * @throws IOException if the input is improperly formatted
   * @since 4.0.0
   */
  @NonNull Component serializeShowItem(final HoverEvent.@NonNull ShowItem input) throws IOException;

  /**
   * Convert a modern hover event {@code show_entity} value to its legacy format.
   *
   * @param input modern hover event
   * @param componentEncoder An encoder that can take a {@link Component} and return a JSON string
   * @return component with the legacy value as a SNBT string
   * @throws IOException if the input is improperly formatted
   * @since 4.0.0
   */
  @NonNull Component serializeShowEntity(final HoverEvent.@NonNull ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentEncoder) throws IOException;
}
