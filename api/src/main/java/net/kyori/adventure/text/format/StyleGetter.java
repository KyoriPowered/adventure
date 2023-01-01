/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.text.format;

import java.util.EnumMap;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Reads style properties from an object.
 *
 * @see Style
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public interface StyleGetter {
  /**
   * Gets the font.
   *
   * @return the font
   * @since 4.10.0
   * @sinceMinecraft 1.16
   */
  @Nullable Key font();

  /**
   * Gets the color.
   *
   * @return the color
   * @since 4.10.0
   */
  @Nullable TextColor color();

  /**
   * Tests if this stylable has a decoration.
   *
   * @param decoration the decoration
   * @return {@code true} if this stylable has the decoration, {@code false} if this
   *     stylable does not have the decoration
   * @since 4.10.0
   */
  default boolean hasDecoration(final @NotNull TextDecoration decoration) {
    return this.decoration(decoration) == TextDecoration.State.TRUE;
  }

  /**
   * Gets the state of a decoration on this stylable.
   *
   * @param decoration the decoration
   * @return {@link TextDecoration.State#TRUE} if this stylable has the decoration,
   *     {@link TextDecoration.State#FALSE} if this stylable does not have the decoration,
   *     and {@link TextDecoration.State#NOT_SET} if not set
   * @since 4.10.0
   */
  TextDecoration.@NotNull State decoration(final @NotNull TextDecoration decoration);

  /**
   * Gets a map of decorations this stylable has.
   *
   * @return a set of decorations this stylable has
   * @since 4.10.0
   */
  @SuppressWarnings("Duplicates")
  default @Unmodifiable @NotNull Map<TextDecoration, TextDecoration.State> decorations() {
    final Map<TextDecoration, TextDecoration.State> decorations = new EnumMap<>(TextDecoration.class);
    for (int i = 0, length = DecorationMap.DECORATIONS.length; i < length; i++) {
      final TextDecoration decoration = DecorationMap.DECORATIONS[i];
      final TextDecoration.State value = this.decoration(decoration);
      decorations.put(decoration, value);
    }
    return decorations;
  }

  /**
   * Gets the click event.
   *
   * @return the click event
   * @since 4.10.0
   */
  @Nullable ClickEvent clickEvent();

  /**
   * Gets the hover event.
   *
   * @return the hover event
   * @since 4.10.0
   */
  @Nullable HoverEvent<?> hoverEvent();

  /**
   * Gets the string to be inserted when this stylable is shift-clicked.
   *
   * @return the insertion string
   * @since 4.10.0
   */
  @Nullable String insertion();
}
