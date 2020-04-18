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
package net.kyori.adventure.text;

import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ComponentBuilders {
  /**
   * Creates a block NBT component builder.
   *
   * @return a builder
   */
  static BlockNbtComponent.@NonNull Builder blockNbt() {
    return BlockNbtComponent.builder();
  }

  /**
   * Creates an entity NBT component builder.
   *
   * @return a builder
   */
  static EntityNbtComponent.@NonNull Builder entityNbt() {
    return EntityNbtComponent.builder();
  }

  /**
   * Creates an storage NBT component builder.
   *
   * @return a builder
   */
  static StorageNbtComponent.@NonNull Builder storageNbt() {
    return StorageNbtComponent.builder();
  }

  /**
   * Creates a keybind component builder.
   *
   * @return a builder
   */
  static KeybindComponent.@NonNull Builder keybind() {
    return KeybindComponent.builder();
  }

  /**
   * Creates a keybind component builder with a keybind.
   *
   * @param keybind the keybind
   * @return a builder
   */
  static KeybindComponent.@NonNull Builder keybind(final @NonNull String keybind) {
    return KeybindComponent.builder(keybind);
  }

  /**
   * Creates a score component builder.
   *
   * @return a builder
   */
  static ScoreComponent.@NonNull Builder score() {
    return ScoreComponent.builder();
  }

  /**
   * Creates a score component builder with a name and objective.
   *
   * @param name the score name
   * @param objective the score objective
   * @return a builder
   */
  static ScoreComponent.@NonNull Builder score(final @NonNull String name, final @NonNull String objective) {
    return ScoreComponent.builder(name, objective);
  }

  /**
   * Creates a selector component builder.
   *
   * @return a builder
   */
  static SelectorComponent.@NonNull Builder selector() {
    return SelectorComponent.builder();
  }

  /**
   * Creates a selector component builder with a pattern.
   *
   * @param pattern the selector pattern
   * @return a builder
   */
  static SelectorComponent.@NonNull Builder selector(final @NonNull String pattern) {
    return SelectorComponent.builder(pattern);
  }

  /**
   * Creates a text component builder.
   *
   * @return a builder
   */
  static TextComponent.@NonNull Builder text() {
    return TextComponent.builder();
  }

  /**
   * Creates a text component builder with content, and optional color.
   *
   * @param content the plain text content
   * @param color the color
   * @return a builder
   */
  static TextComponent.@NonNull Builder text(final @NonNull String content, final @Nullable TextColor color) {
    return TextComponent.builder(content).color(color);
  }

  /**
   * Creates a text component builder with the content of {@link String#valueOf(char)}.
   *
   * @param value the char value
   * @return the component
   */
  static TextComponent.@NonNull Builder text(final char value) {
    return text(String.valueOf(value));
  }

  /**
   * Creates a text component builder with the content of {@link String#valueOf(double)}.
   *
   * @param value the double value
   * @return the component
   */
  static TextComponent.@NonNull Builder text(final double value) {
    return text(String.valueOf(value));
  }

  /**
   * Creates a text component builder with the content of {@link String#valueOf(float)}.
   *
   * @param value the float value
   * @return the component
   */
  static TextComponent.@NonNull Builder text(final float value) {
    return text(String.valueOf(value));
  }

  /**
   * Creates a text component builder with the content of {@link String#valueOf(int)}.
   *
   * @param value the int value
   * @return the component
   */
  static TextComponent.@NonNull Builder text(final int value) {
    return text(String.valueOf(value));
  }

  /**
   * Creates a text component builder with the content of {@link String#valueOf(long)}.
   *
   * @param value the long value
   * @return the component
   */
  static TextComponent.@NonNull Builder text(final long value) {
    return text(String.valueOf(value));
  }

  /**
   * Creates a text component builder with content.
   *
   * @param content the plain text content
   * @return a builder
   */
  static TextComponent.@NonNull Builder text(final @NonNull String content) {
    return TextComponent.builder(content);
  }

  /**
   * Creates a translatable component builder.
   *
   * @return a builder
   */
  static TranslatableComponent.@NonNull Builder translatable() {
    return TranslatableComponent.builder();
  }

  /**
   * Creates a translatable component builder with a translation key.
   *
   * @param key the translation key
   * @return a builder
   */
  static TranslatableComponent.@NonNull Builder translatable(final @NonNull String key) {
    return TranslatableComponent.builder(key);
  }
}
