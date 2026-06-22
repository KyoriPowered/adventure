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
package net.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

/**
 * A component that can display NBT fetched from different locations, optionally trying to interpret the NBT as JSON
 * using the {@code net.kyori.adventure.text.serializer.gson.GsonComponentSerializer} to convert the JSON to a {@link Component}.
 * Sending interpreted NBT to the chat would be similar to using {@code /tellraw}.
 *
 * <p>This component consists of:</p>
 * <dl>
 *   <dt>nbtPath</dt>
 *   <dd>a path to specify which parts of the nbt you want displayed(<a href="https://minecraft.wiki/w/NBT_path_format#Examples">examples</a>).</dd>
 *   <dt>interpret</dt>
 *   <dd>a boolean telling adventure if the fetched NBT value should be parsed as JSON</dd>
 *   <dt>plain</dt>
 *   <dd>a boolean telling adventure if the fetched NBT value should be pretty-printed without styling</dd>
 * </dl>
 *
 * <p>This component is rendered serverside and can therefore receive platform-defined
 * context. See the documentation for your respective
 * platform for more info</p>
 *
 * @param <C> component type
 * @since 4.0.0
 * @sinceMinecraft 1.14
 */
@SuppressWarnings("removal")
public sealed interface NBTComponent<C extends NBTComponent<C>> extends BuildableComponent<C, NBTComponentBuilder<C, ?>> permits BlockNBTComponent, EntityNBTComponent, StorageNBTComponent {
  /**
   * The default value for {@link #interpret()}.
   *
   * @since 5.0.0
   */
  boolean INTERPRET_DEFAULT = false;

  /**
   * The default value for {@link #plain()}.
   *
   * @since 5.0.0
   */
  boolean PLAIN_DEFAULT = false;

  /**
   * Gets the NBT path.
   *
   * @return the NBT path
   * @since 4.0.0
   */
  String nbtPath();

  /**
   * Sets the NBT path.
   *
   * @param nbtPath the NBT path
   * @return an NBT component
   * @since 4.0.0
   */
  @Contract(pure = true)
  C nbtPath(final String nbtPath);

  /**
   * Gets if we should be interpreting.
   *
   * <p>This cannot be {@code true} if {@link #plain()} is also {@code true}.</p>
   *
   * @return if we should be interpreting
   * @since 4.0.0
   */
  boolean interpret();

  /**
   * Sets if we should be interpreting.
   *
   * <p>This cannot be {@code true} if {@link #plain()} is also {@code true}.</p>
   *
   * @param interpret if we should be interpreting.
   * @return an NBT component
   * @throws IllegalArgumentException if set to {@code true} and {@link #plain()} is also {@code true}
   * @since 4.0.0
   */
  @Contract(pure = true)
  C interpret(final boolean interpret);

  /**
   * Gets the separator.
   *
   * @return the separator
   * @since 4.8.0
   */
  @Nullable Component separator();

  /**
   * Sets the separator.
   *
   * @param separator the separator
   * @return an NBT component
   * @since 4.8.0
   */
  C separator(final @Nullable ComponentLike separator);

  /**
   * Gets if styling should be removed from pretty-printed NBT.
   *
   * <p>This cannot be {@code true} if {@link #interpret()} is also {@code true}.</p>
   *
   * @return if styling should be removed when pretty-printed
   * @since 5.0.0
   * @sinceMinecraft 26.1
   */
  boolean plain();

  /**
   * Sets if styling should be removed from pretty-printed NBT.
   *
   * <p>This cannot be {@code true} if {@link #interpret()} is also {@code true}.</p>
   *
   * @param plain if styling should be removed when pretty-printed
   * @return an NBT component
   * @throws IllegalArgumentException if set to {@code true} and {@link #interpret()} is also {@code true}
   * @since 5.0.0
   * @sinceMinecraft 26.1
   */
  C plain(final boolean plain);

  @Override
  NBTComponentBuilder<C, ? extends NBTComponentBuilder<C, ?>> toBuilder();
}
