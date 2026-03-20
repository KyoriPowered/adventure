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
package net.kyori.adventure.text.minimessage;

import java.util.Map;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A context which is used by serialization and deserialization of MiniMessage strings.
 *
 * @since 4.26.0
 */
@ApiStatus.NonExtendable
public interface SerializationContext {
  /**
   * Get a map of all named colors and their {@link TextColor} instances.
   *
   * @return map of all named colors and their {@link TextColor} instances.
   * @since 4.26.0
   */
  @Unmodifiable
  @NotNull Map<String, TextColor> namedColors();

  /**
   * Get a map of all named color aliases and the named colors they point to.
   *
   * @return map of all named color aliases and the named colors they point to.
   * @since 4.26.0
   */
  @Unmodifiable
  @NotNull Map<String, String> namedColorAliases();

  /**
   * Get the {@link TextColor} of a registered color name. This method also resolves aliases.
   *
   * @param name the name of the color to retrieve
   * @return the retrieved color, or null if none found
   * @since 4.26.0
   */
  default @Nullable TextColor namedColor(final @NotNull String name) {
    final TextColor color = this.namedColors().get(name);
    if (color != null) {
      return color;
    }

    final String alias = this.namedColorAliases().get(name);
    if (alias != null) {
      return this.namedColor(alias);
    }

    return null;
  }
}
