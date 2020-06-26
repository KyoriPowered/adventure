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
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class StorageNBTComponentImpl extends NBTComponentImpl<StorageNBTComponent, StorageNBTComponent.Builder> implements StorageNBTComponent {
  private final Key storage;

  protected StorageNBTComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPath, final boolean interpret, final Key storage) {
    super(children, style, nbtPath, interpret);
    this.storage = storage;
  }

  @Override
  public @NonNull StorageNBTComponent nbtPath(final @NonNull String nbtPath) {
    if(Objects.equals(this.nbtPath, nbtPath)) return this;
    return new StorageNBTComponentImpl(this.children, this.style, nbtPath, this.interpret, this.storage);
  }

  @Override
  public @NonNull StorageNBTComponent interpret(final boolean interpret) {
    if(this.interpret == interpret) return this;
    return new StorageNBTComponentImpl(this.children, this.style, this.nbtPath, interpret, this.storage);
  }

  @Override
  public @NonNull Key storage() {
    return this.storage;
  }

  @Override
  public @NonNull StorageNBTComponent storage(final @NonNull Key storage) {
    if(Objects.equals(this.storage, storage)) return this;
    return new StorageNBTComponentImpl(this.children, this.style, this.nbtPath, this.interpret, storage);
  }

  @Override
  public @NonNull StorageNBTComponent children(final @NonNull List<Component> children) {
    return new StorageNBTComponentImpl(children, this.style, this.nbtPath, this.interpret, this.storage);
  }

  @Override
  public @NonNull StorageNBTComponent style(final @NonNull Style style) {
    if(Objects.equals(this.style, style)) return this;
    return new StorageNBTComponentImpl(this.children, style, this.nbtPath, this.interpret, this.storage);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof StorageNBTComponent)) return false;
    if(!super.equals(other)) return false;
    final StorageNBTComponentImpl that = (StorageNBTComponentImpl) other;
    return Objects.equals(this.storage, that.storage());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.storage.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("storage", this.storage)
      ),
      super.examinableProperties()
    );
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static class BuilderImpl extends NBTComponentImpl.BuilderImpl<StorageNBTComponent, Builder> implements Builder {
    private @MonotonicNonNull Key storage;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull StorageNBTComponent component) {
      super(component);
      this.storage = component.storage();
    }

    @Override
    public @NonNull Builder storage(final @NonNull Key storage) {
      this.storage = storage;
      return this;
    }

    @Override
    public @NonNull StorageNBTComponent build() {
      if(this.nbtPath == null) throw new IllegalStateException("nbt path must be set");
      if(this.storage == null) throw new IllegalStateException("storage must be set");
      return new StorageNBTComponentImpl(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.storage);
    }
  }
}
