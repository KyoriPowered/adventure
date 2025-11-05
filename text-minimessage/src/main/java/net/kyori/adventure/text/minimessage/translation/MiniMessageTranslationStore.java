/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text.minimessage.translation;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.TranslationStore;

/**
 * A MiniMessage translation store.
 *
 * <p>For documentation on how to use the MiniMessage translation system, see the Javadocs
 * for {@link MiniMessageTranslator}.</p>
 *
 * @since 4.20.0
 */
public sealed interface MiniMessageTranslationStore extends TranslationStore.StringBased<String> permits MiniMessageTranslationStoreImpl {

  /**
   * Creates a MiniMessage translation store, backed by the default MiniMessage instance.
   *
   * @param name the name of the translation store
   * @return the translation store
   * @since 4.20.0
   */
  static MiniMessageTranslationStore create(final Key name) {
    return create(name, MiniMessage.miniMessage());
  }

  /**
   * Creates a MiniMessage translation store.
   *
   * @param name the name of the translation store
   * @param miniMessage the MiniMessage instance to use for deserialization
   * @return the translation store
   * @since 4.20.0
   */
  static MiniMessageTranslationStore create(final Key name, final MiniMessage miniMessage) {
    return new MiniMessageTranslationStoreImpl(name, miniMessage);
  }
}
