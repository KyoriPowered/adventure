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
import java.util.Objects;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class ObjectComponentImpl extends AbstractComponent implements ObjectComponent {
  private final @Nullable Key atlas;
  private final Key sprite;

  private ObjectComponentImpl(final @NotNull List<Component> children, final @NotNull Style style, final @Nullable Key atlas, final @NotNull Key sprite) {
    super(children, style);
    this.atlas = atlas;
    this.sprite = sprite;
  }

  @Override
  public @Nullable Key atlas() {
    return this.atlas;
  }

  @Override
  public @NotNull Key sprite() {
    return this.sprite;
  }

  @Override
  public @NotNull ObjectComponent atlas(final @Nullable Key atlas) {
    return create(this.children, this.style, atlas, this.sprite);
  }

  @Override
  public @NotNull ObjectComponent sprite(final @NotNull Key sprite) {
    return create(this.children, this.style, this.atlas, requireNonNull(sprite, "sprite"));
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof ObjectComponent)) return false;
    if (!super.equals(other)) return false;
    final ObjectComponentImpl that = (ObjectComponentImpl) other;
    return Objects.equals(this.atlas, that.atlas())
      && Objects.equals(this.sprite, that.sprite());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + Objects.hashCode(this.atlas);
    result = (31 * result) + this.sprite.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static @NotNull ObjectComponentImpl create(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final @Nullable Key atlas, final @NotNull Key sprite) {
    return new ObjectComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      atlas,
      requireNonNull(sprite, "sprite")
    );
  }

  @Override
  public @NotNull ObjectComponent children(final @NotNull List<? extends ComponentLike> children) {
    return create(children, this.style, this.atlas, this.sprite);
  }

  @Override
  public @NotNull ObjectComponent style(final @NotNull Style style) {
    return create(this.children, style, this.atlas, this.sprite);
  }

  static class BuilderImpl extends AbstractComponentBuilder<ObjectComponent, ObjectComponent.Builder> implements ObjectComponent.Builder {
    private Key atlas;
    private Key sprite;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull ObjectComponent component) {
      super(component);
      this.atlas = component.atlas();
      this.sprite = component.sprite();
    }

    @Override
    public @NotNull Builder atlas(final @Nullable Key atlas) {
      this.atlas = atlas;
      return this;
    }

    @Override
    public @NotNull Builder sprite(final @NotNull Key sprite) {
      this.sprite = requireNonNull(sprite, "sprite");
      return this;
    }

    @Override
    public @NotNull ObjectComponent build() {
      if (this.sprite == null) throw new IllegalStateException("sprite id must be set");
      return create(this.children, this.buildStyle(), this.atlas, this.sprite);
    }
  }
}
