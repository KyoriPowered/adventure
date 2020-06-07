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
package net.kyori.adventure.nbt;

import java.util.Set;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface CompoundTag extends CompoundTagSetter<CompoundTag>, Tag {
  static @NonNull CompoundTag empty() {
    return CompoundTagImpl.EMPTY;
  }

  static @NonNull Builder builder() {
    return new CompoundTagBuilder();
  }

  @Override
  default @NonNull TagType<CompoundTag> type() {
    return TagTypes.COMPOUND;
  }

  @NonNull Set<String> keySet();

  @Nullable Tag get(final String key);

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
   * Inserts a byte.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putByte(final @NonNull String key, final byte value);

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
   * Inserts a short.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putShort(final @NonNull String key, final short value);

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
   * Inserts an int.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putInt(final @NonNull String key, final int value);

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
   * Inserts a long.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putLong(final @NonNull String key, final long value);

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
   * Inserts a float.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putFloat(final @NonNull String key, final float value);

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
   * Inserts a double.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putDouble(final @NonNull String key, final double value);

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
   * Inserts an array of bytes.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putByteArray(final @NonNull String key, final byte@NonNull[] value);

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
   * Inserts a string.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putString(final @NonNull String key, final @NonNull String value);

  /**
   * Gets a list.
   *
   * @param key the key
   * @return the list, or a new list if this compound does not contain a list tag
   *     with the specified key, or has a tag with a different type
   */
  default @NonNull ListTag getList(final @NonNull String key) {
    return this.getList(key, ListTag.empty());
  }

  /**
   * Gets a list.
   *
   * @param key the key
   * @param defaultValue the default value
   * @return the list, or {@code defaultValue} if this compound does not contain a list tag
   *     with the specified key, or has a tag with a different type
   */
  @NonNull ListTag getList(final @NonNull String key, final @NonNull ListTag defaultValue);

  /**
   * Gets a list, ensuring that the type is the same as {@code type}.
   *
   * @param key the key
   * @param expectedType the expected list type
   * @return the list, or a new list if this compound does not contain a list tag
   *     with the specified key, has a tag with a different type, or the {@link ListTag#listType() list type}
   *     does not match {@code expectedType}
   */
  default @NonNull ListTag getList(final @NonNull String key, final @NonNull TagType<? extends Tag> expectedType) {
    return this.getList(key, expectedType, ListTag.empty());
  }

  /**
   * Gets a list, ensuring that the type is the same as {@code type}.
   *
   * @param key the key
   * @param expectedType the expected list type
   * @param defaultValue the default value
   * @return the list, or {@code defaultValue} if this compound does not contain a list tag
   *     with the specified key, has a tag with a different type, or the {@link ListTag#listType() list type}
   *     does not match {@code expectedType}
   */
  @NonNull ListTag getList(final @NonNull String key, final @NonNull TagType<? extends Tag> expectedType, final @NonNull ListTag defaultValue);

  /**
   * Gets a compound.
   *
   * @param key the key
   * @return the compound, or a new compound if this compound does not contain a compound tag
   *     with the specified key, or has a tag with a different type
   */
  default @NonNull CompoundTag getCompound(final @NonNull String key) {
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
  @NonNull CompoundTag getCompound(final @NonNull String key, final @NonNull CompoundTag defaultValue);

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
   * Inserts an array of ints.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putIntArray(final @NonNull String key, final int@NonNull[] value);

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
   * Inserts an array of longs.
   *
   * @param key the key
   * @param value the value
   * @return a compound tag
   */
  @Override
  @NonNull CompoundTag putLongArray(final @NonNull String key, final long@NonNull[] value);

  interface Builder extends CompoundTagSetter<Builder> {
    @NonNull CompoundTag build();
  }
}
