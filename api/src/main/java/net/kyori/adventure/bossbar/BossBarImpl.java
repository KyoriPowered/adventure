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
package net.kyori.adventure.bossbar;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Services;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("deprecation")
final class BossBarImpl extends HackyBossBarPlatformBridge implements BossBar {
  private final List<Listener> listeners = new CopyOnWriteArrayList<>();
  private Component name;
  private float progress;
  private Color color;
  private Overlay overlay;
  private final Set<Flag> flags = EnumSet.noneOf(Flag.class);
  @Nullable BossBarImplementation implementation;

  @ApiStatus.Internal
  static final class ImplementationAccessor {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static final Optional<BossBarImplementation.Provider> SERVICE = Services.service(BossBarImplementation.Provider.class);

    private ImplementationAccessor() {
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    static @NotNull <I extends BossBarImplementation> I get(final @NotNull BossBar bar, final @NotNull Class<I> type) {
      @Nullable BossBarImplementation implementation = ((BossBarImpl) bar).implementation;
      if (implementation == null) {
        implementation = SERVICE.get().create(bar);
        ((BossBarImpl) bar).implementation = implementation;
      }
      return type.cast(implementation);
    }
  }

  BossBarImpl(final @NotNull Component name, final float progress, final @NotNull Color color, final @NotNull Overlay overlay) {
    this.name = requireNonNull(name, "name");
    this.progress = progress;
    this.color = requireNonNull(color, "color");
    this.overlay = requireNonNull(overlay, "overlay");
  }

  BossBarImpl(final @NotNull Component name, final float progress, final @NotNull Color color, final @NotNull Overlay overlay, final @NotNull Set<Flag> flags) {
    this(name, progress, color, overlay);
    this.flags.addAll(flags);
  }

  @Override
  public @NotNull Component name() {
    return this.name;
  }

  @Override
  public @NotNull BossBar name(final @NotNull Component newName) {
    requireNonNull(newName, "name");
    final Component oldName = this.name;
    if (!Objects.equals(newName, oldName)) {
      this.name = newName;
      this.forEachListener(listener -> listener.bossBarNameChanged(this, oldName, newName));
    }
    return this;
  }

  @Override
  public float progress() {
    return this.progress;
  }

  @Override
  public @NotNull BossBar progress(final float newProgress) {
    checkProgress(newProgress);
    final float oldProgress = this.progress;
    if (newProgress != oldProgress) {
      this.progress = newProgress;
      this.forEachListener(listener -> listener.bossBarProgressChanged(this, oldProgress, newProgress));
    }
    return this;
  }

  static void checkProgress(final float progress) {
    if (progress < MIN_PROGRESS || progress > MAX_PROGRESS) {
      throw new IllegalArgumentException("progress must be between " + MIN_PROGRESS + " and " + MAX_PROGRESS + ", was " + progress);
    }
  }

  @Override
  public @NotNull Color color() {
    return this.color;
  }

  @Override
  public @NotNull BossBar color(final @NotNull Color newColor) {
    requireNonNull(newColor, "color");
    final Color oldColor = this.color;
    if (newColor != oldColor) {
      this.color = newColor;
      this.forEachListener(listener -> listener.bossBarColorChanged(this, oldColor, newColor));
    }
    return this;
  }

  @Override
  public @NotNull Overlay overlay() {
    return this.overlay;
  }

  @Override
  public @NotNull BossBar overlay(final @NotNull Overlay newOverlay) {
    requireNonNull(newOverlay, "overlay");
    final Overlay oldOverlay = this.overlay;
    if (newOverlay != oldOverlay) {
      this.overlay = newOverlay;
      this.forEachListener(listener -> listener.bossBarOverlayChanged(this, oldOverlay, newOverlay));
    }
    return this;
  }

  @Override
  public @NotNull Set<Flag> flags() {
    return Collections.unmodifiableSet(this.flags);
  }

  @Override
  public @NotNull BossBar flags(final @NotNull Set<Flag> newFlags) {
    if (newFlags.isEmpty()) {
      final Set<Flag> oldFlags = EnumSet.copyOf(this.flags);
      this.flags.clear();
      this.forEachListener(listener -> listener.bossBarFlagsChanged(this, Collections.emptySet(), oldFlags));
    } else if (!this.flags.equals(newFlags)) {
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
  public boolean hasFlag(final @NotNull Flag flag) {
    return this.flags.contains(flag);
  }

  @Override
  public @NotNull BossBar addFlag(final @NotNull Flag flag) {
    return this.editFlags(flag, Set::add, BossBarImpl::onFlagsAdded);
  }

  @Override
  public @NotNull BossBar removeFlag(final @NotNull Flag flag) {
    return this.editFlags(flag, Set::remove, BossBarImpl::onFlagsRemoved);
  }

  private @NotNull BossBar editFlags(final @NotNull Flag flag, final @NotNull BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
    if (predicate.test(this.flags, flag)) {
      onChange.accept(this, Collections.singleton(flag));
    }
    return this;
  }

  @Override
  public @NotNull BossBar addFlags(final @NotNull Flag@NotNull... flags) {
    return this.editFlags(flags, Set::add, BossBarImpl::onFlagsAdded);
  }

  @Override
  public @NotNull BossBar removeFlags(final @NotNull Flag@NotNull... flags) {
    return this.editFlags(flags, Set::remove, BossBarImpl::onFlagsRemoved);
  }

  private @NotNull BossBar editFlags(final Flag[] flags, final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
    if (flags.length == 0) return this;
    Set<Flag> changes = null;
    for (int i = 0, length = flags.length; i < length; i++) {
      if (predicate.test(this.flags, flags[i])) {
        if (changes == null) {
          changes = EnumSet.noneOf(Flag.class);
        }
        changes.add(flags[i]);
      }
    }
    if (changes != null) {
      onChange.accept(this, changes);
    }
    return this;
  }

  @Override
  public @NotNull BossBar addFlags(final @NotNull Iterable<Flag> flags) {
    return this.editFlags(flags, Set::add, BossBarImpl::onFlagsAdded);
  }

  @Override
  public @NotNull BossBar removeFlags(final @NotNull Iterable<Flag> flags) {
    return this.editFlags(flags, Set::remove, BossBarImpl::onFlagsRemoved);
  }

  private @NotNull BossBar editFlags(final Iterable<Flag> flags, final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
    Set<Flag> changes = null;
    for (final Flag flag : flags) {
      if (predicate.test(this.flags, flag)) {
        if (changes == null) {
          changes = EnumSet.noneOf(Flag.class);
        }
        changes.add(flag);
      }
    }
    if (changes != null) {
      onChange.accept(this, changes);
    }
    return this;
  }

  @Override
  public @NotNull BossBar addListener(final @NotNull Listener listener) {
    this.listeners.add(listener);
    return this;
  }

  @Override
  public @NotNull BossBar removeListener(final @NotNull Listener listener) {
    this.listeners.remove(listener);
    return this;
  }

  @Override
  public @NotNull Iterable<? extends BossBarViewer> viewers() {
    if (this.implementation != null) {
      return this.implementation.viewers();
    }
    return Collections.emptyList();
  }

  private void forEachListener(final @NotNull Consumer<Listener> consumer) {
    for (final Listener listener : this.listeners) {
      consumer.accept(listener);
    }
  }

  private static void onFlagsAdded(final BossBarImpl bar, final Set<Flag> flagsAdded) {
    bar.forEachListener(listener -> listener.bossBarFlagsChanged(bar, flagsAdded, Collections.emptySet()));
  }

  private static void onFlagsRemoved(final BossBarImpl bar, final Set<Flag> flagsRemoved) {
    bar.forEachListener(listener -> listener.bossBarFlagsChanged(bar, Collections.emptySet(), flagsRemoved));
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("name", this.name),
      ExaminableProperty.of("progress", this.progress),
      ExaminableProperty.of("color", this.color),
      ExaminableProperty.of("overlay", this.overlay),
      ExaminableProperty.of("flags", this.flags)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }
}
