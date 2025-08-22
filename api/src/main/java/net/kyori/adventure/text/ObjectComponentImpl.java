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
  private final Contents contents;

  private ObjectComponentImpl(final @NotNull List<Component> children, final @NotNull Style style, final @NotNull Contents contents) {
    super(children, style);
    this.contents = contents;
  }

  @Override
  public @NotNull Contents contents() {
    return this.contents;
  }

  @Override
  public @NotNull ObjectComponent contents(final @NotNull Contents contents) {
    return create(this.children, this.style, contents);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof ObjectComponent)) return false;
    if (!super.equals(other)) return false;
    final ObjectComponentImpl that = (ObjectComponentImpl) other;
    return Objects.equals(this.contents, that.contents());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.contents.hashCode();
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

  static @NotNull ObjectComponentImpl create(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final @NotNull Contents contents) {
    return new ObjectComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      requireNonNull(contents, "contents")
    );
  }

  @Override
  public @NotNull ObjectComponent children(final @NotNull List<? extends ComponentLike> children) {
    return create(children, this.style, this.contents);
  }

  @Override
  public @NotNull ObjectComponent style(final @NotNull Style style) {
    return create(this.children, style, this.contents);
  }

  static final class SpriteContentsImpl implements SpriteContents {
    private final @Nullable Key atlas;
    private final Key sprite;

    SpriteContentsImpl(final @Nullable Key atlas, final @NotNull Key sprite) {
      this.atlas = atlas;
      this.sprite = requireNonNull(sprite, "sprite");
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
    public boolean equals(final @Nullable Object other) {
      if (this == other) return true;
      if (!(other instanceof SpriteContents)) return false;
      final SpriteContentsImpl that = (SpriteContentsImpl) other;
      return Objects.equals(this.atlas, that.atlas())
        && Objects.equals(this.sprite, that.sprite());
    }

    @Override
    public int hashCode() {
      int result = Objects.hashCode(this.atlas);
      result = (31 * result) + this.sprite.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  static final class BuilderImpl extends AbstractComponentBuilder<ObjectComponent, ObjectComponent.Builder> implements ObjectComponent.Builder {
    private Contents contents;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull ObjectComponent component) {
      super(component);
      this.contents = component.contents();
    }

    @Override
    public @NotNull Builder contents(final @NotNull Contents contents) {
      this.contents = requireNonNull(contents, "contents");
      return this;
    }

    @Override
    public @NotNull ObjectComponent build() {
      if (this.contents == null) throw new IllegalStateException("contents must be set");
      return create(this.children, this.buildStyle(), this.contents);
    }
  }
}
