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
package net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.function.Supplier;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * A map of named {@link Tag} arguments.
 *
 * @since 5.1.0
 */
@ApiStatus.NonExtendable
public interface NamedArgumentMap {

  /**
   * Get whether an argument of that name exists.
   *
   * @param name name of the argument or flag
   * @return whether an argument by this name is present
   * @since 5.1.0
   */
  boolean isPresent(String name);

  /**
   * Get the number of arguments present.
   *
   * @return the number of arguments present
   * @since 5.1.0
   */
  int size();

  /**
   * Get an argument by its name, returning {@code null} if none was found.
   *
   * @param name name of the argument
   * @return the argument
   * @since 5.1.0
   */
  Tag.@Nullable Argument get(String name);

  /**
   * Get the value of a flag. If a flag is present {@code flag},
   * this method return {@link TriState#TRUE}. If a flag
   * is inverted {@code !flag}, {@link TriState#FALSE} is returned.
   * Otherwise, {@link TriState#NOT_SET} is returned.
   *
   * @param name the name of the flag
   * @return its presence status in the tag
   * @since 5.1.0
   */
  TriState flag(String name);

  /**
   * Get whether this flag is set, inverted or not.
   *
   * @param name the name of the flag
   * @return whether it is present
   * @since 5.1.0
   */
  boolean isFlagPresent(String name);

  /**
   * Get an argument by its name, throwing an exception if no argument with that name was present.
   *
   * @param name name of the argument
   * @return the argument
   * @since 5.1.0
   */
  default Tag.Argument orThrow(final String name) {
    return this.orThrow(name, name + " is not present");
  }

  /**
   * Get an argument by its name, throwing an exception if no argument with that name was present.
   *
   * @param name name of the argument
   * @param errorMessage the error to throw if an argument with that name is not present
   * @return the argument
   * @since 5.1.0
   */
  Tag.Argument orThrow(String name, String errorMessage);

  /**
   * Get an argument by its name, throwing an exception if no argument with that name was present.
   *
   * @param name name of the argument
   * @param errorMessage the error to throw if an argument with that name is not present
   * @return the argument
   * @since 5.1.0
   */
  Tag.Argument orThrow(String name, Supplier<String> errorMessage);
}
