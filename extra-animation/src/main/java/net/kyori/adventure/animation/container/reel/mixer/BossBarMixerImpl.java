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
package net.kyori.adventure.animation.container.reel.mixer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.kyori.adventure.animation.container.reel.ComponentReel;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.GenericFrameReel;
import net.kyori.adventure.animation.container.reel.generator.FrameReelGenerator;
import net.kyori.adventure.animation.container.reel.generator.FrameReelLike;
import net.kyori.adventure.animation.container.reel.generator.InflexibleFrameReelLike;
import net.kyori.adventure.animation.util.BossBarState;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

class BossBarMixerImpl implements BossBarMixer {

  private final ComponentReel nameReel;

  private final FrameReel<Float> progressReel;

  private final FrameReel<BossBar.Color> colorReel;

  private final FrameReel<BossBar.Overlay> overlayReel;

  private final FrameReel<Set<BossBar.Flag>> flagsReel;

  <C extends Collection<BossBar.Flag>> BossBarMixerImpl(final ComponentReel nameReel, final FrameReel<Float> progressReel, final FrameReel<BossBar.Color> colorReel, final FrameReel<BossBar.Overlay> overlayReel, final FrameReel<C> flagsReel) {
    if (sizeCheck(nameReel, progressReel, colorReel, overlayReel, flagsReel))
      throw new IllegalArgumentException("Size must be the same for each reel.");
    this.nameReel = nameReel;
    this.progressReel = progressReel;
    this.colorReel = colorReel;
    this.overlayReel = overlayReel;
    this.flagsReel = FrameReel.reel(flagsReel.frames().stream().map(BossBarState::copyFlags).collect(Collectors.toList()));
  }

  private static boolean sizeCheck(final GenericFrameReel<?, ?, ?> first, final GenericFrameReel<?, ?, ?>... reels) {
    final int size = first.frames().size();

    for (final GenericFrameReel<?, ?, ?> reel : reels) {
      if (reel.frames().size() != size)
        return true;
    }

    return false;
  }

  @Override
  public ComponentReel nameReel() {
    return this.nameReel;
  }

  @Override
  public BossBarMixer nameReel(final ComponentReel nameReel) {
    return new BossBarMixerImpl(nameReel, this.progressReel, this.colorReel, this.overlayReel, this.flagsReel);
  }

  @Override
  public FrameReel<Float> progressReel() {
    return this.progressReel;
  }

  @Override
  public BossBarMixer progressReel(final FrameReel<Float> progressReel) {
    return new BossBarMixerImpl(this.nameReel, progressReel, this.colorReel, this.overlayReel, this.flagsReel);
  }

  @Override
  public FrameReel<BossBar.Color> colorReel() {
    return this.colorReel;
  }

  @Override
  public BossBarMixer colorReel(final FrameReel<BossBar.Color> colorReel) {
    return new BossBarMixerImpl(this.nameReel, this.progressReel, colorReel, this.overlayReel, this.flagsReel);
  }

  @Override
  public FrameReel<BossBar.Overlay> overlayReel() {
    return this.overlayReel;
  }

  @Override
  public BossBarMixer overlayReel(final FrameReel<BossBar.Overlay> overlayReel) {
    return new BossBarMixerImpl(this.nameReel, this.progressReel, this.colorReel, overlayReel, this.flagsReel);
  }

  @Override
  public FrameReel<Set<BossBar.Flag>> flagsReel() {
    return this.flagsReel;
  }

  @Override
  public <C extends Collection<BossBar.Flag>> BossBarMixer flagsReel(final FrameReel<C> flagsReel) {
    return new BossBarMixerImpl(this.nameReel, this.progressReel, this.colorReel, this.overlayReel, flagsReel);
  }

  @Override
  public FrameReel<BossBarState> asFrameReel() {
    final List<BossBarState> frames = new LinkedList<>();
    for (int i = 0; i < this.nameReel.frames().size(); i++) {
      frames.add(BossBarState.state(
        this.nameReel.frames().get(i),
        this.progressReel.frames().get(i),
        this.colorReel.frames().get(i),
        this.overlayReel.frames().get(i),
        Collections.unmodifiableSet(new HashSet<>(this.flagsReel.frames().get(i)))
        ));
    }

    return FrameReel.reel(frames);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("nameReel", this.nameReel),
      ExaminableProperty.of("progressReel", this.progressReel),
      ExaminableProperty.of("colorReel", this.colorReel),
      ExaminableProperty.of("overlayReel", this.overlayReel),
      ExaminableProperty.of("flagsReel", this.flagsReel)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

  @Override
  public @NotNull BossBarMixerBuilder toBuilder() {
    return new BuilderImpl(this.nameReel, this.progressReel, this.colorReel, this.overlayReel, this.flagsReel);
  }

  static class BuilderImpl implements BossBarMixerBuilder {

    private int targetSize = -1;

    private FrameReelGenerator<ComponentReel> nameReel;

    private FrameReelGenerator<FrameReel<Float>> progressReel;

    private FrameReelGenerator<FrameReel<BossBar.Color>> colorReel;

    private FrameReelGenerator<FrameReel<BossBar.Overlay>> overlayReel;

    private FrameReelGenerator<FrameReel<Collection<BossBar.Flag>>> flagsReel;

    <C extends Collection<BossBar.Flag>> BuilderImpl(final ComponentReel nameReel, final FrameReel<Float> progressReel, final FrameReel<BossBar.Color> colorReel, final FrameReel<BossBar.Overlay> overlayReel, final FrameReel<C> flagsReel) {
      if (sizeCheck(nameReel, progressReel, colorReel, overlayReel, flagsReel))
        throw new IllegalArgumentException("Size must be the same for each reel.");
      this.nameReel = FrameReelGenerator.fromComponentReel(nameReel);
      this.progressReel = FrameReelGenerator.fromReel(progressReel);
      this.colorReel = FrameReelGenerator.fromReel(colorReel);
      this.overlayReel = FrameReelGenerator.fromReel(overlayReel);
      this.flagsReel = FrameReelGenerator.fromReel(FrameReel.reel(flagsReel.frames().stream().map(BossBarState::copyFlags).collect(Collectors.toList())));
    }

    BuilderImpl() {
      this.nameReel = FrameReelGenerator.componentFrameReplicator(null);
      this.progressReel = FrameReelGenerator.frameReplicator(BossBarState.DO_NOT_APPLY_PROGRESS);
      this.colorReel = FrameReelGenerator.frameReplicator(null);
      this.overlayReel = FrameReelGenerator.frameReplicator(null);
      this.flagsReel = FrameReelGenerator.frameReplicator(null);
    }

    @Override
    public @NotNull BossBarMixer build() {
      int targetSize = this.targetSize;
      if (targetSize < 0) {
        int size = Integer.MAX_VALUE;
        for (final FrameReelGenerator<?> r : Arrays.asList(this.nameReel, this.progressReel, this.colorReel, this.overlayReel, this.flagsReel)) {
          if (r instanceof FrameReelLike)
            size = Math.min(((FrameReelLike<?>) r).asFrameReel().size(), size);
          else if (r instanceof InflexibleFrameReelLike)
            size = Math.min(((InflexibleFrameReelLike<?>) r).createFrameReel().size(), size);
        }

        if (size == Integer.MAX_VALUE)
          throw new IllegalArgumentException("Can not build the reel because target size is unknown.");

        targetSize = size;
      }

      return new BossBarMixerImpl(this.nameReel.createReel(targetSize), this.progressReel.createReel(targetSize), this.colorReel.createReel(targetSize), this.overlayReel.createReel(targetSize), this.flagsReel.createReel(targetSize));
    }

    @Override
    public BossBarMixerBuilder targetSize(final int size) {
      if (size < 0)
        throw new IllegalArgumentException("Size must not be negative");
      this.targetSize = size;
      return this;
    }

    @Override
    public BossBarMixerBuilder nameReel(final ComponentReel nameReel) {
      return this.nameReel(FrameReelGenerator.fromComponentReel(nameReel));
    }

    @Override
    public BossBarMixerBuilder nameReel(final FrameReelGenerator<ComponentReel> generator) {
      this.nameReel = generator;
      return this;
    }

    @Override
    public BossBarMixerBuilder progressReel(final FrameReel<Float> progressReel) {
      return this.progressReel(FrameReelGenerator.fromReel(progressReel));
    }

    @Override
    public BossBarMixerBuilder progressReel(final FrameReelGenerator<FrameReel<Float>> generator) {
      this.progressReel = generator;
      return this;
    }

    @Override
    public BossBarMixerBuilder colorReel(final FrameReel<BossBar.Color> colorReel) {
      return this.colorReel(FrameReelGenerator.fromReel(colorReel));
    }

    @Override
    public BossBarMixerBuilder colorReel(final FrameReelGenerator<FrameReel<BossBar.Color>> generator) {
      this.colorReel = generator;
      return this;
    }

    @Override
    public BossBarMixerBuilder overlayReel(final FrameReel<BossBar.Overlay> overlayReel) {
      return this.overlayReel(FrameReelGenerator.fromReel(overlayReel));
    }

    @Override
    public BossBarMixerBuilder overlayReel(final FrameReelGenerator<FrameReel<BossBar.Overlay>> generator) {
      this.overlayReel = generator;
      return this;
    }

    @Override
    public <C extends Collection<BossBar.Flag>> BossBarMixerBuilder flagsReel(final FrameReel<C> flagsReel) {
      return this.flagsReel(FrameReelGenerator.fromReel(flagsReel));
    }

    @Override
    public <C extends Collection<BossBar.Flag>> BossBarMixerBuilder flagsReel(final FrameReelGenerator<FrameReel<C>> generator) {
      this.flagsReel = s -> FrameReel.reel(generator.createReel(s).frames().stream().map(BossBarState::copyFlags).collect(Collectors.toList()));
      return this;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("nameReel", this.nameReel),
        ExaminableProperty.of("progressReel", this.progressReel),
        ExaminableProperty.of("colorReel", this.colorReel),
        ExaminableProperty.of("overlayReel", this.overlayReel),
        ExaminableProperty.of("flagsReel", this.flagsReel)
      );
    }

    @Override
    public String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }

  }

}
