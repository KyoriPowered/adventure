/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
package net.kyori.adventure.text.minimessage.placeholder;

import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The result of a resolved placeholder.
 *
 * @param <T> the type of the replacement
 * @since 4.10.0
 */
@ApiStatus.NonExtendable
public interface Replacement<T> extends Examinable {

  /**
   * Creates a replacement that inserts a MiniMessage string. The inserted string will impact
   * the rest of the parse process.
   *
   * @param miniMessage the string
   * @return the replacement
   * @since 4.10.0
   */
  static @NotNull Replacement<String> miniMessage(final @NotNull String miniMessage) {
    return new ReplacementImpl<>(Objects.requireNonNull(miniMessage, "miniMessage"));
  }

  /**
   * Creates a replacement that inserts a component.
   *
   * @param component the component
   * @return the replacement
   * @since 4.10.0
   */
  static @NotNull Replacement<Component> component(final @NotNull ComponentLike component) {
    return new ReplacementImpl<>(
      Objects.requireNonNull(
        Objects.requireNonNull(component, "component").asComponent(),
        "component cannot resolve to null"
      )
    );
  }

  /**
   * The value of the replacement.
   *
   * @return the value
   * @since 4.10.0
   */
  @NotNull T value();
}
