/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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
import java.util.stream.Stream;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class EntityNBTComponentImpl extends NBTComponentImpl<EntityNBTComponent, EntityNBTComponent.Builder> implements EntityNBTComponent {
  private final String selector;

  EntityNBTComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPath, final boolean interpret, final String selector) {
    super(children, style, nbtPath, interpret);
    this.selector = selector;
  }

  @Override
  public @NonNull EntityNBTComponent nbtPath(final @NonNull String nbtPath) {
    if(Objects.equals(this.nbtPath, nbtPath)) return this;
    return new EntityNBTComponentImpl(this.children, this.style, nbtPath, this.interpret, this.selector);
  }

  @Override
  public @NonNull EntityNBTComponent interpret(final boolean interpret) {
    if(this.interpret == interpret) return this;
    return new EntityNBTComponentImpl(this.children, this.style, this.nbtPath, interpret, this.selector);
  }

  @Override
  public @NonNull String selector() {
    return this.selector;
  }

  @Override
  public @NonNull EntityNBTComponent selector(final @NonNull String selector) {
    if(Objects.equals(this.selector, selector)) return this;
    return new EntityNBTComponentImpl(this.children, this.style, this.nbtPath, this.interpret, selector);
  }

  @Override
  public @NonNull EntityNBTComponent children(final @NonNull List<Component> children) {
    return new EntityNBTComponentImpl(children, this.style, this.nbtPath, this.interpret, this.selector);
  }

  @Override
  public @NonNull EntityNBTComponent style(final @NonNull Style style) {
    if(Objects.equals(this.style, style)) return this;
    return new EntityNBTComponentImpl(this.children, style, this.nbtPath, this.interpret, this.selector);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof EntityNBTComponent)) return false;
    if(!super.equals(other)) return false;
    final EntityNBTComponentImpl that = (EntityNBTComponentImpl) other;
    return Objects.equals(this.selector, that.selector());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.selector.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("selector", this.selector)
      ),
      super.examinableProperties()
    );
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends NBTComponentImpl.BuilderImpl<EntityNBTComponent, Builder> implements Builder {
    private @Nullable String selector;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull EntityNBTComponent component) {
      super(component);
      this.selector = component.selector();
    }

    @Override
    public @NonNull Builder selector(final @NonNull String selector) {
      this.selector = selector;
      return this;
    }

    @Override
    public @NonNull EntityNBTComponent build() {
      if(this.nbtPath == null) throw new IllegalStateException("nbt path must be set");
      if(this.selector == null) throw new IllegalStateException("selector must be set");
      return new EntityNBTComponentImpl(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.selector);
    }
  }
}
