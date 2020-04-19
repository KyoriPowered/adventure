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

import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

public abstract class AbstractBossBar implements BossBar, Examinable {
  private Component name;
  private float percent;
  private Color color;
  private Overlay overlay;
  private boolean darkenScreen;
  private boolean playBossMusic;
  private boolean createWorldFog;

  protected AbstractBossBar(final @NonNull Component name, final float percent, final @NonNull Color color, final @NonNull Overlay overlay) {
    this.name = requireNonNull(name, "name");
    this.percent = percent;
    this.color = requireNonNull(color, "color");
    this.overlay = requireNonNull(overlay, "overlay");
  }

  @Override
  public @NonNull Component name() {
    return this.name;
  }

  @Override
  public @NonNull BossBar name(final @NonNull Component name) {
    if(name != this.name) {
      this.name = requireNonNull(name, "name");
      this.changed(Change.NAME);
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
      this.percent = percent;
      this.changed(Change.PERCENT);
    }
    return this;
  }

  @Override
  public @NonNull Color color() {
    return this.color;
  }

  @Override
  public @NonNull BossBar color(final @NonNull Color color) {
    if(color != this.color) {
      this.color = requireNonNull(color, "color");
      this.changed(Change.COLOR);
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
      this.changed(Change.OVERLAY);
    }
    return this;
  }

  @Override
  public boolean darkenScreen() {
    return this.darkenScreen;
  }

  @Override
  public @NonNull BossBar darkenScreen(final boolean darkenScreen) {
    if(darkenScreen != this.darkenScreen) {
      this.darkenScreen = darkenScreen;
      this.changed(Change.FLAG);
    }
    return this;
  }

  @Override
  public boolean playBossMusic() {
    return this.playBossMusic;
  }

  @Override
  public @NonNull BossBar playBossMusic(final boolean playBossMusic) {
    if(playBossMusic != this.playBossMusic) {
      this.playBossMusic = playBossMusic;
      this.changed(Change.FLAG);
    }
    return this;
  }

  @Override
  public boolean createWorldFog() {
    return this.createWorldFog;
  }

  @Override
  public @NonNull BossBar createWorldFog(final boolean createWorldFog) {
    if(createWorldFog != this.createWorldFog) {
      this.createWorldFog = createWorldFog;
      this.changed(Change.FLAG);
    }
    return this;
  }

  protected abstract void changed(final Change type);

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("name", this.name),
      ExaminableProperty.of("percent", this.percent),
      ExaminableProperty.of("color", this.color),
      ExaminableProperty.of("overlay", this.overlay),
      ExaminableProperty.of("darkenScreen", this.darkenScreen),
      ExaminableProperty.of("playBossMusic", this.playBossMusic),
      ExaminableProperty.of("createWorldFog", this.createWorldFog)
    );
  }

  /**
   * The type of change.
   */
  protected enum Change {
    NAME,
    PERCENT,
    COLOR,
    OVERLAY,
    FLAG;
  }
}
