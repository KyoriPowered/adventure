/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text.format;

import java.util.EnumSet;
import net.kyori.text.Component;
import net.kyori.text.util.NameMap;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.Set;

/**
 * An enumeration of decorations which may be applied to a {@link Component}.
 */
public enum TextDecoration implements TextFormat {
  /**
   * A decoration which makes text obfuscated/unreadable.
   */
  OBFUSCATED("obfuscated"),
  /**
   * A decoration which makes text appear bold.
   */
  BOLD("bold"),
  /**
   * A decoration which makes text have a strike through it.
   */
  STRIKETHROUGH("strikethrough"),
  /**
   * A decoration which makes text have an underline.
   */
  UNDERLINED("underlined"),
  /**
   * A decoration which makes text appear in italics.
   */
  ITALIC("italic");

  public static final NameMap<TextDecoration> NAMES = NameMap.create(values(), constant -> constant.name);
  /**
   * The name of this decoration.
   */
  private final String name;

  TextDecoration(final String name) {
    this.name = name;
  }

  @Override
  public @NonNull String toString() {
    return this.name;
  }

  public static @NonNull Set<TextDecoration> setOf(final @NonNull TextDecoration... decorations) {
    final Set<TextDecoration> set = EnumSet.noneOf(TextDecoration.class);
    Collections.addAll(set, decorations);
    return set;
  }

  /**
   * A state that a {@link TextDecoration} can be in.
   */
  public enum State {
    NOT_SET {
      @Override
      public String toString() {
        return "null";
      }
    },
    FALSE,
    TRUE;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }

    /**
     * Gets a state from a {@code boolean}.
     *
     * @param flag the boolean
     * @return the state
     */
    public static @NonNull State byBoolean(final boolean flag) {
      return flag ? TRUE : FALSE;
    }

    /**
     * Gets a state from a {@code Boolean}.
     *
     * @param flag the boolean
     * @return the state
     */
    public static @NonNull State byBoolean(final @Nullable Boolean flag) {
      return flag == null ? NOT_SET : byBoolean(flag.booleanValue());
    }
  }
}
