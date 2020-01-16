/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class EntityNbtComponentImpl extends NbtComponentImpl<EntityNbtComponent, EntityNbtComponent.Builder> implements EntityNbtComponent {
  private final String selector;

  protected EntityNbtComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPath, final boolean interpret, final String selector) {
    super(children, style, nbtPath, interpret);
    this.selector = selector;
  }

  @Override
  public @NonNull EntityNbtComponent nbtPath(final @NonNull String nbtPath) {
    if(Objects.equals(this.nbtPath, nbtPath)) return this;
    return new EntityNbtComponentImpl(this.children, this.style, nbtPath, this.interpret, this.selector);
  }

  @Override
  public @NonNull EntityNbtComponent interpret(final boolean interpret) {
    if(this.interpret == interpret) return this;
    return new EntityNbtComponentImpl(this.children, this.style, this.nbtPath, interpret, this.selector);
  }

  @Override
  public @NonNull String selector() {
    return this.selector;
  }

  @Override
  public @NonNull EntityNbtComponent selector(final @NonNull String selector) {
    if(Objects.equals(this.selector, selector)) return this;
    return new EntityNbtComponentImpl(this.children, this.style, this.nbtPath, this.interpret, selector);
  }

  @Override
  public @NonNull EntityNbtComponent children(final @NonNull List<Component> children) {
    return new EntityNbtComponentImpl(children, this.style, this.nbtPath, this.interpret, this.selector);
  }

  @Override
  public @NonNull EntityNbtComponent style(final @NonNull Style style) {
    if(Objects.equals(this.style, style)) return this;
    return new EntityNbtComponentImpl(this.children, style, this.nbtPath, this.interpret, this.selector);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof EntityNbtComponent)) return false;
    if(!super.equals(other)) return false;
    final EntityNbtComponentImpl that = (EntityNbtComponentImpl) other;
    return Objects.equals(this.selector, that.selector());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.selector.hashCode();
    return result;
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    super.populateToString(builder);
    builder.put("selector", this.selector);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static class BuilderImpl extends NbtComponentImpl.BuilderImpl<EntityNbtComponent, Builder> implements Builder {
    private @Nullable String selector;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull EntityNbtComponent component) {
      super(component);
      this.selector = component.selector();
    }

    @Override
    public @NonNull Builder selector(final @NonNull String selector) {
      this.selector = selector;
      return this;
    }

    @Override
    public @NonNull EntityNbtComponent build() {
      if(this.nbtPath == null) throw new IllegalStateException("nbt path must be set");
      if(this.selector == null) throw new IllegalStateException("selector must be set");
      return new EntityNbtComponentImpl(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.selector);
    }
  }
}
