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
package net.kyori.adventure.identity;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import net.kyori.adventure.Adventure;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An identity for a specific player.
 *
 * @since 4.12.0
 */
public interface PlayerIdentity extends Examinable, Identity {
  /**
   * A pointer to a team name.
   *
   * @since 4.12.0
   */
  Pointer<Component> TEAM_NAME = Pointer.pointer(Component.class, Key.key(Adventure.NAMESPACE, "team_name"));

  /**
   * Creates a new player identity.
   *
   * @param uuid the uuid
   * @param name the name
   * @return the player identity
   * @since 4.12.0
   */
  static @NotNull PlayerIdentity playerIdentity(final @NotNull UUID uuid, final @NotNull Component name) {
    return playerIdentity(uuid, name, null);
  }

  /**
   * Creates a new player identity.
   *
   * @param uuid the uuid
   * @param name the name
   * @param teamName the optional team name
   * @return the player identity
   * @since 4.12.0
   */
  static @NotNull PlayerIdentity playerIdentity(final @NotNull UUID uuid, final @NotNull Component name, final @Nullable Component teamName) {
    return new PlayerIdentityImpl(Objects.requireNonNull(uuid, "uuid"), Objects.requireNonNull(name, "name"), teamName);
  }

  /**
   * The name of the player.
   *
   * @return the name
   * @since 4.12.0
   */
  @Contract(pure = true)
  @NotNull Component name();

  /**
   * The team name of the player.
   *
   * @return the team name
   * @since 4.12.0
   */
  @Contract(pure = true)
  @Nullable Component teamName();

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("uuid", this.uuid()),
      ExaminableProperty.of("name", this.name()),
      ExaminableProperty.of("teamName", this.teamName())
    );
  }
}
