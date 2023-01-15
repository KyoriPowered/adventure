/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import java.util.regex.Matcher;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Given an in-game position, this component reads the NBT of the associated block and displays that information.
 *
 * <p>This component consists of:</p>
 * <dl>
 *   <dt>pos</dt>
 *   <dd>a position in the world the component is being displayed in.</dd>
 *   <dt>everything in</dt>
 *   <dd>{@link NBTComponent}</dd>
 * </dl>
 *
 * @see NBTComponent
 * @since 4.0.0
 * @sinceMinecraft 1.14
 */
public interface BlockNBTComponent extends NBTComponent<BlockNBTComponent, BlockNBTComponent.Builder>, ScopedComponent<BlockNBTComponent> {
  /**
   * Gets the block position.
   *
   * @return the block position
   * @since 4.0.0
   */
  @NotNull Pos pos();

  /**
   * Sets the block position.
   *
   * @param pos the block position
   * @return a block NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NotNull BlockNBTComponent pos(final @NotNull Pos pos);

  /**
   * Sets the block position to a {@link LocalPos} with the given coordinates.
   *
   * @param left the left coordinate
   * @param up the up coordinate
   * @param forwards the forwards coordinate
   * @return a block NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  default @NotNull BlockNBTComponent localPos(final double left, final double up, final double forwards) {
    return this.pos(LocalPos.localPos(left, up, forwards));
  }

  /**
   * Sets the block position to a {@link WorldPos} with the given coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @return a block NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  default @NotNull BlockNBTComponent worldPos(final WorldPos.@NotNull Coordinate x, final WorldPos.@NotNull Coordinate y, final WorldPos.@NotNull Coordinate z) {
    return this.pos(WorldPos.worldPos(x, y, z));
  }

  /**
   * Sets the block position to an absolute {@link WorldPos} with the given coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @return a block NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  default @NotNull BlockNBTComponent absoluteWorldPos(final int x, final int y, final int z) {
    return this.worldPos(WorldPos.Coordinate.absolute(x), WorldPos.Coordinate.absolute(y), WorldPos.Coordinate.absolute(z));
  }

  /**
   * Sets the block position to an relative {@link WorldPos} with the given coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @return a block NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  default @NotNull BlockNBTComponent relativeWorldPos(final int x, final int y, final int z) {
    return this.worldPos(WorldPos.Coordinate.relative(x), WorldPos.Coordinate.relative(y), WorldPos.Coordinate.relative(z));
  }

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("pos", this.pos())
      ),
      NBTComponent.super.examinableProperties()
    );
  }

  /**
   * An NBT component builder.
   *
   * @since 4.0.0
   */
  interface Builder extends NBTComponentBuilder<BlockNBTComponent, Builder> {
    /**
     * Sets the block position.
     *
     * @param pos the block position
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_ -> this")
    @NotNull Builder pos(final @NotNull Pos pos);

    /**
     * Sets the block position to a {@link LocalPos} with the given values.
     *
     * @param left the left value
     * @param up the up value
     * @param forwards the forwards value
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_, _, _ -> this")
    default @NotNull Builder localPos(final double left, final double up, final double forwards) {
      return this.pos(LocalPos.localPos(left, up, forwards));
    }

    /**
     * Sets the block position to a {@link WorldPos} with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_, _, _ -> this")
    default @NotNull Builder worldPos(final WorldPos.@NotNull Coordinate x, final WorldPos.@NotNull Coordinate y, final WorldPos.@NotNull Coordinate z) {
      return this.pos(WorldPos.worldPos(x, y, z));
    }

    /**
     * Sets the block position to an absolute {@link WorldPos} with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_, _, _ -> this")
    default @NotNull Builder absoluteWorldPos(final int x, final int y, final int z) {
      return this.worldPos(WorldPos.Coordinate.absolute(x), WorldPos.Coordinate.absolute(y), WorldPos.Coordinate.absolute(z));
    }

    /**
     * Sets the block position to an relative {@link WorldPos} with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this builder
     * @since 4.0.0
     */
    @Contract("_, _, _ -> this")
    default @NotNull Builder relativeWorldPos(final int x, final int y, final int z) {
      return this.worldPos(WorldPos.Coordinate.relative(x), WorldPos.Coordinate.relative(y), WorldPos.Coordinate.relative(z));
    }
  }

  /**
   * A position.
   *
   * @since 4.0.0
   */
  interface Pos extends Examinable {
    /**
     * Attempt to parse a position from the input string.
     *
     * <p>The input string must refer to a local position (with 3 {@code ^}-prefixed digits),
     * or a world position (with 3 digits that are global if unprefixed, or relative to the
     * current position if {@code ~}-prefixed).</p>
     *
     * @param input input
     * @return a new pos
     * @throws IllegalArgumentException if the position was in an invalid format
     * @since 4.0.0
     */
    static @NotNull Pos fromString(final @NotNull String input) throws IllegalArgumentException {
      final Matcher localMatch = BlockNBTComponentImpl.Tokens.LOCAL_PATTERN.matcher(input);
      if (localMatch.matches()) {
        return BlockNBTComponent.LocalPos.localPos(
          Double.parseDouble(localMatch.group(1)),
          Double.parseDouble(localMatch.group(3)),
          Double.parseDouble(localMatch.group(5))
        );
      }

      final Matcher worldMatch = BlockNBTComponentImpl.Tokens.WORLD_PATTERN.matcher(input);
      if (worldMatch.matches()) {
        return BlockNBTComponent.WorldPos.worldPos(
          BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(1), worldMatch.group(2)),
          BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(3), worldMatch.group(4)),
          BlockNBTComponentImpl.Tokens.deserializeCoordinate(worldMatch.group(5), worldMatch.group(6))
        );
      }

      throw new IllegalArgumentException("Cannot convert position specification '" + input + "' into a position");
    }

    /**
     * Gets a parseable string representation of this position.
     *
     * @return a string representation
     * @see #fromString(String)
     * @since 4.0.0
     */
    @NotNull String asString();
  }

  /**
   * A local position.
   *
   * @since 4.0.0
   */
  interface LocalPos extends Pos {
    /**
     * Creates a local position with the given values.
     *
     * @param left the left value
     * @param up the up value
     * @param forwards the forwards value
     * @return a local position
     * @since 4.10.0
     */
    static @NotNull LocalPos localPos(final double left, final double up, final double forwards) {
      return new BlockNBTComponentImpl.LocalPosImpl(left, up, forwards);
    }

    /**
     * Creates a local position with the given values.
     *
     * @param left the left value
     * @param up the up value
     * @param forwards the forwards value
     * @return a local position
     * @since 4.0.0
     * @deprecated for removal since 4.10.0, use {@link #localPos(double, double, double)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    static @NotNull LocalPos of(final double left, final double up, final double forwards) {
      return new BlockNBTComponentImpl.LocalPosImpl(left, up, forwards);
    }

    /**
     * Gets the left value.
     *
     * @return the left value
     * @since 4.0.0
     */
    double left();

    /**
     * Gets the up value.
     *
     * @return the up value
     * @since 4.0.0
     */
    double up();

    /**
     * Gets the forwards value.
     *
     * @return the forwards value
     * @since 4.0.0
     */
    double forwards();
  }

  /**
   * A world position.
   *
   * @since 4.0.0
   */
  interface WorldPos extends Pos {
    /**
     * Creates a world position with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return a world position
     * @since 4.10.0
     */
    static @NotNull WorldPos worldPos(final @NotNull Coordinate x, final @NotNull Coordinate y, final @NotNull Coordinate z) {
      return new BlockNBTComponentImpl.WorldPosImpl(x, y, z);
    }

    /**
     * Creates a world position with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return a world position
     * @since 4.0.0
     * @deprecated for removal since 4.10.0, use {@link #worldPos(WorldPos.Coordinate, WorldPos.Coordinate, WorldPos.Coordinate)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    static @NotNull WorldPos of(final @NotNull Coordinate x, final @NotNull Coordinate y, final @NotNull Coordinate z) {
      return new BlockNBTComponentImpl.WorldPosImpl(x, y, z);
    }

    /**
     * Gets the x coordinate.
     *
     * @return the x coordinate
     * @since 4.0.0
     */
    @NotNull Coordinate x();

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate
     * @since 4.0.0
     */
    @NotNull Coordinate y();

    /**
     * Gets the z coordinate.
     *
     * @return the z coordinate
     * @since 4.0.0
     */
    @NotNull Coordinate z();

    /**
     * A coordinate component within a {@link WorldPos}.
     *
     * @since 4.0.0
     */
    interface Coordinate extends Examinable {
      /**
       * Creates a absolute coordinate with the given value.
       *
       * @param value the value
       * @return a coordinate
       * @since 4.0.0
       */
      static @NotNull Coordinate absolute(final int value) {
        return coordinate(value, Type.ABSOLUTE);
      }

      /**
       * Creates a relative coordinate with the given value.
       *
       * @param value the value
       * @return a coordinate
       * @since 4.0.0
       */
      static @NotNull Coordinate relative(final int value) {
        return coordinate(value, Type.RELATIVE);
      }

      /**
       * Creates a coordinate with the given value and type.
       *
       * @param value the value
       * @param type the type
       * @return a coordinate
       * @since 4.10.0
       */
      static @NotNull Coordinate coordinate(final int value, final @NotNull Type type) {
        return new BlockNBTComponentImpl.WorldPosImpl.CoordinateImpl(value, type);
      }

      /**
       * Creates a coordinate with the given value and type.
       *
       * @param value the value
       * @param type the type
       * @return a coordinate
       * @since 4.0.0
       * @deprecated for removal since 4.10.0, use {@link #coordinate(int, Coordinate.Type)} instead.
       */
      @Deprecated
      @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
      static @NotNull Coordinate of(final int value, final @NotNull Type type) {
        return new BlockNBTComponentImpl.WorldPosImpl.CoordinateImpl(value, type);
      }

      /**
       * Gets the value.
       *
       * @return the value
       * @since 4.0.0
       */
      int value();

      /**
       * Gets the type.
       *
       * @return the type
       * @since 4.0.0
       */
      @NotNull Type type();

      /**
       * The type of a coordinate.
       *
       * @since 4.0.0
       */
      enum Type {
        /**
         * An absolute coordinate.
         *
         * @since 4.0.0
         */
        ABSOLUTE,
        /**
         * A relative coordinate.
         *
         * @since 4.0.0
         */
        RELATIVE;
      }
    }
  }
}
