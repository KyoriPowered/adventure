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

import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

final class VirtualComponentImpl extends TextComponentImpl implements VirtualComponent {
  static <C> VirtualComponent createVirtual(final @NotNull Class<C> context, final @NotNull VirtualComponentRenderer<?> renderer) {
    return createVirtual(context, renderer, Collections.emptyList(), Style.empty());
  }

  static <C> VirtualComponent createVirtual(final @NotNull Class<C> context, final @NotNull VirtualComponentRenderer<?> renderer, final List<? extends ComponentLike> children, final Style style) {
    final List<Component> filteredChildren = ComponentLike.asComponents(children, IS_NOT_EMPTY);

    return new VirtualComponentImpl(filteredChildren, style, "", context, renderer);
  }

  private final Class<?> context;
  private final VirtualComponentRenderer<?> renderer;

  private VirtualComponentImpl(final @NotNull List<Component> children, final @NotNull Style style, final @NotNull String content, final @NotNull Class<?> context, final @NotNull VirtualComponentRenderer<?> renderer) {
    super(children, style, content);
    this.context = context;
    this.renderer = renderer;
  }

  @Override
  VirtualComponent create0(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final @NotNull String content) {
    return new VirtualComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), style, content, this.context, this.renderer);
  }

  @Override
  public @NotNull Class<?> context() {
    return this.context;
  }

  @Override
  public @NotNull VirtualComponentRenderer<?> renderer() {
    return this.renderer;
  }

  @Override
  public @NotNull String content() {
    return this.renderer.fallbackString();
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends TextComponentImpl.BuilderImpl {
    private final Class<?> context;
    private final VirtualComponentRenderer<?> renderer;

    BuilderImpl(final VirtualComponent other) {
      super(other);
      this.context = other.context();
      this.renderer = other.renderer();
    }

    @Override
    public @NotNull TextComponent build() {
      return createVirtual(this.context, this.renderer, this.children, this.buildStyle());
    }
  }
}
