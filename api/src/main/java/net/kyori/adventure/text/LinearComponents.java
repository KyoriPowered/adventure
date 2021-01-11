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

import net.kyori.adventure.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A utility class that allows {@link Component components} to be created where {@link Style styles} can be specified inline.
 *
 * @since 4.0.0
 */
public final class LinearComponents {
  private LinearComponents() {
  }

  /**
   * Styles apply to all components after them until a conflicting style is discovered
   *   <pre>
   *     Component message = LinearComponents.linear(NamedTextColor.RED, translatable("welcome.message"), TextDecoration.BOLD, text(" SERVER));
   *   </pre>
   * In this example all the text is red, but only the last word is bold.
   *   <pre>
   *     Component message = LinearComponents.linear(NamedTextColor.GREEN, text("I am green. "), NamedTextColor.GRAY, text("I am gray."));
   *   </pre>
   * In this example, the first text is green and the second is gray.
   *
   * @param applicables the things used to make the component
   * @return a component
   * @since 4.0.0
   * @deprecated for removal since 4.4.0. Use {@link Component#compose(ComponentBuilderApplicable...)} instead
   */
  @Deprecated
  public static @NonNull Component linear(final @NonNull ComponentBuilderApplicable@NonNull... applicables) {
    return Component.compose(applicables);
  }

}
