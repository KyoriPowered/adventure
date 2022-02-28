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
package net.kyori.adventure.text.minimessage.internal;

import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for tag naming.
 *
 * @since 4.10.0
 */
@ApiStatus.Internal
public final class TagInternals {
  private static final String TAG_NAME_PATTERN = "[!?#]?[a-z0-9_-]*";

  private TagInternals() {
  }

  /**
   * Checks if a tag name matches the pattern for allowed tag names. If it does not, then
   * this method will throw an {@link IllegalArgumentException}
   *
   * @param tagName the name of the tag
   * @since 4.10.0
   */
  public static void checkTagName(final @NotNull String tagName) {
    if (!Objects.requireNonNull(tagName).matches(TAG_NAME_PATTERN)) {
      throw new IllegalArgumentException("Tag name must match pattern " + TAG_NAME_PATTERN + ", was " + tagName);
    }
  }

  /**
   * Checks if a tag name matches the pattern for allowed tag names, first sanitizing it
   * by converting the tag name to lowercase. If it does not match the pattern, then this
   * method will throw an {@link IllegalArgumentException}
   *
   * @param tagName the name of the tag
   * @since 4.10.0
   */
  public static void sanitizeAndCheckTagName(final @NotNull String tagName) {
    checkTagName(Objects.requireNonNull(tagName).toLowerCase(Locale.ROOT));
  }
}
