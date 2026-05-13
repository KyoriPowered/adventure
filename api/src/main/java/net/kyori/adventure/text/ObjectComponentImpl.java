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

import java.util.List;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.object.ObjectContents;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record ObjectComponentImpl(List<Component> children, Style style, ObjectContents contents, @Nullable Component fallback) implements ObjectComponent {

  static ObjectComponentImpl create(final List<? extends ComponentLike> children, final Style style, final ObjectContents objectContents, final @Nullable ComponentLike fallback) {
    return new ObjectComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      requireNonNull(objectContents, "contents"),
      ComponentLike.unbox(fallback)
    );
  }

  @Override
  public ObjectComponent contents(final ObjectContents contents) {
    return create(this.children, this.style, contents, this.fallback);
  }

  @Override
  public ObjectComponent fallback(final @Nullable ComponentLike fallback) {
    return create(this.children, this.style, this.contents, fallback);
  }

  @Override
  public Builder toBuilder() {
    return new BuilderImpl(this);
  }

  @Override
  public ObjectComponent children(final List<? extends ComponentLike> children) {
    return create(children, this.style, this.contents, this.fallback);
  }

  @Override
  public ObjectComponent style(final Style style) {
    return create(this.children, style, this.contents, this.fallback);
  }

  @Override
  public ObjectContents asObjectContents() {
    return this.contents;
  }

  static final class BuilderImpl extends AbstractComponentBuilder<ObjectComponent, Builder> implements Builder {
    private @Nullable ObjectContents objectContents; // Not nullable for built type, it errors in builder.
    private @Nullable ComponentLike fallback;

    BuilderImpl() {
    }

    BuilderImpl(final ObjectComponent component) {
      super(component);
      this.objectContents = component.contents();
      this.fallback = component.fallback();
    }

    @Override
    public Builder contents(final ObjectContents objectContents) {
      this.objectContents = requireNonNull(objectContents, "contents");
      return this;
    }

    @Override
    public Builder fallback(final @Nullable ComponentLike fallback) {
      this.fallback = fallback;
      return this;
    }

    @Override
    public ObjectComponent build() {
      if (this.objectContents == null) throw new IllegalStateException("contents must be set");
      return create(this.children, this.buildStyle(), this.objectContents, this.fallback);
    }
  }
}
