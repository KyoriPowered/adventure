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
package net.kyori.adventure.text.serializer.legacy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

/**
 * A legacy component serializer.
 *
 * <p>Legacy does <b>not</b> support more complex features such as, but not limited
 * to, {@link ClickEvent} and {@link HoverEvent}.</p>
 */
public interface LegacyComponentSerializer extends ComponentSerializer<Component, TextComponent, String> {
  /**
   * Gets a component serializer for legacy-based serialization and deserialization. Note that this
   * serializer works exactly like vanilla Minecraft and does not detect any links. If you want to
   * detect and make URLs clickable, use {@link #legacyLinking()}.
   *
   * @return a component serializer for legacy serialization and deserialization
   */
  static @NonNull LegacyComponentSerializer legacy() {
    return LegacyComponentSerializerImpl.INSTANCE;
  }

  /**
   * Gets a legacy component serializer for legacy serialization and deserialization that detects
   * and makes URLs clickable in chat.
   *
   * @return a component serializer for legacy serialization and deserialization that detects and
   *         makes URLs clickable in chat
   */
  static @NonNull LegacyComponentSerializer legacyLinking() {
    return LinkingLegacyComponentSerializer.NO_STYLE;
  }

  /**
   * Creates a legacy component serializer for legacy-based serialization and deserialization that
   * detects links and applies {@code style}.
   *
   * @param style the style to use
   * @return a legacy component serializer for legacy-based serialization and deserialization that
   *         styles links with the specified {@code style}
   */
  static @NonNull LegacyComponentSerializer legacyLinking(final @NonNull Style style) {
    return new LinkingLegacyComponentSerializer(requireNonNull(style));
  }

  /**
   * The legacy character.
   */
  char CHARACTER = '\u00A7';

  /**
   * Deserialize a component from a {@link String} with the {@link #CHARACTER legacy character}.
   *
   * @param input the input
   * @return the component
   */
  @Override
  default @NonNull TextComponent deserialize(final @NonNull String input) {
    return this.deserialize(input, CHARACTER);
  }

  /**
   * Deserialize a component from a {@link String} with the specified {@code character legacy character}.
   *
   * @param input the input
   * @param character the legacy character
   * @return the component
   */
  @NonNull TextComponent deserialize(final @NonNull String input, final char character);

  /**
   * Serializes a component into a {@link String} with the specified {@link #CHARACTER legacy character}.
   *
   * @param component the component
   * @return the string
   */
  @Override
  default @NonNull String serialize(final @NonNull Component component) {
    return this.serialize(component, CHARACTER);
  }

  /**
   * Serializes a component into a {@link String} with the specified {@code character legacy character}.
   *
   * @param component the component
   * @param character the legacy character
   * @return the string
   */
  @NonNull String serialize(final @NonNull Component component, final char character);
}
