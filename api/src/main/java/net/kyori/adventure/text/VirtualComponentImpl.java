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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.Nullable;

final class VirtualComponentImpl<C> extends TextComponentImpl implements VirtualComponent {
  static <C> VirtualComponent createVirtual(final Class<C> contextType, final VirtualComponentRenderer<C> renderer) {
    return createVirtual(contextType, renderer, Collections.emptyList(), Style.empty());
  }

  static <C> VirtualComponent createVirtual(final Class<C> contextType, final VirtualComponentRenderer<C> renderer, final List<? extends ComponentLike> children, final Style style) {
    final List<Component> filteredChildren = ComponentLike.asComponents(children, IS_NOT_EMPTY);

    return new VirtualComponentImpl<>(filteredChildren, style, "", contextType, renderer);
  }

  private final Class<C> contextType;
  private final VirtualComponentRenderer<C> renderer;

  private VirtualComponentImpl(final List<Component> children, final Style style, final String content, final Class<C> contextType, final VirtualComponentRenderer<C> renderer) {
    super(children, style, content);
    this.contextType = contextType;
    this.renderer = renderer;
  }

  @Override
  VirtualComponent create0(final List<? extends ComponentLike> children, final Style style, final String content) {
    return new VirtualComponentImpl<>(ComponentLike.asComponents(children, IS_NOT_EMPTY), style, content, this.contextType, this.renderer);
  }

  @Override
  public Class<C> contextType() {
    return this.contextType;
  }

  @Override
  public VirtualComponentRenderer<C> renderer() {
    return this.renderer;
  }

  @Override
  public String content() {
    return this.renderer.fallbackString();
  }

  @Override
  public Builder toBuilder() {
    return new BuilderImpl<>(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof final VirtualComponentImpl<?> that)) return false;
    if (!super.equals(other)) return false;
    return Objects.equals(this.contextType, that.contextType) && Objects.equals(this.renderer, that.renderer);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.contextType.hashCode();
    result = (31 * result) + this.renderer.hashCode();
    return result;
  }

  static final class BuilderImpl<C> extends TextComponentImpl.BuilderImpl {
    private final Class<C> contextType;
    private final VirtualComponentRenderer<C> renderer;

    BuilderImpl(final VirtualComponentImpl<C> other) {
      super(other);
      this.contextType = other.contextType();
      this.renderer = other.renderer();
    }

    @Override
    public TextComponent build() {
      return createVirtual(this.contextType, this.renderer, this.children, this.buildStyle());
    }
  }
}
