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
package net.kyori.adventure.text;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

/**
 * A {@link Component} that displays the client's current keybind for the supplied action.
 *
 * <p>This component takes:</p>
 * <dl>
 *   <dt>keybind</dt>
 *   <dd>a keybind identifier for a action. (e.g key.inventory, key.jump etc..)</dd>
 * </dl>
 *
 * @since 4.0.0
 * @sinceMinecraft 1.12
 */
public interface KeybindComponent extends BuildableComponent<KeybindComponent, KeybindComponent.Builder>, ScopedComponent<KeybindComponent> {
  /**
   * Gets the keybind.
   *
   * @return the keybind
   * @since 4.0.0
   */
  @NonNull String keybind();

  /**
   * Sets the keybind.
   *
   * @param keybind the keybind
   * @return a copy of this component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NonNull KeybindComponent keybind(final @NonNull String keybind);

  /**
   * A keybind component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends ComponentBuilder<KeybindComponent, Builder> {
    /**
     * Sets the keybind.
     *
     * @param keybind the keybind
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NonNull Builder keybind(final @NonNull String keybind);
  }
}
