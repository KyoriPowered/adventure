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
package net.kyori.adventure.text;

import java.util.Objects;
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
@SuppressWarnings("removal")
public sealed interface KeybindComponent extends ScopedComponent<KeybindComponent>, BuildableComponent<KeybindComponent, KeybindComponent.Builder> permits KeybindComponentImpl {
  /**
   * Gets the keybind.
   *
   * @return the keybind
   * @since 4.0.0
   */
  String keybind();

  /**
   * Sets the keybind.
   *
   * @param keybind the keybind
   * @return a copy of this component
   * @since 4.0.0
   */
  @Contract(pure = true)
  KeybindComponent keybind(final String keybind);

  /**
   * Sets the keybind.
   *
   * @param keybind the keybind
   * @return a copy of this component
   * @since 4.9.0
   */
  @Contract(pure = true)
  default KeybindComponent keybind(final KeybindLike keybind) {
    return this.keybind(Objects.requireNonNull(keybind, "keybind").asKeybind());
  }

  @Override
  Builder toBuilder();

  /**
   * Something that can provide a keybind identifier.
   *
   * @since 4.9.0
   */
  interface KeybindLike {
    /**
     * Gets the keybind identifier.
     *
     * @return the keybind identifier
     * @since 4.9.0
     */
    String asKeybind();
  }

  /**
   * A keybind component builder.
   *
   * @since 4.0.0
   */
  sealed interface Builder extends ComponentBuilder<KeybindComponent, Builder> permits KeybindComponentImpl.BuilderImpl {
    /**
     * Sets the keybind.
     *
     * @param keybind the keybind
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    Builder keybind(final String keybind);

    /**
     * Sets the keybind.
     *
     * @param keybind the keybind
     * @return this builder
     * @since 4.9.0
     */
    @Contract(pure = true)
    default Builder keybind(final KeybindLike keybind) {
      return this.keybind(Objects.requireNonNull(keybind, "keybind").asKeybind());
    }
  }
}
