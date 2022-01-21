/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record BlockNBTComponentImpl(
  @NotNull List<Component> children,
  @NotNull Style style,
  @NotNull String nbtPath,
  boolean interpret,
  @Nullable Component separator,
  @NotNull Pos pos
) implements BlockNBTComponent {
  static @NotNull BlockNBTComponent create(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final String nbtPath, final boolean interpret, final @Nullable ComponentLike separator, final @NotNull Pos pos) {
    return new BlockNBTComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      requireNonNull(nbtPath, "nbtPath"),
      interpret,
      ComponentLike.unbox(separator),
      requireNonNull(pos, "pos")
    );
  }

  @Deprecated
  BlockNBTComponentImpl {
  }

  @Override
  public @NotNull BlockNBTComponent nbtPath(final @NotNull String nbtPath) {
    if (Objects.equals(this.nbtPath, nbtPath)) return this;
    return create(this.children, this.style, requireNonNull(nbtPath, "nbtPath"), this.interpret, this.separator, this.pos);
  }

  @Override
  public @NotNull BlockNBTComponent interpret(final boolean interpret) {
    if (this.interpret == interpret) return this;
    return create(this.children, this.style, this.nbtPath, interpret, this.separator, this.pos);
  }

  @Override
  public @Nullable Component separator() {
    return this.separator;
  }

  @Override
  public @NotNull BlockNBTComponent separator(final @Nullable ComponentLike separator) {
    return create(this.children, this.style, this.nbtPath, this.interpret, separator, this.pos);
  }

  @Override
  public @NotNull Pos pos() {
    return this.pos;
  }

  @Override
  public @NotNull BlockNBTComponent pos(final @NotNull Pos pos) {
    return create(this.children, this.style, this.nbtPath, this.interpret, this.separator, requireNonNull(pos, "pos"));
  }

  @Override
  public @NotNull BlockNBTComponent children(final @NotNull List<? extends ComponentLike> children) {
    return create(requireNonNull(children, "children"), this.style, this.nbtPath, this.interpret, this.separator, this.pos);
  }

  @Override
  public @NotNull BlockNBTComponent style(final @NotNull Style style) {
    return create(this.children, requireNonNull(style, "style"), this.nbtPath, this.interpret, this.separator, this.pos);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public BlockNBTComponent.@NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractNBTComponentBuilder<BlockNBTComponent, Builder> implements BlockNBTComponent.Builder {
    private @Nullable Pos pos;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull BlockNBTComponent component) {
      super(component);
      this.pos = component.pos();
    }

    @Override
    public BlockNBTComponent.@NotNull Builder pos(final @NotNull Pos pos) {
      this.pos = requireNonNull(pos, "pos");
      return this;
    }

    @Override
    public @NotNull BlockNBTComponent build() {
      if (this.nbtPath == null) throw new IllegalStateException("nbt path must be set");
      if (this.pos == null) throw new IllegalStateException("pos must be set");
      return create(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.pos);
    }
  }

  record LocalPosImpl(
    double left,
    double up,
    double forwards
  ) implements LocalPos {
    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("left", this.left),
        ExaminableProperty.of("up", this.up),
        ExaminableProperty.of("forwards", this.forwards)
      );
    }

    @Override
    public String toString() {
      return String.format("^%f ^%f ^%f", this.left, this.up, this.forwards);
    }

    @Override
    public @NotNull String asString() {
      return Tokens.serializeLocal(this.left) + ' ' + Tokens.serializeLocal(this.up) + ' ' + Tokens.serializeLocal(this.forwards);
    }
  }

  record WorldPosImpl(
    @NotNull Coordinate x,
    @NotNull Coordinate y,
    @NotNull Coordinate z
  ) implements WorldPos {
    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("x", this.x),
        ExaminableProperty.of("y", this.y),
        ExaminableProperty.of("z", this.z)
      );
    }

    @Override
    public String toString() {
      return this.x.toString() + ' ' + this.y.toString() + ' ' + this.z.toString();
    }

    @Override
    public @NotNull String asString() {
      return Tokens.serializeCoordinate(this.x()) + ' ' + Tokens.serializeCoordinate(this.y()) + ' ' + Tokens.serializeCoordinate(this.z());
    }

    record CoordinateImpl(
      int value,
      @NotNull Type type
    ) implements Coordinate {
      CoordinateImpl(final int value, final @NotNull Type type) {
        this.value = value;
        this.type = requireNonNull(type, "type");
      }

      @Override
      public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
          ExaminableProperty.of("value", this.value),
          ExaminableProperty.of("type", this.type)
        );
      }

      @Override
      public String toString() {
        return (this.type == Type.RELATIVE ? "~" : "") + this.value;
      }
    }
  }

  static final class Tokens {
    static final Pattern LOCAL_PATTERN = Pattern.compile("^\\^(\\d+(\\.\\d+)?) \\^(\\d+(\\.\\d+)?) \\^(\\d+(\\.\\d+)?)$");
    static final Pattern WORLD_PATTERN = Pattern.compile("^(~?)(\\d+) (~?)(\\d+) (~?)(\\d+)$");

    static final String LOCAL_SYMBOL = "^";
    static final String RELATIVE_SYMBOL = "~";
    static final String ABSOLUTE_SYMBOL = "";

    private Tokens() {
    }

    static WorldPos.Coordinate deserializeCoordinate(final String prefix, final String value) {
      final int i = Integer.parseInt(value);
      if (prefix.equals(ABSOLUTE_SYMBOL)) {
        return WorldPos.Coordinate.absolute(i);
      } else if (prefix.equals(RELATIVE_SYMBOL)) {
        return WorldPos.Coordinate.relative(i);
      } else {
        throw new AssertionError(); // regex does not allow any other value for prefix.
      }
    }

    static String serializeLocal(final double value) {
      return LOCAL_SYMBOL + value;
    }

    static String serializeCoordinate(final WorldPos.Coordinate coordinate) {
      return (coordinate.type() == WorldPos.Coordinate.Type.RELATIVE ? RELATIVE_SYMBOL : ABSOLUTE_SYMBOL) + coordinate.value();
    }
  }
}
