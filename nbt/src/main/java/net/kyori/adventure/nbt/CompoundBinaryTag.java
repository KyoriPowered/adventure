/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.nbt;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Binary tag holding a mapping of string keys to {@link BinaryTag} values.
 *
 * @since 4.0.0
 */
public sealed interface CompoundBinaryTag extends BinaryTag, CompoundTagSetter<CompoundBinaryTag>, Iterable<Map.Entry<String, ? extends BinaryTag>> permits CompoundBinaryTagImpl {
  /**
   * Gets an empty compound tag.
   *
   * @return an empty tag
   * @since 4.0.0
   */
  static CompoundBinaryTag empty() {
    return CompoundBinaryTagImpl.EMPTY;
  }

  /**
   * Creates a compound tag populated with {@code tags}.
   *
   * <p>If {@code tags} is empty, {@link #empty()} will be returned.</p>
   *
   * @param tags the map of contents for the created tag
   * @return a compound tag
   * @since 4.4.0
   */
  static CompoundBinaryTag from(final Map<String, ? extends BinaryTag> tags) {
    if (tags.isEmpty()) return empty();
    return new CompoundBinaryTagImpl(tags);
  }

  /**
   * Create a {@link Collector} to consume streams of map entries.
   *
   * <p>In the event of duplicate entries, the last seen entry will be preserved.</p>
   *
   * @return a collector for map entries
   * @since 4.21.0
   */
  static Collector<Map.Entry<String, ? extends BinaryTag>, ?, CompoundBinaryTag> toCompoundTag() {
    return toCompoundTag(Map.Entry::getKey, Map.Entry::getValue);
  }

  /**
   * Create a {@link Collector} to consume streams of a user-chosen type.
   *
   * <p>In the event of duplicate keys, the last seen entry will be preserved.</p>
   *
   * @param <T> the stream value type
   * @param keyLens a function to extract a key from the target object
   * @param valueLens a function to extract a tag value from the target object
   * @return a collector creating compound tags
   * @since 4.21.0
   */
  static <T> Collector<T, ?, CompoundBinaryTag> toCompoundTag(final Function<T, String> keyLens, final Function<T, ? extends BinaryTag> valueLens) {
    requireNonNull(keyLens, "keyLens");
    requireNonNull(valueLens, "valueLens");

    return Collector.of(
      CompoundBinaryTag::builder,
      (b, ent) -> b.put(keyLens.apply(ent), valueLens.apply(ent)),
      (l, r) -> l.put(r.build()),
      CompoundBinaryTag.Builder::build,
      Collector.Characteristics.UNORDERED
    );
  }

  /**
   * Create a {@link Collector} to consume streams of map entries, with initial contents.
   *
   * <p>In the event of duplicate entries, the last seen entry will be preserved.</p>
   *
   * @param initial an existing tag that will initialize the builder
   * @return a collector for map entries
   * @since 4.21.0
   */
  static Collector<Map.Entry<String, ? extends BinaryTag>, ?, CompoundBinaryTag> toCompoundTag(final CompoundBinaryTag initial) {
    return toCompoundTag(initial, Map.Entry::getKey, Map.Entry::getValue);
  }

  /**
   * Create a {@link Collector} to consume streams of a user-chosen type, with initial contents.
   *
   * <p>In the event of duplicate keys, the last seen entry will be preserved.</p>
   *
   * @param <T> the stream value type
   * @param initial an existing tag that will initialize the builder
   * @param keyLens a function to extract a key from the target object
   * @param valueLens a function to extract a tag value from the target object
   * @return a collector creating compound tags
   * @since 4.21.0
   */
  static <T> Collector<T, ?, CompoundBinaryTag> toCompoundTag(final CompoundBinaryTag initial, final Function<T, String> keyLens, final Function<T, ? extends BinaryTag> valueLens) {
    requireNonNull(initial, "initial");
    requireNonNull(keyLens, "keyLens");
    requireNonNull(valueLens, "valueLens");

    return Collector.of(
      () -> CompoundBinaryTag.builder().put(initial),
      (b, ent) -> b.put(keyLens.apply(ent), valueLens.apply(ent)),
      (l, r) -> l.put(r.build()),
      CompoundBinaryTag.Builder::build,
      Collector.Characteristics.UNORDERED
    );
  }

  /**
   * Creates a builder.
   *
   * @return a new builder
   * @since 4.0.0
   */
  static Builder builder() {
    return new CompoundTagBuilder();
  }

  /**
   * Creates a builder with the specified initial capacity.
   *
   * @param initialCapacity the initial capacity
   * @return a new builder
   * @since 4.25.0
   */
  static Builder builder(final @Range(from = 0, to = Integer.MAX_VALUE) int initialCapacity) {
    return new CompoundTagBuilder(initialCapacity);
  }

  @Override
  default BinaryTagType<CompoundBinaryTag> type() {
    return BinaryTagTypes.COMPOUND;
  }

  /**
   * Returns whether the compound contains a specific key.
   *
   * @param key the key to check for
   * @return whether the compound contains the key
   * @since 4.25.0
   */
  boolean contains(final String key);

  /**
   * Returns whether the compound contains a tag of a specific type with a key.
   *
   * @param key the key to check for
   * @param type the type to check for
   * @return whether there is a tag of {@code type} with {@code key}
   * @since 4.25.0
   */
  boolean contains(final String key, final BinaryTagType<?> type);

  /**
   * Gets a set of all keys.
   *
   * @return the keys
   * @since 4.0.0
   */
  Set<String> keySet();

  /**
   * Gets a tag.
   *
   * @param key the key
   * @return a tag
   * @since 4.0.0
   */
  @Nullable BinaryTag get(final String key);

  /**
   * Gets the number of elements in the compound.
   *
   * @return the number of elements in the compound
   * @since 4.15.0
   */
  int size();

  /**
   * Returns whether the compound has tags or not.
   *
   * @return false if the compound has tags
   * @since 4.18.0
   */
  boolean isEmpty();

  /**
   * Gets a boolean.
   *
   * <p>Booleans are stored as a {@link ByteBinaryTag} with a value of {@code 0} for {@code false} and {@code 1} for {@code true}.</p>
   *
   * @param key the key
   * @return the boolean value, or {@code false} if this compound does not contain a boolean tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default boolean getBoolean(final String key) {
    return this.getBoolean(key, false);
  }

  /**
   * Gets a boolean.
   *
   * <p>Booleans are stored as a {@link ByteBinaryTag} with a value of {@code 0} for {@code false} and {@code 1} for {@code true}.</p>
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the boolean value, or {@code defaultValue} if this compound does not contain a boolean tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default boolean getBoolean(final String key, final boolean defaultValue) {
    final BinaryTag tag = this.get(key);
    if (tag instanceof final ByteBinaryTag bbt) {
      // != 0 might look weird, but it is what vanilla does
      return bbt.value() != 0;
    }
    return defaultValue;
  }

  /**
   * Gets a byte.
   *
   * @param key the key
   * @return the byte value, or {@code 0} if this compound does not contain a byte tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default byte getByte(final String key) {
    return this.getByte(key, (byte) 0);
  }

  /**
   * Gets a byte.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the byte value, or {@code defaultValue} if this compound does not contain a byte tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  byte getByte(final String key, final byte defaultValue);

  /**
   * Gets a short.
   *
   * @param key the key
   * @return the short value, or {@code 0} if this compound does not contain a short tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default short getShort(final String key) {
    return this.getShort(key, (short) 0);
  }

  /**
   * Gets a short.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the short value, or {@code defaultValue} if this compound does not contain a short tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  short getShort(final String key, final short defaultValue);

  /**
   * Gets an int.
   *
   * @param key the key
   * @return the int value, or {@code 0} if this compound does not contain an int tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default int getInt(final String key) {
    return this.getInt(key, 0);
  }

  /**
   * Gets an int.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the int value, or {@code defaultValue} if this compound does not contain an int tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  int getInt(final String key, final int defaultValue);

  /**
   * Gets a long.
   *
   * @param key the key
   * @return the long value, or {@code 0} if this compound does not contain a long tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default long getLong(final String key) {
    return this.getLong(key, 0L);
  }

  /**
   * Gets a long.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the long value, or {@code defaultValue} if this compound does not contain a long tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  long getLong(final String key, final long defaultValue);

  /**
   * Gets a float.
   *
   * @param key the key
   * @return the float value, or {@code 0} if this compound does not contain a float tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default float getFloat(final String key) {
    return this.getFloat(key, 0f);
  }

  /**
   * Gets a float.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the float value, or {@code defaultValue} if this compound does not contain a float tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  float getFloat(final String key, final float defaultValue);

  /**
   * Gets a double.
   *
   * @param key the key
   * @return the double value, or {@code 0} if this compound does not contain a double tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default double getDouble(final String key) {
    return this.getDouble(key, 0d);
  }

  /**
   * Gets a double.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the double value, or {@code defaultValue} if this compound does not contain a double tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  double getDouble(final String key, final double defaultValue);

  /**
   * Gets an array of bytes.
   *
   * @param key the key
   * @return the array of bytes, or a zero-length array if this compound does not contain a byte array tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  byte[] getByteArray(final String key);

  /**
   * Gets an array of bytes.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of bytes, or {@code defaultValue}
   * @since 4.0.0
   */
  @Contract("_, !null -> !null")
  byte @Nullable [] getByteArray(final String key, final byte @Nullable [] defaultValue);

  /**
   * Gets a string.
   *
   * @param key the key
   * @return the string value, or {@code ""} if this compound does not contain a string tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default String getString(final String key) {
    return this.getString(key, "");
  }

  /**
   * Gets a string.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the string value, or {@code defaultValue} if this compound does not contain a string tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  @Contract("_, !null -> !null")
  @Nullable String getString(final String key, final @Nullable String defaultValue);

  /**
   * Gets a list.
   *
   * @param key the key
   * @return the list, or a new list if this compound does not contain a list tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default ListBinaryTag getList(final String key) {
    return this.getList(key, ListBinaryTag.empty());
  }

  /**
   * Gets a list.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the list, or {@code defaultValue} if this compound does not contain a list tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  @Contract("_, !null -> !null")
  @Nullable ListBinaryTag getList(final String key, final @Nullable ListBinaryTag defaultValue);

  /**
   * Gets a list, ensuring that the type is the same as {@code type}.
   *
   * @param key the key
   * @param expectedType the expected list type
   * @return the list, or a new list if this compound does not contain a list tag
   *     with the specified key, has a tag with a different type, or the {@link ListBinaryTag#elementType() list type}
   *     does not match {@code expectedType}
   * @since 4.0.0
   */
  default ListBinaryTag getList(final String key, final BinaryTagType<? extends BinaryTag> expectedType) {
    return this.getList(key, expectedType, ListBinaryTag.empty());
  }

  /**
   * Gets a list, ensuring that the type is the same as {@code type}.
   *
   * @param key the key
   * @param expectedType the expected list type
   * @param defaultValue the default value
   * @return the list, or {@code defaultValue} if this compound does not contain a list tag
   *     with the specified key, has a tag with a different type, or the {@link ListBinaryTag#elementType() list type}
   *     does not match {@code expectedType}
   * @since 4.0.0
   */
  @Contract("_, _, !null -> !null")
  @Nullable ListBinaryTag getList(final String key, final BinaryTagType<? extends BinaryTag> expectedType, final @Nullable ListBinaryTag defaultValue);

  /**
   * Gets a compound.
   *
   * @param key the key
   * @return the compound, or a new compound if this compound does not contain a compound tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default CompoundBinaryTag getCompound(final String key) {
    return this.getCompound(key, empty());
  }

  /**
   * Gets a compound.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the compound, or {@code defaultValue} if this compound does not contain a compound tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  @Contract("_, !null -> !null")
  @Nullable CompoundBinaryTag getCompound(final String key, final @Nullable CompoundBinaryTag defaultValue);

  /**
   * Gets an array of ints.
   *
   * @param key the key
   * @return the array of ints, or a zero-length array if this compound does not contain a int array tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  int[] getIntArray(final String key);

  /**
   * Gets an array of ints.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of ints, or {@code defaultValue}
   * @since 4.0.0
   */
  @Contract("_, !null -> !null")
  int @Nullable [] getIntArray(final String key, final int @Nullable [] defaultValue);

  /**
   * Gets an array of longs.
   *
   * @param key the key
   * @return the array of longs, or a zero-length array if this compound does not contain a long array tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  long[] getLongArray(final String key);

  /**
   * Gets an array of longs.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of longs, or {@code defaultValue}
   * @since 4.0.0
   */
  @Contract("_, !null -> !null")
  long @Nullable [] getLongArray(final String key, final long @Nullable [] defaultValue);

  /**
   * Gets a stream of entries in this compound tag.
   *
   * @return a new entry stream
   * @since 4.21.0
   */
  Stream<Map.Entry<String, ? extends BinaryTag>> stream();

  /**
   * A compound tag builder.
   *
   * @since 4.0.0
   */
  sealed interface Builder extends CompoundTagSetter<Builder> permits CompoundTagBuilder {
    /**
     * Builds.
     *
     * @return a compound tag
     * @since 4.0.0
     */
    CompoundBinaryTag build();
  }
}
