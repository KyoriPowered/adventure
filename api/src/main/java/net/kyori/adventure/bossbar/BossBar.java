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
package net.kyori.adventure.bossbar;

import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.NameMap;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A bossbar.
 */
public interface BossBar {
  /**
   * Creates a new bossbar.
   *
   * @param name the name
   * @param percent the percent, between 0 and 1
   * @param color the color
   * @param overlay the overlay
   * @return a bossbar
   */
  static @NonNull BossBar of(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay) {
    BossBarImpl.checkPercent(percent);
    return new BossBarImpl(name, percent, color, overlay);
  }

  /**
   * Creates a new bossbar.
   *
   * @param name the name
   * @param percent the percent, between 0 and 1
   * @param color the color
   * @param overlay the overlay
   * @param flags the flags
   * @return a bossbar
   */
  static @NonNull BossBar of(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay, final @NonNull Set<Flag> flags) {
    BossBarImpl.checkPercent(percent);
    return new BossBarImpl(name, percent, color, overlay, flags);
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @NonNull Component name();

  /**
   * Sets the name.
   *
   * @param name the name
   * @return the bossbar
   */
  @NonNull BossBar name(final @NonNull Component name);

  /**
   * Gets the percent.
   *
   * <p>The percent is a value between 0 and 1.</p>
   *
   * @return the percent
   */
  float percent();

  /**
   * Sets the percent.
   *
   * <p>The percent is a value between 0 and 1.</p>
   *
   * @param percent the percent
   * @return the bossbar
   */
  @NonNull BossBar percent(final float percent);

  /**
   * Gets the color.
   *
   * @return the color
   */
  @NonNull Color color();

  /**
   * Sets the color.
   *
   * @param color the color
   * @return the bossbar
   */
  @NonNull BossBar color(final @NonNull Color color);

  /**
   * Gets the overlay.
   *
   * @return the overlay
   */
  @NonNull Overlay overlay();

  /**
   * Sets the overlay.
   *
   * @param overlay the overlay
   * @return the bossbar
   */
  @NonNull BossBar overlay(final @NonNull Overlay overlay);

  /**
   * Gets the flags.
   *
   * @return the flags
   */
  @NonNull Set<Flag> flags();

  /**
   * Sets the flags.
   *
   * @param flags the flags
   * @return the bossbar
   */
  @NonNull BossBar flags(final @NonNull Set<Flag> flags);

  /**
   * Sets the flags.
   *
   * @param flags the flags
   * @return the bossbar
   */
  @NonNull BossBar addFlags(final @NonNull Flag@NonNull... flags);

  /**
   * Sets the flags.
   *
   * @param flags the flags
   * @return the bossbar
   */
  @NonNull BossBar removeFlags(final @NonNull Flag@NonNull... flags);

  @NonNull BossBar addListener(final @NonNull Listener listener);

  @NonNull BossBar removeListener(final @NonNull Listener listener);

  @FunctionalInterface
  interface Listener {
    void bossBarChanged(final @NonNull BossBar bar, final @NonNull Change change);

    /**
     * The type of change.
     */
    enum Change {
      NAME,
      PERCENT,
      COLOR,
      OVERLAY,
      FLAGS;
    }
  }

  enum Color {
    PINK("pink"),
    BLUE("blue"),
    RED("red"),
    GREEN("green"),
    YELLOW("yellow"),
    PURPLE("purple"),
    WHITE("white");

    public static final NameMap<Color> NAMES = NameMap.create(Color.class, color -> color.name);
    private final String name;

    Color(final String name) {
      this.name = name;
    }
  }

  enum Flag {
    /**
     * If the screen should be darkened.
     */
    DARKEN_SCREEN,
    /**
     * If boss music should be played.
     */
    PLAY_BOSS_MUSIC,
    /**
     * If world fog should be created.
     */
    CREATE_WORLD_FOG;
  }

  enum Overlay {
    PROGRESS("progress"),
    NOTCHED_6("notched_6"),
    NOTCHED_10("notched_10"),
    NOTCHED_12("notched_12"),
    NOTCHED_20("notched_20");

    public static final NameMap<Overlay> NAMES = NameMap.create(Overlay.class, overlay -> overlay.name);
    private final String name;

    Overlay(final String name) {
      this.name = name;
    }
  }
}
