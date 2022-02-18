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
package net.kyori.adventure.animation.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;

/**
 * An object that makes bossbar change to another state.
 *
 * @since 1.10.0
 */
public interface BossBarState extends Examinable {

  /**
   * Value of progress that makes new progress does not apply to bossbar.
   */
  float DO_NOT_APPLY_PROGRESS = -1f;

  /**
   * Creates new bossbar state.
   *
   * <p>Null or {@link BossBarState#DO_NOT_APPLY_PROGRESS} values makes they do not apply to bossbar.</p>
   *
   * @param name name
   * @param progress progress
   * @param color color
   * @param overlay overlay
   * @param flags flags collection
   * @return created state
   *
   * @since 1.10.0
   */
  static BossBarState state(Component name, float progress, BossBar.Color color, BossBar.Overlay overlay, Collection<BossBar.Flag> flags) {
    if ((progress < 0 || progress > 1) && progress != DO_NOT_APPLY_PROGRESS)
      throw new IllegalArgumentException("Progress must be 0-1 float (inclusive) or DO_NOT_APPLY_PROGRESS value");
    return new BossBarStateImpl(name, progress, color, overlay, flags);
  }

  /**
   * Creates new bossbar state.
   *
   * <p>Null values makes they do not apply to bossbar.</p>
   *
   * @param name name
   * @param progress progress
   * @param color color
   * @param overlay overlay
   * @param flags flags array
   * @return created state
   *
   * @since 1.10.0
   */
  static BossBarState state(Component name, float progress, BossBar.Color color, BossBar.Overlay overlay, BossBar.Flag... flags) {
    return state(name, progress, color, overlay, Arrays.asList(flags));
  }

  /**
   * Captures state from boss bar.
   *
   * @param bossBar a boss bar to capture from
   * @return new state instance from boss bar
   *
   * @since 1.10.0
   */
  static BossBarState state(BossBar bossBar) {
    return state(bossBar.name(), bossBar.progress(), bossBar.color(), bossBar.overlay(), bossBar.flags());
  }

  /**
   * Copies flags collection.
   *
   * @param flags original collection
   * @return copied collection
   *
   * @since 1.10.0
   */
  static Set<BossBar.Flag> copyFlags(final Collection<BossBar.Flag> flags) {
    if (flags == null) {
      return null;
    } else {
      return Collections.unmodifiableSet(new HashSet<>(flags));
    }
  }

  /**
   * Gets the name.
   *
   * @return name or null if not applies
   *
   * @since 1.10.0
   */
  Component name();

  /**
   * Sets the name.
   *
   * @param name name or null to not apply
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarState name(Component name);

  /**
   * Gets the progress.
   *
   * @return progress or {@link BossBarState#DO_NOT_APPLY_PROGRESS} if not applies
   *
   * @since 1.10.0
   */
  float progress();

  /**
   * Sets the progress.
   *
   * @param progress progress or {@link BossBarState#DO_NOT_APPLY_PROGRESS} to not apply
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarState progress(float progress);

  /**
   * Gets the color.
   *
   * @return color or null if not applies
   *
   * @since 1.10.0
   */
  BossBar.Color color();

  /**
   * Sets the color.
   *
   * @param color color or null to not apply
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarState color(BossBar.Color color);

  /**
   * Gets the overlay.
   *
   * @return overlay or null if not applies
   *
   * @since 1.10.0
   */
  BossBar.Overlay overlay();

  /**
   * Sets the overlay.
   *
   * @param overlay overlay or null to not apply
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarState overlay(BossBar.Overlay overlay);

  /**
   * Gets flags.
   *
   * @return flag collection or null if not applies
   *
   * @since 1.10.0
   */
  Set<BossBar.Flag> flags();

  /**
   * Sets flags.
   *
   * @param flags flag collection or null to not apply
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarState flags(Collection<BossBar.Flag> flags);

  /**
   * Sets flags.
   *
   * @param flags flag array
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  default BossBarState flags(BossBar.Flag... flags) {
    return this.flags(Arrays.asList(flags));
  }

  /**
   * Applies this state to existing boss bar.
   *
   * @param bossBar boss bar to apply state to
   * @return this instance
   *
   * @since 1.10.0
   */
  default BossBarState apply(BossBar bossBar) {
    if (this.name() != null)
      bossBar.name(this.name());
    if (this.color() != null)
      bossBar.color(this.color());
    if (this.progress() >= 0 && this.progress() <= 1)
      bossBar.progress(this.progress());
    if (this.overlay() != null)
      bossBar.overlay(this.overlay());
    if (this.flags() != null)
      bossBar.flags(this.flags());
    return this;
  }

  /**
   * Creates new boss bar instance from this state when all properties all applicable.
   *
   * @return created boss bar
   * @throws IllegalStateException when some values are not applicable (null or {@link BossBarState#DO_NOT_APPLY_PROGRESS})
   *
   * @since 1.10.0
   */
  default BossBar asBossBar() {
    try {
      return BossBar.bossBar(this.name(), this.progress(), this.color(), this.overlay(), this.flags());
    } catch (NullPointerException e) {
      throw new IllegalStateException("Some values are not applicable.", e);
    }
  }

}
