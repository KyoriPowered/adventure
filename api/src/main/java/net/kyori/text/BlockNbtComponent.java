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

import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A block NBT component.
 */
public interface BlockNbtComponent extends NbtComponent<BlockNbtComponent, BlockNbtComponent.Builder>, ScopedComponent<BlockNbtComponent> {
  /**
   * Creates a block NBT component builder.
   *
   * @return a builder
   */
  static @NonNull Builder builder() {
    return new BlockNbtComponentImpl.BuilderImpl();
  }

  /**
   * Creates a block NBT component with a position.
   *
   * @param nbtPath the nbt path
   * @param pos the block position
   * @return the block NBT component
   */
  static @NonNull BlockNbtComponent of(final @NonNull String nbtPath, final @NonNull Pos pos) {
    return builder().nbtPath(nbtPath).pos(pos).build();
  }

  /**
   * Creates a block NBT component by applying configuration from {@code consumer}.
   *
   * @param consumer the builder configurator
   * @return the block NBT component
   */
  static @NonNull BlockNbtComponent make(final @NonNull Consumer<? super Builder> consumer) {
    final Builder builder = builder();
    return AbstractComponentBuilder.configureAndBuild(builder, consumer);
  }

  /**
   * Gets the block position.
   *
   * @return the block position
   */
  @NonNull Pos pos();

  /**
   * Sets the block position.
   *
   * @param pos the block position
   * @return a component
   */
  @NonNull BlockNbtComponent pos(final @NonNull Pos pos);

  /**
   * Sets the block position to a {@link LocalPos} with the given coordinates.
   * 
   * @param left the left coordinate
   * @param up the up coordinate
   * @param forwards the forwards coordinate
   * @return a component
   */
  default @NonNull BlockNbtComponent localPos(final double left, final double up, final double forwards) {
    return this.pos(LocalPos.of(left, up, forwards));
  }

  /**
   * Sets the block position to a {@link WorldPos} with the given coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @return a component
   */
  default @NonNull BlockNbtComponent worldPos(final WorldPos.@NonNull Coordinate x, final WorldPos.@NonNull Coordinate y, final WorldPos.@NonNull Coordinate z) {
    return this.pos(WorldPos.of(x, y, z));
  }

  /**
   * Sets the block position to an absolute {@link WorldPos} with the given coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @return a component
   */
  default @NonNull BlockNbtComponent absoluteWorldPos(final int x, final int y, final int z) {
    return this.worldPos(WorldPos.Coordinate.absolute(x), WorldPos.Coordinate.absolute(y), WorldPos.Coordinate.absolute(z));
  }

  /**
   * Sets the block position to an relative {@link WorldPos} with the given coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @return a component
   */
  default @NonNull BlockNbtComponent relativeWorldPos(final int x, final int y, final int z) {
    return this.worldPos(WorldPos.Coordinate.relative(x), WorldPos.Coordinate.relative(y), WorldPos.Coordinate.relative(z));
  }

  /**
   * An NBT component builder.
   */
  interface Builder extends NbtComponentBuilder<BlockNbtComponent, Builder> {
    /**
     * Sets the block position.
     *
     * @param pos the block position
     * @return this builder
     */
    @NonNull Builder pos(final @NonNull Pos pos);

    /**
     * Sets the block position to a {@link LocalPos} with the given values.
     *
     * @param left the left value
     * @param up the up value
     * @param forwards the forwards value
     * @return this builder
     */
    default @NonNull Builder localPos(final double left, final double up, final double forwards) {
      return this.pos(LocalPos.of(left, up, forwards));
    }

    /**
     * Sets the block position to a {@link WorldPos} with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this builder
     */
    default @NonNull Builder worldPos(final WorldPos.@NonNull Coordinate x, final WorldPos.@NonNull Coordinate y, final WorldPos.@NonNull Coordinate z) {
      return this.pos(WorldPos.of(x, y, z));
    }

    /**
     * Sets the block position to an absolute {@link WorldPos} with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this builder
     */
    default @NonNull Builder absoluteWorldPos(final int x, final int y, final int z) {
      return this.worldPos(WorldPos.Coordinate.absolute(x), WorldPos.Coordinate.absolute(y), WorldPos.Coordinate.absolute(z));
    }

    /**
     * Sets the block position to an relative {@link WorldPos} with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this builder
     */
    default @NonNull Builder relativeWorldPos(final int x, final int y, final int z) {
      return this.worldPos(WorldPos.Coordinate.relative(x), WorldPos.Coordinate.relative(y), WorldPos.Coordinate.relative(z));
    }
  }

  /**
   * A position.
   */
  interface Pos {
  }

  /**
   * A local position.
   */
  interface LocalPos extends Pos {
    /**
     * Creates a local position with the given values.
     *
     * @param left the left value
     * @param up the up value
     * @param forwards the forwards value
     * @return a local position
     */
    static @NonNull LocalPos of(final double left, final double up, final double forwards) {
      return new BlockNbtComponentImpl.LocalPosImpl(left, up, forwards);
    }

    /**
     * Gets the left value.
     * 
     * @return the left value
     */
    double left();

    /**
     * Gets the up value.
     *
     * @return the up value
     */
    double up();

    /**
     * Gets the forwards value.
     *
     * @return the forwards value
     */
    double forwards();
  }

  /**
   * A world position.
   */
  interface WorldPos extends Pos {
    /**
     * Creates a world position with the given coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return a world position
     */
    static @NonNull WorldPos of(final @NonNull Coordinate x, final @NonNull Coordinate y, final @NonNull Coordinate z) {
      return new BlockNbtComponentImpl.WorldPosImpl(x, y, z);
    }

    /**
     * Gets the x coordinate.
     * 
     * @return the x coordinate
     */
    @NonNull Coordinate x();

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate
     */
    @NonNull Coordinate y();

    /**
     * Gets the z coordinate.
     *
     * @return the z coordinate
     */
    @NonNull Coordinate z();

    /**
     * A coordinate component within a {@link WorldPos}.
     */
    interface Coordinate {
      /**
       * Creates a absolute coordinate with the given value.
       *
       * @param value the value
       * @return a coordinate
       */
      static @NonNull Coordinate absolute(final int value) {
        return of(value, Type.ABSOLUTE);
      }

      /**
       * Creates a relative coordinate with the given value.
       *
       * @param value the value
       * @return a coordinate
       */
      static @NonNull Coordinate relative(final int value) {
        return of(value, Type.RELATIVE);
      }

      /**
       * Creates a coordinate with the given value and type.
       *
       * @param value the value
       * @param type the type
       * @return a coordinate
       */
      static @NonNull Coordinate of(final int value, final @NonNull Type type) {
        return new BlockNbtComponentImpl.WorldPosImpl.CoordinateImpl(value, type);
      }

      /**
       * Gets the value.
       *
       * @return the value
       */
      int value();

      /**
       * Gets the type.
       *
       * @return the type
       */
      @NonNull Type type();

      /**
       * The type of a coordinate.
       */
      enum Type {
        ABSOLUTE,
        RELATIVE;
      }
    }
  }
}
