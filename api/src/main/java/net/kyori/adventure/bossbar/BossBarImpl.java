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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Listenable;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

final class BossBarImpl extends Listenable<BossBar.Listener> implements BossBar, Examinable {
  private static final BiConsumer<BossBarImpl, Set<Flag>> FLAGS_ADDED = (bar, flagsAdded) -> bar.forEachListener(listener -> listener.bossBarFlagsChanged(bar, flagsAdded, Collections.emptySet()));
  private static final BiConsumer<BossBarImpl, Set<Flag>> FLAGS_REMOVED = (bar, flagsRemoved) -> bar.forEachListener(listener -> listener.bossBarFlagsChanged(bar, Collections.emptySet(), flagsRemoved));
  private Component name;
  private float percent;
  private Color color;
  private Overlay overlay;
  private final Set<Flag> flags = EnumSet.noneOf(Flag.class);

  BossBarImpl(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay) {
    this.name = requireNonNull(name, "name");
    this.percent = percent;
    this.color = requireNonNull(color, "color");
    this.overlay = requireNonNull(overlay, "overlay");
  }

  BossBarImpl(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay, final @NonNull Set<Flag> flags) {
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

  static void checkPercent(final float percent) {
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
    if(newFlags.isEmpty()) {
      final Set<Flag> oldFlags = EnumSet.copyOf(this.flags);
      this.flags.clear();
      this.forEachListener(listener -> listener.bossBarFlagsChanged(this, Collections.emptySet(), oldFlags));
    } else if(!this.flags.equals(newFlags)) {
      final Set<Flag> oldFlags = EnumSet.copyOf(this.flags);
      this.flags.clear();
      this.flags.addAll(newFlags);
      final Set<BossBar.Flag> added = EnumSet.copyOf(newFlags);
      added.removeIf(oldFlags::contains);
      final Set<BossBar.Flag> removed = EnumSet.copyOf(oldFlags);
      removed.removeIf(this.flags::contains);
      this.forEachListener(listener -> listener.bossBarFlagsChanged(this, added, removed));
    }
    return this;
  }

  @Override
  public boolean hasFlag(final @NonNull Flag flag) {
    return this.flags.contains(flag);
  }

  @Override
  public @NonNull BossBar addFlag(final @NonNull Flag flag) {
    return this.editFlags(flag, Set::add, FLAGS_ADDED);
  }

  @Override
  public @NonNull BossBar removeFlag(final @NonNull Flag flag) {
    return this.editFlags(flag, Set::remove, FLAGS_REMOVED);
  }

  private @NonNull BossBar editFlags(final @NonNull Flag flag, final @NonNull BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
    if(predicate.test(this.flags, flag)) {
      onChange.accept(this, Collections.singleton(flag));
    }
    return this;
  }

  @Override
  public @NonNull BossBar addFlags(final @NonNull Flag@NonNull... flags) {
    return this.editFlags(flags, Set::add, FLAGS_ADDED);
  }

  @Override
  public @NonNull BossBar removeFlags(final @NonNull Flag@NonNull... flags) {
    return this.editFlags(flags, Set::remove, FLAGS_REMOVED);
  }

  private @NonNull BossBar editFlags(final Flag[] flags, final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
    if(flags.length == 0) return this;
    Set<Flag> changes = null;
    for(int i = 0, length = flags.length; i < length; i++) {
      if(predicate.test(this.flags, flags[i])) {
        if(changes == null) {
          changes = EnumSet.noneOf(Flag.class);
        }
        changes.add(flags[i]);
      }
    }
    if(changes != null) {
      onChange.accept(this, changes);
    }
    return this;
  }

  @Override
  public @NonNull BossBar addFlags(final @NonNull Iterable<Flag> flags) {
    return this.editFlags(flags, Set::add, FLAGS_ADDED);
  }

  @Override
  public @NonNull BossBar removeFlags(final @NonNull Iterable<Flag> flags) {
    return this.editFlags(flags, Set::remove, FLAGS_REMOVED);
  }

  private @NonNull BossBar editFlags(final Iterable<Flag> flags, final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
    Set<Flag> changes = null;
    for(final Flag flag : flags) {
      if(predicate.test(this.flags, flag)) {
        if(changes == null) {
          changes = EnumSet.noneOf(Flag.class);
        }
        changes.add(flag);
      }
    }
    if(changes != null) {
      onChange.accept(this, changes);
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
