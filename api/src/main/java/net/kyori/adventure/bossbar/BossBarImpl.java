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

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Listenable;
import net.kyori.adventure.util.ShadyPines;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/* package */ final class BossBarImpl extends Listenable<BossBar.Listener> implements BossBar, Examinable {
  private Component name;
  private float percent;
  private Color color;
  private Overlay overlay;
  private final Set<Flag> flags = EnumSet.noneOf(Flag.class);

  /* package */ BossBarImpl(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay) {
    this.name = requireNonNull(name, "name");
    this.percent = percent;
    this.color = requireNonNull(color, "color");
    this.overlay = requireNonNull(overlay, "overlay");
  }

  /* package */ BossBarImpl(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay, final @NonNull Set<Flag> flags) {
    this(name, percent, color, overlay);
    this.flags.addAll(flags);
  }

  @Override
  public @NonNull Component name() {
    return this.name;
  }

  @Override
  public @NonNull BossBar name(final @NonNull Component newName) {
    requireNonNull(newName, "name");
    final Component oldName = this.name;
    if(!Objects.equals(newName, oldName)) {
      this.name = newName;
      this.forEachListener(listener -> listener.bossBarNameChanged(this, oldName, newName));
    }
    return this;
  }

  @Override
  public float percent() {
    return this.percent;
  }

  @Override
  public @NonNull BossBar percent(final float newPercent) {
    checkPercent(newPercent);
    final float oldPercent = this.percent;
    if(newPercent != oldPercent) {
      this.percent = newPercent;
      this.forEachListener(listener -> listener.bossBarPercentChanged(this, oldPercent, newPercent));
    }
    return this;
  }

  /* package */ static void checkPercent(final float percent) {
    if(percent < MIN_PERCENT || percent > MAX_PERCENT) {
      throw new IllegalArgumentException("percent must be between " + MIN_PERCENT + " and " + MAX_PERCENT + ", was " + percent);
    }
  }

  @Override
  public @NonNull Color color() {
    return this.color;
  }

  @Override
  public @NonNull BossBar color(final @NonNull Color newColor) {
    requireNonNull(newColor, "color");
    final Color oldColor = this.color;
    if(newColor != oldColor) {
      this.color = newColor;
      this.forEachListener(listener -> listener.bossBarColorChanged(this, oldColor, newColor));
    }
    return this;
  }

  @Override
  public @NonNull Overlay overlay() {
    return this.overlay;
  }

  @Override
  public @NonNull BossBar overlay(final @NonNull Overlay newOverlay) {
    requireNonNull(newOverlay, "overlay");
    final Overlay oldOverlay = this.overlay;
    if(newOverlay != oldOverlay) {
      this.overlay = newOverlay;
      this.forEachListener(listener -> listener.bossBarOverlayChanged(this, oldOverlay, newOverlay));
    }
    return this;
  }

  @Override
  public @NonNull Set<Flag> flags() {
    return this.flags;
  }

  @Override
  public @NonNull BossBar flags(final @NonNull Set<Flag> newFlags) {
    if(!this.flags.equals(newFlags)) {
      final Set<Flag> oldFlags = new HashSet<>(this.flags);
      this.flags.clear();
      this.flags.addAll(newFlags);
      this.forEachListener(listener -> listener.bossBarFlagsChanged(this, oldFlags, this.flags));
    }
    return this;
  }

  @Override
  public @NonNull BossBar addFlags(final @NonNull Flag@NonNull... flags) {
    return this.editFlags(flags, Set::add);
  }

  @Override
  public @NonNull BossBar removeFlags(final @NonNull Flag@NonNull... flags) {
    return this.editFlags(flags, Set::remove);
  }

  private @NonNull BossBar editFlags(final @NonNull Flag@NonNull[] flags, final @NonNull BiPredicate<Set<Flag>, Flag> predicate) {
    if(flags.length == 0) return this;
    final Set<Flag> oldFlags = new HashSet<>(this.flags);
    boolean changed = false;
    for(int i = 0, length = flags.length; i < length; i++) {
      if(predicate.test(this.flags, flags[i])) {
        changed = true;
      }
    }
    if(changed) {
      this.forEachListener(listener -> listener.bossBarFlagsChanged(this, oldFlags, this.flags));
    }
    return this;
  }

  @Override
  public @NonNull BossBar addListener(final @NonNull Listener listener) {
    this.addListener0(listener);
    return this;
  }

  @Override
  public @NonNull BossBar removeListener(final @NonNull Listener listener) {
    this.removeListener0(listener);
    return this;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final BossBarImpl that = (BossBarImpl) other;
    return this.name.equals(that.name)
      && ShadyPines.equals(this.percent, that.percent)
      && this.color == that.color
      && this.overlay == that.overlay
      && this.flags.equals(that.flags);
  }

  @Override
  public int hashCode() {
    int result = this.name.hashCode();
    result = (31 * result) + Float.hashCode(this.percent);
    result = (31 * result) + this.color.hashCode();
    result = (31 * result) + this.overlay.hashCode();
    result = (31 * result) + this.flags.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("name", this.name),
      ExaminableProperty.of("percent", this.percent),
      ExaminableProperty.of("color", this.color),
      ExaminableProperty.of("overlay", this.overlay),
      ExaminableProperty.of("flags", this.flags)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }
}
