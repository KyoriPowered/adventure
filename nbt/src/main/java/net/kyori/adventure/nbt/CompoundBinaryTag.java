/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2024 KyoriPowered
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Binary tag holding a mapping of string keys to {@link BinaryTag} values.
 *
 * @since 4.0.0
 */
public interface CompoundBinaryTag extends BinaryTag, CompoundTagSetter<CompoundBinaryTag>, Iterable<Map.Entry<String, ? extends BinaryTag>> {
  /**
   * Gets an empty compound tag.
   *
   * @return an empty tag
   * @since 4.0.0
   */
  static @NotNull CompoundBinaryTag empty() {
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
  static @NotNull CompoundBinaryTag from(final @NotNull Map<String, ? extends BinaryTag> tags) {
    if (tags.isEmpty()) return empty();
    return new CompoundBinaryTagImpl(new HashMap<>(tags)); // explicitly copy
  }

  /**
   * Creates a builder.
   *
   * @return a new builder
   * @since 4.0.0
   */
  static @NotNull Builder builder() {
    return new CompoundTagBuilder();
  }

  @Override
  default @NotNull BinaryTagType<CompoundBinaryTag> type() {
    return BinaryTagTypes.COMPOUND;
  }

  /**
   * Gets a set of all keys.
   *
   * @return the keys
   * @since 4.0.0
   */
  @NotNull Set<String> keySet();

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
   * Gets a boolean.
   *
   * <p>Booleans are stored as a {@link ByteBinaryTag} with a value of {@code 0} for {@code false} and {@code 1} for {@code true}.</p>
   *
   * @param key the key
   * @return the boolean value, or {@code false} if this compound does not contain a boolean tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default boolean getBoolean(final @NotNull String key) {
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
  default boolean getBoolean(final @NotNull String key, final boolean defaultValue) {
    // != 0 might look weird, but it is what vanilla does
    return this.getByte(key) != 0 || defaultValue;
  }

  /**
   * Gets a byte.
   *
   * @param key the key
   * @return the byte value, or {@code 0} if this compound does not contain a byte tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default byte getByte(final @NotNull String key) {
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
  byte getByte(final @NotNull String key, final byte defaultValue);

  /**
   * Gets a short.
   *
   * @param key the key
   * @return the short value, or {@code 0} if this compound does not contain a short tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default short getShort(final @NotNull String key) {
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
  short getShort(final @NotNull String key, final short defaultValue);

  /**
   * Gets an int.
   *
   * @param key the key
   * @return the int value, or {@code 0} if this compound does not contain an int tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default int getInt(final @NotNull String key) {
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
  int getInt(final @NotNull String key, final int defaultValue);

  /**
   * Gets a long.
   *
   * @param key the key
   * @return the long value, or {@code 0} if this compound does not contain a long tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default long getLong(final @NotNull String key) {
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
  long getLong(final @NotNull String key, final long defaultValue);

  /**
   * Gets a float.
   *
   * @param key the key
   * @return the float value, or {@code 0} if this compound does not contain a float tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default float getFloat(final @NotNull String key) {
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
  float getFloat(final @NotNull String key, final float defaultValue);

  /**
   * Gets a double.
   *
   * @param key the key
   * @return the double value, or {@code 0} if this compound does not contain a double tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default double getDouble(final @NotNull String key) {
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
  double getDouble(final @NotNull String key, final double defaultValue);

  /**
   * Gets an array of bytes.
   *
   * @param key the key
   * @return the array of bytes, or a zero-length array if this compound does not contain a byte array tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  byte@NotNull[] getByteArray(final @NotNull String key);

  /**
   * Gets an array of bytes.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of bytes, or {@code defaultValue}
   * @since 4.0.0
   */
  byte@NotNull[] getByteArray(final @NotNull String key, final byte@NotNull[] defaultValue);

  /**
   * Gets a string.
   *
   * @param key the key
   * @return the string value, or {@code ""} if this compound does not contain a string tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default @NotNull String getString(final @NotNull String key) {
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
  @NotNull String getString(final @NotNull String key, final @NotNull String defaultValue);

  /**
   * Gets a list.
   *
   * @param key the key
   * @return the list, or a new list if this compound does not contain a list tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default @NotNull ListBinaryTag getList(final @NotNull String key) {
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
  @NotNull ListBinaryTag getList(final @NotNull String key, final @NotNull ListBinaryTag defaultValue);

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
  default @NotNull ListBinaryTag getList(final @NotNull String key, final @NotNull BinaryTagType<? extends BinaryTag> expectedType) {
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
  @NotNull ListBinaryTag getList(final @NotNull String key, final @NotNull BinaryTagType<? extends BinaryTag> expectedType, final @NotNull ListBinaryTag defaultValue);

  /**
   * Gets a compound.
   *
   * @param key the key
   * @return the compound, or a new compound if this compound does not contain a compound tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  default @NotNull CompoundBinaryTag getCompound(final @NotNull String key) {
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
  @NotNull CompoundBinaryTag getCompound(final @NotNull String key, final @NotNull CompoundBinaryTag defaultValue);

  /**
   * Gets an array of ints.
   *
   * @param key the key
   * @return the array of ints, or a zero-length array if this compound does not contain a int array tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  int@NotNull[] getIntArray(final @NotNull String key);

  /**
   * Gets an array of ints.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of ints, or {@code defaultValue}
   * @since 4.0.0
   */
  int@NotNull[] getIntArray(final @NotNull String key, final int@NotNull[] defaultValue);

  /**
   * Gets an array of longs.
   *
   * @param key the key
   * @return the array of longs, or a zero-length array if this compound does not contain a long array tag
   *     with the specified key, or has a tag with a different type
   * @since 4.0.0
   */
  long@NotNull[] getLongArray(final @NotNull String key);

  /**
   * Gets an array of longs.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of longs, or {@code defaultValue}
   * @since 4.0.0
   */
  long@NotNull[] getLongArray(final @NotNull String key, final long@NotNull[] defaultValue);

  /**
   * A compound tag builder.
   *
   * @since 4.0.0
   */
  interface Builder extends CompoundTagSetter<Builder> {
    /**
     * Builds.
     *
     * @return a compound tag
     * @since 4.0.0
     */
    @NotNull CompoundBinaryTag build();
  }
}
