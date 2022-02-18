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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

class BossBarStateImpl implements BossBarState {

  private final Component name;

  private final float progress;

  private final BossBar.Overlay overlay;

  private final Set<BossBar.Flag> flags;

  private final BossBar.Color color;

  BossBarStateImpl(final Component name, final float progress, final BossBar.Color color, final BossBar.Overlay overlay, final Collection<BossBar.Flag> flags) {
    this.name = name;
    this.progress = progress;
    this.color = color;
    this.overlay = overlay;
    if (flags == null)
      this.flags = null;
    else
      this.flags = new HashSet<>(flags);
  }

  @Override
  public Component name() {
    return this.name;
  }

  @Override
  public BossBarState name(final Component name) {
    return new BossBarStateImpl(name, this.progress, this.color, this.overlay, this.flags);
  }

  @Override
  public float progress() {
    return this.progress;
  }

  @Override
  public BossBarState progress(final float progress) {
    if ((progress < 0 || progress > 1) && progress != DO_NOT_APPLY_PROGRESS)
      throw new IllegalArgumentException("Progress must be 0-1 float (inclusive) or DO_NOT_APPLY_PROGRESS value");
    return new BossBarStateImpl(this.name, progress, this.color, this.overlay, this.flags);
  }

  @Override
  public BossBar.Color color() {
    return this.color;
  }

  @Override
  public BossBarState color(final BossBar.Color color) {
    return new BossBarStateImpl(this.name, this.progress, color, this.overlay, this.flags);
  }

  @Override
  public BossBar.Overlay overlay() {
    return this.overlay;
  }

  @Override
  public BossBarState overlay(final BossBar.Overlay overlay) {
    return new BossBarStateImpl(this.name, this.progress, this.color, overlay, this.flags);
  }

  @Override
  public Set<BossBar.Flag> flags() {
    return this.flags;
  }

  @Override
  public BossBarState flags(final Collection<BossBar.Flag> flags) {
    return new BossBarStateImpl(this.name, this.progress, this.color, this.overlay, flags);
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
    return this.examine(StringExaminer.simpleEscaping());
  }
}
