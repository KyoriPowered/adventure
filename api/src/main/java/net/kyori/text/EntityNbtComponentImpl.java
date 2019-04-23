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

import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

final class EntityNbtComponentImpl extends NbtComponentImpl<EntityNbtComponent, EntityNbtComponent.Builder> implements EntityNbtComponent {
  private final String selectorPattern;

  protected EntityNbtComponentImpl(final @NonNull List<Component> children, final Style.@Nullable Builder style, final String nbtPathPattern, final boolean interpret, final String selectorPattern) {
    super(children, style, nbtPathPattern, interpret);
    this.selectorPattern = selectorPattern;
  }

  protected EntityNbtComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPathPattern, final boolean interpret, final String selectorPattern) {
    super(children, style, nbtPathPattern, interpret);
    this.selectorPattern = selectorPattern;
  }

  @Override
  public @NonNull EntityNbtComponent nbtPath(final @NonNull String nbtPath) {
    return new EntityNbtComponentImpl(this.children, this.style, nbtPath, this.interpret, this.selectorPattern);
  }

  @Override
  public @NonNull EntityNbtComponent interpret(final boolean interpret) {
    return new EntityNbtComponentImpl(this.children, this.style, this.nbtPath, interpret, this.selectorPattern);
  }

  @Override
  public @NonNull String selector() {
    return this.selectorPattern;
  }

  @Override
  public @NonNull EntityNbtComponent selector(final @NonNull String selector) {
    return new EntityNbtComponentImpl(this.children, this.style, this.nbtPath, this.interpret, selector);
  }

  @Override
  public @NonNull EntityNbtComponent children(final @NonNull List<Component> children) {
    return new EntityNbtComponentImpl(children, this.style, this.nbtPath, this.interpret, this.selectorPattern);
  }

  @Override
  public @NonNull EntityNbtComponent copy() {
    return new EntityNbtComponentImpl(this.children, this.style, this.nbtPath, this.interpret, this.selectorPattern);
  }

  @Override
  public @NonNull EntityNbtComponent style(final @NonNull Style style) {
    return new EntityNbtComponentImpl(this.children, style, this.nbtPath, this.interpret, this.selectorPattern);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  public static class BuilderImpl extends NbtComponentImpl.BuilderImpl<EntityNbtComponent, Builder> implements Builder {
    private @Nullable String selector;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull EntityNbtComponentImpl component) {
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
      return new EntityNbtComponentImpl(this.children, this.style, this.nbtPath, this.interpret, this.selector);
    }
  }
}
