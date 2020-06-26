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
import net.kyori.adventure.util.ShadyPines;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

final class BlockNBTComponentImpl extends NBTComponentImpl<BlockNBTComponent, BlockNBTComponent.Builder> implements BlockNBTComponent {
  private final Pos pos;

  BlockNBTComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final String nbtPath, final boolean interpret, final @NonNull Pos pos) {
    super(children, style, nbtPath, interpret);
    this.pos = pos;
  }

  @Override
  public @NonNull BlockNBTComponent nbtPath(final @NonNull String nbtPath) {
    if(Objects.equals(this.nbtPath, nbtPath)) return this;
    return new BlockNBTComponentImpl(this.children, this.style, nbtPath, this.interpret, this.pos);
  }

  @Override
  public @NonNull BlockNBTComponent interpret(final boolean interpret) {
    if(this.interpret == interpret) return this;
    return new BlockNBTComponentImpl(this.children, this.style, this.nbtPath, interpret, this.pos);
  }

  @Override
  public @NonNull Pos pos() {
    return this.pos;
  }

  @Override
  public @NonNull BlockNBTComponent pos(final @NonNull Pos pos) {
    return new BlockNBTComponentImpl(this.children, this.style, this.nbtPath, this.interpret, pos);
  }

  @Override
  public @NonNull BlockNBTComponent children(final @NonNull List<Component> children) {
    return new BlockNBTComponentImpl(children, this.style, this.nbtPath, this.interpret, this.pos);
  }

  @Override
  public @NonNull BlockNBTComponent style(final @NonNull Style style) {
    if(Objects.equals(this.style, style)) return this;
    return new BlockNBTComponentImpl(this.children, style, this.nbtPath, this.interpret, this.pos);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof BlockNBTComponent)) return false;
    if(!super.equals(other)) return false;
    final BlockNBTComponent that = (BlockNBTComponent) other;
    return Objects.equals(this.pos, that.pos());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.pos.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("pos", this.pos)
      ),
      super.examinableProperties()
    );
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends NBTComponentImpl.BuilderImpl<BlockNBTComponent, Builder> implements Builder {
    private @Nullable Pos pos;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull BlockNBTComponent component) {
      super(component);
      this.pos = component.pos();
    }

    @Override
    public @NonNull Builder pos(final @NonNull Pos pos) {
      this.pos = pos;
      return this;
    }

    @Override
    public @NonNull BlockNBTComponent build() {
      if(this.nbtPath == null) throw new IllegalStateException("nbt path must be set");
      if(this.pos == null) throw new IllegalStateException("pos must be set");
      return new BlockNBTComponentImpl(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.pos);
    }
  }

  static final class LocalPosImpl implements LocalPos {
    private final double left;
    private final double up;
    private final double forwards;

    LocalPosImpl(final double left, final double up, final double forwards) {
      this.left = left;
      this.up = up;
      this.forwards = forwards;
    }

    @Override
    public double left() {
      return this.left;
    }

    @Override
    public double up() {
      return this.up;
    }

    @Override
    public double forwards() {
      return this.forwards;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if(this == other) return true;
      if(!(other instanceof LocalPos)) return false;
      final LocalPos that = (LocalPos) other;
      return ShadyPines.equals(that.left(), this.left())
        && ShadyPines.equals(that.up(), this.up())
        && ShadyPines.equals(that.forwards(), this.forwards());
    }

    @Override
    public int hashCode() {
      int result = Double.hashCode(this.left);
      result = (31 * result) + Double.hashCode(this.up);
      result = (31 * result) + Double.hashCode(this.forwards);
      return result;
    }

    @Override
    public String toString() {
      return String.format("^%f ^%f ^%f", this.left, this.up, this.forwards);
    }
  }

  static final class WorldPosImpl implements WorldPos {
    private final Coordinate x;
    private final Coordinate y;
    private final Coordinate z;

    WorldPosImpl(final Coordinate x, final Coordinate y, final Coordinate z) {
      this.x = requireNonNull(x, "x");
      this.y = requireNonNull(y, "y");
      this.z = requireNonNull(z, "z");
    }

    @Override
    public @NonNull Coordinate x() {
      return this.x;
    }

    @Override
    public @NonNull Coordinate y() {
      return this.y;
    }

    @Override
    public @NonNull Coordinate z() {
      return this.z;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if(this == other) return true;
      if(!(other instanceof WorldPos)) return false;
      final WorldPos that = (WorldPos) other;
      return this.x.equals(that.x())
        && this.y.equals(that.y())
        && this.z.equals(that.z());
    }

    @Override
    public int hashCode() {
      int result = this.x.hashCode();
      result = (31 * result) + this.y.hashCode();
      result = (31 * result) + this.z.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return this.x.toString() + ' ' + this.y.toString() + ' ' + this.z.toString();
    }

    static final class CoordinateImpl implements Coordinate {
      private final int value;
      private final Type type;

      CoordinateImpl(final int value, final @NonNull Type type) {
        this.value = value;
        this.type = requireNonNull(type, "type");
      }

      @Override
      public int value() {
        return this.value;
      }

      @Override
      public @NonNull Type type() {
        return this.type;
      }

      @Override
      public boolean equals(final @Nullable Object other) {
        if(this == other) return true;
        if(!(other instanceof Coordinate)) return false;
        final Coordinate that = (Coordinate) other;
        return this.value() == that.value()
          && this.type() == that.type();
      }

      @Override
      public int hashCode() {
        int result = this.value;
        result = (31 * result) + this.type.hashCode();
        return result;
      }

      @Override
      public String toString() {
        return (this.type == Type.RELATIVE ? "~" : "") + this.value;
      }
    }
  }
}
