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
package net.kyori.adventure.text.format;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A container around a {@link Component}'s {@link Component#font() font}.
 *
 * @since 4.10.0
 */
public interface Font extends Examinable, StyleBuilderApplicable {
  /**
   * Creates a {@code Font}.
   *
   * @param font the font
   * @return a {@code Font}
   * @since 4.10.0
   */
  static @NotNull Font font(final @NotNull Key font) {
    Objects.requireNonNull(font, "font");
    return new FontImpl(font);
  }

  /**
   * Fetches a {@link Key} from a {@code Font}.
   *
   * @param font the font
   * @return a font key, or {@code null}
   * @since 4.10.0
   */
  static @Nullable Key unbox(final @Nullable Font font) {
    return font != null ? font.font() : null;
  }

  /**
   * Gets the font.
   *
   * @return the font
   * @since 4.10.0
   */
  @NotNull Key font();

  @Override
  default void styleApply(final Style.@NotNull Builder style) {
    style.font(this.font());
  }

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("font", this.font()));
  }
}
