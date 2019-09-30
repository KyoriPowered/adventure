/*
 * This file is part of text, licensed under the MIT License.
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
package net.kyori.text;

import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.regex.Pattern;

final class StorageNbtComponentImpl extends NbtComponentImpl<StorageNbtComponent, StorageNbtComponent.Builder> implements StorageNbtComponent {
  private final String selectorPattern;

  protected StorageNbtComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPathPattern, final boolean interpret, final String selectorPattern) {
    super(children, style, nbtPathPattern, interpret);
    this.selectorPattern = selectorPattern;
  }

  @Override
  public @NonNull StorageNbtComponent nbtPath(final @NonNull String nbtPath) {
    return new StorageNbtComponentImpl(this.children, this.style, nbtPath, this.interpret, this.selectorPattern);
  }

  @Override
  public @NonNull StorageNbtComponent interpret(final boolean interpret) {
    return new StorageNbtComponentImpl(this.children, this.style, this.nbtPath, interpret, this.selectorPattern);
  }

  @Override
  public @NonNull String storage() {
    return this.selectorPattern;
  }

  @Override
  public @NonNull StorageNbtComponent storage(final @NonNull String storage) {
    return new StorageNbtComponentImpl(this.children, this.style, this.nbtPath, this.interpret, storage);
  }

  @Override
  public @NonNull StorageNbtComponent children(final @NonNull List<Component> children) {
    return new StorageNbtComponentImpl(children, this.style, this.nbtPath, this.interpret, this.selectorPattern);
  }

  @Override
  public @NonNull StorageNbtComponent style(final @NonNull Style style) {
    return new StorageNbtComponentImpl(this.children, style, this.nbtPath, this.interpret, this.selectorPattern);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static class BuilderImpl extends NbtComponentImpl.BuilderImpl<StorageNbtComponent, Builder> implements Builder {
    static final Pattern ID_PATTERN = Pattern.compile("^([a-z0-9_.-]+:)?[a-z0-9/._-]+$");
    private @MonotonicNonNull String storage;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull StorageNbtComponent component) {
      super(component);
      this.storage = component.storage();
    }

    @Override
    public @NonNull Builder storage(final @NonNull String storage) {
      if(!ID_PATTERN.matcher(storage).find()) throw new IllegalStateException("Invalid storage id \"" + this.storage + "\" supplied");
      this.storage = storage;
      return this;
    }

    @Override
    public @NonNull StorageNbtComponent build() {
      if(this.nbtPath == null) throw new IllegalStateException("nbt path must be set");
      if(this.storage == null) throw new IllegalStateException("storage must be set");
      return new StorageNbtComponentImpl(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.storage);
    }
  }
}
