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
import java.util.Map;
import java.util.Objects;

final class BlockNbtComponentImpl extends NbtComponentImpl<BlockNbtComponent, BlockNbtComponent.Builder> implements BlockNbtComponent {
  private final String pos;

  protected BlockNbtComponentImpl(final @NonNull List<Component> children, final Style.@Nullable Builder style, final String nbtPath, final boolean interpret, final String posPattern) {
    super(children, style, nbtPath, interpret);
    this.pos = posPattern;
  }

  protected BlockNbtComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPath, final boolean interpret, final String pos) {
    super(children, style, nbtPath, interpret);
    this.pos = pos;
  }

  @Override
  public @NonNull BlockNbtComponent nbtPath(final @NonNull String nbtPath) {
    return new BlockNbtComponentImpl(this.children, this.style, nbtPath, this.interpret, this.pos);
  }

  @Override
  public @NonNull BlockNbtComponent interpret(final boolean interpret) {
    return new BlockNbtComponentImpl(this.children, this.style, this.nbtPath, interpret, this.pos);
  }

  @Override
  public @NonNull String pos() {
    return this.pos;
  }

  @Override
  public @NonNull BlockNbtComponent pos(final @NonNull String pos) {
    return new BlockNbtComponentImpl(this.children, this.style, this.nbtPath, this.interpret, pos);
  }

  @Override
  public @NonNull BlockNbtComponent children(final @NonNull List<Component> children) {
    return new BlockNbtComponentImpl(children, this.style, this.nbtPath, this.interpret, this.pos);
  }

  @Override
  public @NonNull BlockNbtComponent copy() {
    return new BlockNbtComponentImpl(this.children, this.style, this.nbtPath, this.interpret, this.pos);
  }

  @Override
  public @NonNull BlockNbtComponent style(final @NonNull Style style) {
    return new BlockNbtComponentImpl(this.children, style, this.nbtPath, this.interpret, this.pos);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof BlockNbtComponent)) return false;
    if(!super.equals(other)) return false;
    final BlockNbtComponent that = (BlockNbtComponent) other;
    return Objects.equals(this.pos, that.pos());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.pos);
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    super.populateToString(builder);
    builder.put("pos", this.pos);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  public static class BuilderImpl extends NbtComponentImpl.BuilderImpl<BlockNbtComponent, Builder> implements Builder {
    private @Nullable String pos;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull BlockNbtComponentImpl component) {
      super(component);
      this.pos = component.pos();
    }

    @Override
    public @NonNull Builder pos(final @NonNull String pos) {
      this.pos = pos;
      return this;
    }

    @Override
    public @NonNull BlockNbtComponent build() {
      if(this.nbtPath == null) throw new IllegalStateException("nbt path must be set");
      if(this.pos == null) throw new IllegalStateException("pos must be set");
      return new BlockNbtComponentImpl(this.children, this.style, this.nbtPath, this.interpret, this.pos);
    }
  }
}
