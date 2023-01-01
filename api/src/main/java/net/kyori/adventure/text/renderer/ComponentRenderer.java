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
package net.kyori.adventure.text.renderer;

import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * A component renderer.
 *
 * @param <C> the context type
 * @since 4.0.0
 */
public interface ComponentRenderer<C> {
  /**
   * Renders a component.
   *
   * @param component the component
   * @param context the context
   * @return the rendered component
   * @since 4.0.0
   */
  @NotNull Component render(final @NotNull Component component, final @NotNull C context);

  /**
   * Return a {@link ComponentRenderer} that takes a different context type.
   *
   * @param transformer context type transformer
   * @param <T> transformation function
   * @return mapping renderer
   * @since 4.0.0
   */
  default <T> ComponentRenderer<T> mapContext(final Function<T, C> transformer) {
    return (component, ctx) -> this.render(component, transformer.apply(ctx));
  }
}
