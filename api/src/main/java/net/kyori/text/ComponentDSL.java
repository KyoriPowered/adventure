/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ComponentDSL {
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
    return keybind().keybind(keybind);
  }

  /**
   * Creates a keybind component builder with a keybind and style.
   *
   * @param keybind the keybind
   * @param style the style
   * @return a builder
   */
  static KeybindComponent.@NonNull Builder keybind(final @NonNull String keybind, final @NonNull Style style) {
    return keybind(keybind).style(style);
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
   * Creates a selector component builder.
   *
   * @return a builder
   */
  static SelectorComponent.@NonNull Builder selector() {
    return SelectorComponent.builder();
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
   * Creates a text component builder with content.
   *
   * @param content the content
   * @return a builder
   */
  static TextComponent.@NonNull Builder text(final @NonNull String content) {
    return text().content(content);
  }

  /**
   * Creates a text component builder with content and style.
   *
   * @param content the content
   * @param style the style
   * @return a builder
   */
  static TextComponent.@NonNull Builder text(final @NonNull String content, final @NonNull Style style) {
    return text(content).style(style);
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
    return translatable().key(key);
  }

  /**
   * Creates a translatable component builder with a translation key and style.
   *
   * @param key the translation key
   * @param style the style
   * @return a builder
   */
  static TranslatableComponent.@NonNull Builder translatable(final @NonNull String key, final @NonNull Style style) {
    return translatable(key).style(style);
  }

  /**
   * Creates a translatable component builder with a translation key and translation args.
   *
   * @param key the translation key
   * @param args the translation args
   * @return a builder
   */
  static TranslatableComponent.@NonNull Builder translatable(final @NonNull String key, final @NonNull ComponentBuilder<?, ?>... args) {
    return translatable(key).args(args);
  }

  /**
   * Creates a translatable component builder with a translation key, style, and translation args.
   *
   * @param key the translation key
   * @param style the style
   * @param args the translation args
   * @return a builder
   */
  static TranslatableComponent.@NonNull Builder translatable(final @NonNull String key, final @NonNull Style style, final @NonNull ComponentBuilder<?, ?>... args) {
    return translatable(key, style).args(args);
  }

  /**
   * Creates a translatable component builder with a translation key and translation args.
   *
   * @param key the translation key
   * @param args the translation args
   * @return a builder
   */
  static TranslatableComponent.@NonNull Builder translatable(final @NonNull String key, final @NonNull Component... args) {
    return translatable(key).args(args);
  }

  /**
   * Creates a translatable component builder with a translation key, style, and translation args.
   *
   * @param key the translation key
   * @param style the style
   * @param args the translation args
   * @return a builder
   */
  static TranslatableComponent.@NonNull Builder translatable(final @NonNull String key, final @NonNull Style style, final @NonNull Component... args) {
    return translatable(key, style).args(args);
  }
}
