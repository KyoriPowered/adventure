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
import net.kyori.adventure.util.Index;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A bossbar.
 *
 * @since 4.0.0
 */
public interface BossBar {
  /**
   * The minimum value the percent can be.
   *
   * @since 4.0.0
   */
  float MIN_PERCENT = 0f;
  /**
   * The maximum value the percent can be.
   *
   * @since 4.0.0
   */
  float MAX_PERCENT = 1f;

  /**
   * Creates a new bossbar.
   *
   * @param name the name
   * @param percent the percent, between 0 and 1
   * @param color the color
   * @param overlay the overlay
   * @return a bossbar
   * @throws IllegalArgumentException if percent is less than 0 or greater than 1
   * @since 4.0.0
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
   * @throws IllegalArgumentException if percent is less than 0 or greater than 1
   * @since 4.0.0
   */
  static @NonNull BossBar of(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay, final @NonNull Set<Flag> flags) {
    BossBarImpl.checkPercent(percent);
    return new BossBarImpl(name, percent, color, overlay, flags);
  }

  /**
   * Gets the name.
   *
   * @return the name
   * @since 4.0.0
   */
  @NonNull Component name();

  /**
   * Sets the name.
   *
   * @param name the name
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar name(final @NonNull Component name);

  /**
   * Gets the percent.
   *
   * <p>The percent is a value between 0 and 1.</p>
   *
   * @return the percent
   * @since 4.0.0
   */
  float percent();

  /**
   * Sets the percent.
   *
   * <p>The percent is a value between 0 and 1.</p>
   *
   * @param percent the percent
   * @return the bossbar
   * @throws IllegalArgumentException if percent is less than 0 or greater than 1
   * @since 4.0.0
   */
  @NonNull BossBar percent(final float percent);

  /**
   * Gets the color.
   *
   * @return the color
   * @since 4.0.0
   */
  @NonNull Color color();

  /**
   * Sets the color.
   *
   * @param color the color
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar color(final @NonNull Color color);

  /**
   * Gets the overlay.
   *
   * @return the overlay
   * @since 4.0.0
   */
  @NonNull Overlay overlay();

  /**
   * Sets the overlay.
   *
   * @param overlay the overlay
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar overlay(final @NonNull Overlay overlay);

  /**
   * Gets the flags.
   *
   * @return the flags
   * @since 4.0.0
   */
  @NonNull Set<Flag> flags();

  /**
   * Sets the flags.
   *
   * @param flags the flags
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar flags(final @NonNull Set<Flag> flags);

  /**
   * Checks if this bossbar has a flag.
   *
   * @param flag the flag
   * @return {@code true} if this bossbar has the flag, {@code false} otherwise
   * @since 4.0.0
   */
  boolean hasFlag(final @NonNull Flag flag);

  /**
   * Adds a flag to this bossbar.
   *
   * @param flag the flag
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar addFlag(final @NonNull Flag flag);

  /**
   * Removes a flag from this bossbar.
   *
   * @param flag the flag
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar removeFlag(final @NonNull Flag flag);

  /**
   * Adds flags to this bossbar.
   *
   * @param flags the flags
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar addFlags(final @NonNull Flag@NonNull... flags);

  /**
   * Removes flags from this bossbar.
   *
   * @param flags the flags
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar removeFlags(final @NonNull Flag@NonNull... flags);

  /**
   * Adds flags to this bossbar.
   *
   * @param flags the flags
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar addFlags(final @NonNull Iterable<Flag> flags);

  /**
   * Removes flags from this bossbar.
   *
   * @param flags the flags
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar removeFlags(final @NonNull Iterable<Flag> flags);

  /**
   * Adds a listener.
   *
   * @param listener a listener
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar addListener(final @NonNull Listener listener);

  /**
   * Removes a listener.
   *
   * @param listener a listener
   * @return the bossbar
   * @since 4.0.0
   */
  @NonNull BossBar removeListener(final @NonNull Listener listener);

  /**
   * A listener for changes that happen on a {@link BossBar}.
   *
   * @since 4.0.0
   */
  interface Listener {
    /**
     * Bossbar name changed.
     *
     * @param bar the bossbar
     * @param oldName the old name
     * @param newName the new name
     * @since 4.0.0
     */
    default void bossBarNameChanged(final @NonNull BossBar bar, final @NonNull Component oldName, final @NonNull Component newName) {
    }

    /**
     * Bossbar percent changed.
     *
     * @param bar the bossbar
     * @param oldPercent the old percent
     * @param newPercent the new percent
     * @since 4.0.0
     */
    default void bossBarPercentChanged(final @NonNull BossBar bar, final float oldPercent, final float newPercent) {
    }

    /**
     * Bossbar color changed.
     *
     * @param bar the bossbar
     * @param oldColor the old color
     * @param newColor the new color
     * @since 4.0.0
     */
    default void bossBarColorChanged(final @NonNull BossBar bar, final @NonNull Color oldColor, final @NonNull Color newColor) {
    }

    /**
     * Bossbar overlay changed.
     *
     * @param bar the bossbar
     * @param oldOverlay the old overlay
     * @param newOverlay the new overlay
     * @since 4.0.0
     */
    default void bossBarOverlayChanged(final @NonNull BossBar bar, final @NonNull Overlay oldOverlay, final @NonNull Overlay newOverlay) {
    }

    /**
     * Bossbar flags changed.
     *
     * @param bar the bossbar
     * @param flagsAdded the flags added to the bossbar
     * @param flagsRemoved the flags removed from the bossbar
     * @since 4.0.0
     */
    default void bossBarFlagsChanged(final @NonNull BossBar bar, final @NonNull Set<Flag> flagsAdded, final @NonNull Set<Flag> flagsRemoved) {
    }
  }

  /**
   * One of the colors the bar component of a {@link BossBar}.
   *
   * <p>This color does <em>not</em> affect the color of the bar's name text.</p>
   *
   * <p>The exact color for each named value may vary slightly based on game version.</p>
   *
   * @since 4.0.0
   */
  enum Color {
    /**
     * Pink.
     *
     * @since 4.0.0
     */
    PINK("pink"),
    /**
     * Blue.
     *
     * @since 4.0.0
     */
    BLUE("blue"),
    /**
     * Red.
     *
     * @since 4.0.0
     */
    RED("red"),
    /**
     * Green.
     *
     * @since 4.0.0
     */
    GREEN("green"),
    /**
     * Yellow.
     *
     * @since 4.0.0
     */
    YELLOW("yellow"),
    /**
     * Purple.
     *
     * @since 4.0.0
     */
    PURPLE("purple"),
    /**
     * White.
     *
     * @since 4.0.0
     */
    WHITE("white");

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    public static final Index<String, Color> NAMES = Index.create(Color.class, color -> color.name);
    private final String name;

    Color(final String name) {
      this.name = name;
    }
  }

  /**
   * Flags to control toggleable effects of a boss bar.
   *
   * @since 4.0.0
   */
  enum Flag {
    /**
     * If the screen should be darkened.
     *
     * @since 4.0.0
     */
    DARKEN_SCREEN("darken_screen"),
    /**
     * If boss music should be played.
     *
     * @since 4.0.0
     */
    PLAY_BOSS_MUSIC("play_boss_music"),
    /**
     * If world fog should be created.
     *
     * @since 4.0.0
     */
    CREATE_WORLD_FOG("create_world_fog");

    /**
     * The name map.
     *
     * <p>These names are not "official", but we want to provide them to allow serializers to be consistent.</p>
     *
     * @since 4.0.0
     */
    public static final Index<String, Flag> NAMES = Index.create(Flag.class, flag -> flag.name);
    private final String name;

    Flag(final String name) {
      this.name = name;
    }
  }

  /**
   * An overlay on the bar component of a boss bar.
   *
   * @since 4.0.0
   */
  enum Overlay {
    /**
     * A progress bar.
     *
     * @since 4.0.0
     */
    PROGRESS("progress"),
    /**
     * A bar with {@code 6} notches.
     *
     * @since 4.0.0
     */
    NOTCHED_6("notched_6"),
    /**
     * A bar with {@code 10} notches.
     *
     * @since 4.0.0
     */
    NOTCHED_10("notched_10"),
    /**
     * A bar with {@code 12} notches.
     *
     * @since 4.0.0
     */
    NOTCHED_12("notched_12"),
    /**
     * A bar with {@code 20} notches.
     *
     * @since 4.0.0
     */
    NOTCHED_20("notched_20");

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    public static final Index<String, Overlay> NAMES = Index.create(Overlay.class, overlay -> overlay.name);
    private final String name;

    Overlay(final String name) {
      this.name = name;
    }
  }
}
