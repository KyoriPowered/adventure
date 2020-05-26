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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

final class BossBarImpl implements BossBar, Examinable {
  private static final float MINIMUM_PERCENT_CHANGE = 0.01f;
  private Component name;
  private float percent;
  private volatile float lastSentPercent;
  private Color color;
  private Overlay overlay;
  private final Set<Flag> flags = EnumSet.noneOf(Flag.class);
  private List<Listener> listeners = null;

  protected BossBarImpl(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay) {
    this.name = requireNonNull(name, "name");
    this.percent = percent;
    this.lastSentPercent = percent;
    this.color = requireNonNull(color, "color");
    this.overlay = requireNonNull(overlay, "overlay");
  }

  @Override
  public @NonNull Component name() {
    return this.name;
  }

  @Override
  public @NonNull BossBar name(final @NonNull Component name) {
    if(!Objects.equals(this.name, name)) {
      this.name = requireNonNull(name, "name");
      this.changed(Listener.Change.NAME);
    }
    return this;
  }

  @Override
  public float percent() {
    return this.percent;
  }

  @Override
  public @NonNull BossBar percent(final float percent) {
    if(percent != this.percent) {
      final boolean enoughForClientToNotice = enoughForClientToNotice(this.lastSentPercent, percent);
      this.percent = percent;
      if(enoughForClientToNotice) {
        this.lastSentPercent = percent;
        this.changed(Listener.Change.PERCENT);
      }
    }
    return this;
  }

  // https://github.com/KyoriPowered/text/pull/62#discussion_r410790072
  private static boolean enoughForClientToNotice(final float oldValue, final float newValue) {
    return Math.abs(newValue - oldValue) >= MINIMUM_PERCENT_CHANGE;
  }

  @Override
  public @NonNull Color color() {
    return this.color;
  }

  @Override
  public @NonNull BossBar color(final @NonNull Color color) {
    if(color != this.color) {
      this.color = requireNonNull(color, "color");
      this.changed(Listener.Change.COLOR);
    }
    return this;
  }

  @Override
  public @NonNull Overlay overlay() {
    return this.overlay;
  }

  @Override
  public @NonNull BossBar overlay(final @NonNull Overlay overlay) {
    if(overlay != this.overlay) {
      this.overlay = requireNonNull(overlay, "overlay");
      this.changed(Listener.Change.OVERLAY);
    }
    return this;
  }

  @Override
  public @NonNull Set<Flag> flags() {
    return this.flags;
  }

  @Override
  public @NonNull BossBar flags(final @NonNull Set<Flag> flags) {
    this.flags.clear();
    this.flags.addAll(flags);
    return this;
  }

  @Override
  public @NonNull BossBar addFlags(final @NonNull Flag@NonNull... flags) {
    if(this.editFlags(Set::add, flags)) {
      this.changed(Listener.Change.FLAGS);
    }
    return this;
  }

  @Override
  public @NonNull BossBar removeFlags(final @NonNull Flag@NonNull... flags) {
    if(this.editFlags(Set::remove, flags)) {
      this.changed(Listener.Change.FLAGS);
    }
    return this;
  }

  private boolean editFlags(final @NonNull BiPredicate<Set<Flag>, Flag> predicate, final @NonNull Flag@NonNull... flags) {
    boolean changed = false;
    for(int i = 0, length = flags.length; i < length; i++) {
      if(predicate.test(this.flags, flags[i])) {
        changed = true;
      }
    }
    return changed;
  }

  @Override
  public @NonNull BossBar addListener(final @NonNull Listener listener) {
    if(this.listeners == null) {
      this.listeners = new ArrayList<>();
    }
    this.listeners.add(listener);
    return this;
  }

  @Override
  public @NonNull BossBar removeListener(final @NonNull Listener listener) {
    if(this.listeners != null) {
      this.listeners.remove(listener);
    }
    return this;
  }

  protected void changed(final Listener.@NonNull Change change) {
    final List<Listener> listeners = this.listeners;
    if(listeners != null) {
      for(int i = 0, size = listeners.size(); i < size; i++) {
        final Listener listener = listeners.get(i);
        listener.bossBarChanged(this, change);
      }
    }
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
}
