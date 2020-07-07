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
import net.kyori.adventure.util.Buildable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A legacy component serializer.
 *
 * <p>Legacy does <b>not</b> support more complex features such as, but not limited
 * to, {@link ClickEvent} and {@link HoverEvent}.</p>
 */
public interface LegacyComponentSerializer extends ComponentSerializer<Component, TextComponent, String>, Buildable<LegacyComponentSerializer, LegacyComponentSerializer.Builder> {
  /**
   * Gets a component serializer for legacy-based serialization and deserialization. Note that this
   * serializer works exactly like vanilla Minecraft and does not detect any links. If you want to
   * detect and make URLs clickable, use {@link Builder#extractUrls()}.
   *
   * @return a component serializer for legacy serialization and deserialization
   */
  static @NonNull LegacyComponentSerializer legacy() {
    return LegacyComponentSerializerImpl.SECTION_SERIALIZER;
  }

  /**
   * Gets a component serializer for legacy-based serialization and deserialization. Note that this
   * serializer works exactly like vanilla Minecraft and does not detect any links. If you want to
   * detect and make URLs clickable, use {@link Builder#extractUrls()}.
   *
   * @param legacyCharacter the legacy character to use
   * @return a component serializer for legacy serialization and deserialization
   */
  static @NonNull LegacyComponentSerializer legacy(final char legacyCharacter) {
    switch(legacyCharacter) {
      case SECTION_CHAR:
        return LegacyComponentSerializerImpl.SECTION_SERIALIZER;
      case AMPERSAND_CHAR:
        return LegacyComponentSerializerImpl.AMPERSAND_SERIALIZER;
      default:
        return builder().character(legacyCharacter).build();
    }
  }

  /**
   * Creates a new {@link LegacyComponentSerializer.Builder}.
   *
   * @return the builder
   */
  static Builder builder() {
    return new LegacyComponentSerializerImpl.BuilderImpl();
  }

  /**
   * The legacy character used by Minecraft. ('§')
   */
  char SECTION_CHAR = '§';

  /**
   * The legacy character frequently used by configurations and commands. ('&amp;')
   */
  char AMPERSAND_CHAR = '&';

  /**
   * The legacy character used to prefix hex colors. ('#')
   */
  char HEX_CHAR = '#';

  /**
   * Deserialize a component from a legacy {@link String}.
   *
   * @param input the input
   * @return the component
   */
  @Override
  @NonNull TextComponent deserialize(final @NonNull String input);

  /**
   * Serializes a component into a legacy {@link String}.
   *
   * @param component the component
   * @return the string
   */
  @Override
  @NonNull String serialize(final @NonNull Component component);

  /**
   * A builder for {@link LegacyComponentSerializer}.
   */
  interface Builder extends Buildable.AbstractBuilder<LegacyComponentSerializer> {
    /**
     * Sets the legacy character used by the serializer.
     *
     * @param legacyCharacter the legacy character
     * @return this builder
     */
    @NonNull Builder character(final char legacyCharacter);

    /**
     * Sets the legacy hex character used by the serializer.
     *
     * @param legacyHexCharacter the legacy hex character.
     * @return this builder
     */
    @NonNull Builder hexCharacter(final char legacyHexCharacter);

    /**
     * Sets that the serializer should extract URLs into {@link ClickEvent}s
     * when deserializing.
     *
     * @return this builder
     */
    @NonNull Builder extractUrls();

    /**
     * Sets that the serializer should extract URLs into {@link ClickEvent}s
     * when deserializing.
     *
     * @param style the style to use for extracted links
     * @return this builder
     */
    @NonNull Builder extractUrls(final @Nullable Style style);

    /**
     * Sets that the serializer should support hex colors.
     *
     * <p>Otherwise, hex colors are downsampled to the nearest named color.</p>
     *
     * @return this builder
     */
    @NonNull Builder hexColors();

    /**
     * Sets that the serializer should use the '&amp;x' repeated code format when serializing hex
     * colors. Note that messages in this format can still be deserialized, even with this option
     * disabled.
     *
     * <p>This is the format adopted by the BungeeCord (and by usage, Spigot) text API.</p>
     *
     * <p>The format is difficult to manipulate and read, and its use is not recommended. Support
     * is provided for it only to allow plugin developers to use this library alongside parts of
     * the Spigot API which expect legacy strings in this format.</p>
     *
     * <p>It is recommended to use only when absolutely necessary, and when no better alternatives
     * are available.</p>
     *
     * @return this builder
     */
    @NonNull Builder useUnusualXRepeatedCharacterHexFormat();

    /**
     * Builds the serializer.
     *
     * @return the built serializer
     */
    @Override
    @NonNull LegacyComponentSerializer build();
  }
}
