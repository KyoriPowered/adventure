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
package net.kyori.adventure.translation;

import java.util.Locale;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import net.kyori.examination.Examinable;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A global source of translations.
 *
 * @since 4.0.0
 */
public interface GlobalTranslationSource extends TranslationSource, Examinable {
  /**
   * Gets the global translation source.
   *
   * @return the source
   * @since 4.0.0
   */
  static @NonNull GlobalTranslationSource get() {
    return GlobalTranslationSourceImpl.INSTANCE;
  }

  /**
   * Gets a renderer which uses the global source for translating.
   *
   * @return a renderer
   * @since 4.0.0
   */
  static @NonNull TranslatableComponentRenderer<Locale> renderer() {
    return GlobalTranslationSourceImpl.INSTANCE.renderer;
  }

  /**
   * Registers a translation source.
   *
   * <p>The global source may not respond to keys that are registered or unregistered in the sub-source
   * after registration. However, sources may be re-registered at any time.</p>
   *
   * @param source the source
   * @since 4.0.0
   */
  void register(final @NonNull TranslationSource source);

  /**
   * Unregisters a translation source.
   *
   * @param key the key to unregister
   * @since 4.0.0
   */
  void unregister(final @NonNull TranslationSource key);
}
