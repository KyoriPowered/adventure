/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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

import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Debug;

/**
 * A binary tag holding a {@link String} value.
 *
 * @since 4.0.0
 */
public interface StringBinaryTag extends BinaryTag {
  /**
   * Creates a binary tag holding a {@link String} value.
   *
   * @param value the value
   * @return a binary tag
   * @since 4.0.0
   */
  static @NonNull StringBinaryTag of(final @NonNull String value) {
    return new StringBinaryTagImpl(value);
  }

  @Override
  default @NonNull BinaryTagType<StringBinaryTag> type() {
    return BinaryTagTypes.STRING;
  }

  /**
   * Gets the value.
   *
   * @return the value
   * @since 4.0.0
   */
  @NonNull String value();
}

@Debug.Renderer(text = "\"\\\"\" + this.value + \"\\\"\"", hasChildren = "false")
final class StringBinaryTagImpl extends AbstractBinaryTag implements StringBinaryTag {
  private final String value;

  StringBinaryTagImpl(final String value) {
    this.value = value;
  }

  @Override
  public @NonNull String value() {
    return this.value;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final StringBinaryTagImpl that = (StringBinaryTagImpl) other;
    return this.value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("value", this.value));
  }
}
