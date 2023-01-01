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
package net.kyori.adventure.text;

import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A component that can display the name of entities found with a given selector.
 *
 * <p>This component consists of:</p>
 * <dl>
 *   <dt>selector</dt>
 *   <dd>a Minecraft selector.(e.g {@code @p}, {@code @a})</dd>
 * </dl>
 *
 * <p>This component is rendered serverside and can therefore receive platform-defined
 * context. See the documentation for your respective
 * platform for more info</p>
 *
 * @since 4.0.0
 */
public interface SelectorComponent extends BuildableComponent<SelectorComponent, SelectorComponent.Builder>, ScopedComponent<SelectorComponent> {
  /**
   * Gets the selector pattern.
   *
   * @return the selector pattern
   * @since 4.0.0
   */
  @NotNull String pattern();

  /**
   * Sets the selector pattern.
   *
   * @param pattern the selector pattern
   * @return a selector component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NotNull SelectorComponent pattern(final @NotNull String pattern);

  /**
   * Gets the separator.
   *
   * @return the separator
   * @since 4.8.0
   */
  @Nullable Component separator();

  /**
   * Sets the separator.
   *
   * @param separator the separator
   * @return the separator
   * @since 4.8.0
   */
  @NotNull SelectorComponent separator(final @Nullable ComponentLike separator);

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("pattern", this.pattern()),
        ExaminableProperty.of("separator", this.separator())
      ),
      BuildableComponent.super.examinableProperties()
    );
  }

  /**
   * A selector component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends ComponentBuilder<SelectorComponent, Builder> {
    /**
     * Sets the selector pattern.
     *
     * @param pattern the selector pattern
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder pattern(final @NotNull String pattern);

    /**
     * Sets the separator.
     *
     * @param separator the separator
     * @return this builder
     * @since 4.8.0
     */
    @Contract("_ -> this")
    @NotNull Builder separator(final @Nullable ComponentLike separator);
  }
}
