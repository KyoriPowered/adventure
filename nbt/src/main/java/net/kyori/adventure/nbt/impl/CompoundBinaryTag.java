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
package net.kyori.adventure.nbt.impl;

import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface CompoundBinaryTag extends BinaryTag, CompoundTagSetter<CompoundBinaryTag>, Iterable<Map.Entry<String, ? extends BinaryTag>> {
  /**
   * Gets an empty compound tag.
   *
   * @return an empty tag
   */
  static @NonNull CompoundBinaryTag empty() {
    return CompoundBinaryTagImpl.EMPTY;
  }

  /**
   * Creates a builder.
   *
   * @return a new builder
   */
  static @NonNull Builder builder() {
    return new CompoundTagBuilder();
  }

  @Override
  default @NonNull BinaryTagType<CompoundBinaryTag> type() {
    return BinaryTagTypes.COMPOUND;
  }

  /**
   * Gets a set of all keys.
   *
   * @return the keys
   */
  @NonNull Set<String> keySet();

  /**
   * Gets a tag.
   *
   * @param key the key
   * @return a tag
   */
  @Nullable BinaryTag get(final String key);

  /**
   * Gets a byte.
   *
   * @param key the key
   * @return the byte value, or {@code 0} if this compound does not contain a byte tag
   *     with the specified key, or has a tag with a different type
   */
  default byte getByte(final @NonNull String key) {
    return this.getByte(key, (byte) 0);
  }

  /**
   * Gets a byte.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the byte value, or {@code defaultValue} if this compound does not contain a byte tag
   *     with the specified key, or has a tag with a different type
   */
  byte getByte(final @NonNull String key, final byte defaultValue);

  /**
   * Gets a short.
   *
   * @param key the key
   * @return the short value, or {@code 0} if this compound does not contain a short tag
   *     with the specified key, or has a tag with a different type
   */
  default short getShort(final @NonNull String key) {
    return this.getShort(key, (short) 0);
  }

  /**
   * Gets a short.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the short value, or {@code defaultValue} if this compound does not contain a short tag
   *     with the specified key, or has a tag with a different type
   */
  short getShort(final @NonNull String key, final short defaultValue);

  /**
   * Gets an int.
   *
   * @param key the key
   * @return the int value, or {@code 0} if this compound does not contain an int tag
   *     with the specified key, or has a tag with a different type
   */
  default int getInt(final @NonNull String key) {
    return this.getInt(key, 0);
  }

  /**
   * Gets an int.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the int value, or {@code defaultValue} if this compound does not contain an int tag
   *     with the specified key, or has a tag with a different type
   */
  int getInt(final @NonNull String key, final int defaultValue);

  /**
   * Gets a long.
   *
   * @param key the key
   * @return the long value, or {@code 0} if this compound does not contain a long tag
   *     with the specified key, or has a tag with a different type
   */
  default long getLong(final @NonNull String key) {
    return this.getLong(key, 0L);
  }

  /**
   * Gets a long.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the long value, or {@code defaultValue} if this compound does not contain a long tag
   *     with the specified key, or has a tag with a different type
   */
  long getLong(final @NonNull String key, final long defaultValue);

  /**
   * Gets a float.
   *
   * @param key the key
   * @return the float value, or {@code 0} if this compound does not contain a float tag
   *     with the specified key, or has a tag with a different type
   */
  default float getFloat(final @NonNull String key) {
    return this.getFloat(key, 0f);
  }

  /**
   * Gets a float.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the float value, or {@code defaultValue} if this compound does not contain a float tag
   *     with the specified key, or has a tag with a different type
   */
  float getFloat(final @NonNull String key, final float defaultValue);

  /**
   * Gets a double.
   *
   * @param key the key
   * @return the double value, or {@code 0} if this compound does not contain a double tag
   *     with the specified key, or has a tag with a different type
   */
  default double getDouble(final @NonNull String key) {
    return this.getDouble(key, 0d);
  }

  /**
   * Gets a double.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the double value, or {@code defaultValue} if this compound does not contain a double tag
   *     with the specified key, or has a tag with a different type
   */
  double getDouble(final @NonNull String key, final double defaultValue);

  /**
   * Gets an array of bytes.
   *
   * @param key the key
   * @return the array of bytes, or a zero-length array if this compound does not contain a byte array tag
   *     with the specified key, or has a tag with a different type
   */
  byte@NonNull[] getByteArray(final @NonNull String key);

  /**
   * Gets an array of bytes.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of bytes, or {@code defaultValue}
   */
  byte@NonNull[] getByteArray(final @NonNull String key, final byte@NonNull[] defaultValue);

  /**
   * Gets a string.
   *
   * @param key the key
   * @return the string value, or {@code ""} if this compound does not contain a string tag
   *     with the specified key, or has a tag with a different type
   */
  default @NonNull String getString(final @NonNull String key) {
    return this.getString(key, "");
  }

  /**
   * Gets a string.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the string value, or {@code defaultValue} if this compound does not contain a string tag
   *     with the specified key, or has a tag with a different type
   */
  @NonNull String getString(final @NonNull String key, final @NonNull String defaultValue);

  /**
   * Gets a list.
   *
   * @param key the key
   * @return the list, or a new list if this compound does not contain a list tag
   *     with the specified key, or has a tag with a different type
   */
  default @NonNull ListBinaryTag getList(final @NonNull String key) {
    return this.getList(key, ListBinaryTag.empty());
  }

  /**
   * Gets a list.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the list, or {@code defaultValue} if this compound does not contain a list tag
   *     with the specified key, or has a tag with a different type
   */
  @NonNull ListBinaryTag getList(final @NonNull String key, final @NonNull ListBinaryTag defaultValue);

  /**
   * Gets a list, ensuring that the type is the same as {@code type}.
   *
   * @param key the key
   * @param expectedType the expected list type
   * @return the list, or a new list if this compound does not contain a list tag
   *     with the specified key, has a tag with a different type, or the {@link ListBinaryTag#listType() list type}
   *     does not match {@code expectedType}
   */
  default @NonNull ListBinaryTag getList(final @NonNull String key, final @NonNull BinaryTagType<? extends BinaryTag> expectedType) {
    return this.getList(key, expectedType, ListBinaryTag.empty());
  }

  /**
   * Gets a list, ensuring that the type is the same as {@code type}.
   *
   * @param key the key
   * @param expectedType the expected list type
   * @param defaultValue the default value
   * @return the list, or {@code defaultValue} if this compound does not contain a list tag
   *     with the specified key, has a tag with a different type, or the {@link ListBinaryTag#listType() list type}
   *     does not match {@code expectedType}
   */
  @NonNull ListBinaryTag getList(final @NonNull String key, final @NonNull BinaryTagType<? extends BinaryTag> expectedType, final @NonNull ListBinaryTag defaultValue);

  /**
   * Gets a compound.
   *
   * @param key the key
   * @return the compound, or a new compound if this compound does not contain a compound tag
   *     with the specified key, or has a tag with a different type
   */
  default @NonNull CompoundBinaryTag getCompound(final @NonNull String key) {
    return this.getCompound(key, empty());
  }

  /**
   * Gets a compound.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the compound, or {@code defaultValue} if this compound does not contain a compound tag
   *     with the specified key, or has a tag with a different type
   */
  @NonNull CompoundBinaryTag getCompound(final @NonNull String key, final @NonNull CompoundBinaryTag defaultValue);

  /**
   * Gets an array of ints.
   *
   * @param key the key
   * @return the array of ints, or a zero-length array if this compound does not contain a int array tag
   *     with the specified key, or has a tag with a different type
   */
  int@NonNull[] getIntArray(final @NonNull String key);

  /**
   * Gets an array of ints.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of ints, or {@code defaultValue}
   */
  int@NonNull[] getIntArray(final @NonNull String key, final int@NonNull[] defaultValue);

  /**
   * Gets an array of longs.
   *
   * @param key the key
   * @return the array of longs, or a zero-length array if this compound does not contain a long array tag
   *     with the specified key, or has a tag with a different type
   */
  long@NonNull[] getLongArray(final @NonNull String key);

  /**
   * Gets an array of longs.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the array of longs, or {@code defaultValue}
   */
  long@NonNull[] getLongArray(final @NonNull String key, final long@NonNull[] defaultValue);

  /**
   * A compound tag builder.
   */
  interface Builder extends CompoundTagSetter<Builder> {
    /**
     * Builds.
     *
     * @return a compound tag
     */
    @NonNull CompoundBinaryTag build();
  }
}
