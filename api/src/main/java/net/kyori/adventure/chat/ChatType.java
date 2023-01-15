/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.chat;

import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A type of chat.
 *
 * @since 4.12.0
 * @sinceMinecraft 1.19
 */
public interface ChatType extends Examinable, Keyed {
  /**
   * A chat message from a player.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  ChatType CHAT = new ChatTypeImpl(Key.key("chat"));

  /**
   * A message send as a result of using the {@code /say} command.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  ChatType SAY_COMMAND = new ChatTypeImpl(Key.key("say_command"));

  /**
   * A message received as a result of using the {@code /msg} command.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  ChatType MSG_COMMAND_INCOMING = new ChatTypeImpl(Key.key("msg_command_incoming"));

  /**
   * A message sent as a result of using the {@code /msg} command.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  ChatType MSG_COMMAND_OUTGOING = new ChatTypeImpl(Key.key("msg_command_outgoing"));

  /**
   * A message received as a result of using the {@code /teammsg} command.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  ChatType TEAM_MSG_COMMAND_INCOMING = new ChatTypeImpl(Key.key("team_msg_command_incoming"));

  /**
   * A message sent as a result of using the {@code /teammsg} command.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  ChatType TEAM_MSG_COMMAND_OUTGOING = new ChatTypeImpl(Key.key("team_msg_command_outgoing"));

  /**
   * A message sent as a result of using the {@code /me} command.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  ChatType EMOTE_COMMAND = new ChatTypeImpl(Key.key("emote_command"));

  /**
   * Creates a new chat type with a given key.
   *
   * @param key the key
   * @return the chat type
   * @since 4.12.0
   */
  static @NotNull ChatType chatType(final @NotNull Keyed key) {
    return key instanceof ChatType ? (ChatType) key : new ChatTypeImpl(requireNonNull(key, "key").key());
  }

  /**
   * Creates a bound chat type with a name {@link Component}.
   *
   * @param name the name component
   * @return a new bound chat type
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(value = "_ -> new", pure = true)
  default ChatType.@NotNull Bound bind(final @NotNull ComponentLike name) {
    return this.bind(name, null);
  }

  /**
   * Creates a bound chat type with a name and target {@link Component}.
   *
   * @param name the name component
   * @param target the optional target component
   * @return a new bound chat type
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  @Contract(value = "_, _ -> new", pure = true)
  default ChatType.@NotNull Bound bind(final @NotNull ComponentLike name, final @Nullable ComponentLike target) {
    return new ChatTypeImpl.BoundImpl(this, requireNonNull(name.asComponent(), "name"), ComponentLike.unbox(target));
  }

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("key", this.key()));
  }

  /**
   * A bound {@link ChatType}.
   *
   * @since 4.12.0
   * @sinceMinecraft 1.19
   */
  interface Bound extends Examinable {

    /**
     * Gets the chat type.
     *
     * @return the chat type
     * @since 4.12.0
     * @sinceMinecraft 1.19
     */
    @Contract(pure = true)
    @NotNull ChatType type();

    /**
     * Get the name component.
     *
     * @return the name component
     * @since 4.12.0
     * @sinceMinecraft 1.19
     */
    @Contract(pure = true)
    @NotNull Component name();

    /**
     * Get the target component.
     *
     * @return the target component or null
     * @since 4.12.0
     * @sinceMinecraft 1.19
     */
    @Contract(pure = true)
    @Nullable Component target();

    @Override
    default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("type", this.type()),
        ExaminableProperty.of("name", this.name()),
        ExaminableProperty.of("target", this.target())
      );
    }
  }
}
