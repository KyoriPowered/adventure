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
package net.kyori.adventure.text;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;

/**
 * A component that can display NBT fetched from different locations, optionally trying to interpret the NBT as JSON
 * using the {@code net.kyori.adventure.text.serializer.gson.GsonComponentSerializer} to convert the JSON to a {@link Component}.
 * Sending interpreted NBT to the chat would be similar to using {@code /tellraw}.
 *
 * <p>This component consists of:</p>
 * <dl>
 *   <dt>nbtPath</dt>
 *   <dd>a path to specify which parts of the nbt you want displayed(<a href="https://minecraft.gamepedia.com/NBT_path_format#Examples">examples</a>).</dd>
 *   <dt>interpret</dt>
 *   <dd>a boolean telling adventure if the fetched NBT value should be parsed as JSON</dd>
 * </dl>
 *
 * <p>This component is rendered serverside and can therefore receive platform-defined
 * context. See the documentation for your respective
 * platform for more info</p>
 *
 * @since 4.0.0
 * @sinceMinecraft 1.14
 */
public interface NBTComponent<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends BuildableComponent<C, B> {
  /**
   * Gets the NBT path.
   *
   * @return the NBT path
   * @since 4.0.0
   */
  @NonNull String nbtPath();

  /**
   * Sets the NBT path.
   *
   * @param nbtPath the NBT path
   * @return an NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NonNull C nbtPath(final @NonNull String nbtPath);

  /**
   * Gets if we should be interpreting.
   *
   * @return if we should be interpreting
   * @since 4.0.0
   */
  boolean interpret();

  /**
   * Sets if we should be interpreting.
   *
   * @param interpret if we should be interpreting.
   * @return an NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NonNull C interpret(final boolean interpret);
}
