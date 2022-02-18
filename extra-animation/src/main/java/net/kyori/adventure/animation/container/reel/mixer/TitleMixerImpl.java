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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import net.kyori.adventure.animation.container.reel.ComponentReel;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.ReelTransformer;
import net.kyori.adventure.animation.container.reel.generator.FrameReelGenerator;
import net.kyori.adventure.animation.container.reel.generator.FrameReelLike;
import net.kyori.adventure.animation.container.reel.generator.InflexibleFrameReelLike;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

class TitleMixerImpl implements TitleMixer {

  private final ComponentReel titleReel;

  private final ComponentReel subtitleReel;

  private final FrameReel<Title.Times> timesReel;

  TitleMixerImpl(final ComponentReel titleReel, final ComponentReel subtitleReel, final FrameReel<Title.Times> timesReel) {
    this.titleReel = titleReel;
    this.subtitleReel = subtitleReel;
    this.timesReel = timesReel;
  }

  @Override
  public ComponentReel titleReel() {
    return this.titleReel;
  }

  @Override
  public TitleMixer titleReel(final ComponentReel componentReel) {
    return new TitleMixerImpl(componentReel, this.subtitleReel, this.timesReel);
  }

  @Override
  public TitleMixer transformTitleReel(final ReelTransformer<ComponentReel> transformer) {
    return this.titleReel(transformer.transform(this.titleReel()));
  }

  @Override
  public ComponentReel subtitleReel() {
    return this.subtitleReel;
  }

  @Override
  public TitleMixer subtitleReel(final ComponentReel componentReel) {
    return new TitleMixerImpl(this.titleReel, componentReel, this.timesReel);
  }

  @Override
  public TitleMixer transformSubtitleReel(final ReelTransformer<ComponentReel> transformer) {
    return this.subtitleReel(transformer.transform(this.subtitleReel()));
  }

  @Override
  public FrameReel<Title.Times> timesReel() {
    return this.timesReel;
  }

  @Override
  public TitleMixer timesReel(final FrameReel<Title.Times> timesReel) {
    return new TitleMixerImpl(this.titleReel, this.subtitleReel, timesReel);
  }

  @Override
  public TitleMixer transformTimesReel(final ReelTransformer<FrameReel<Title.Times>> transformer) {
    return this.timesReel(transformer.transform(this.timesReel()));
  }

  @Override
  public @NotNull TitleMixerBuilder toBuilder() {
    return new BuilderImpl(this.titleReel, this.subtitleReel, this.timesReel);
  }

  @Override
  public FrameReel<Title> asFrameReel() {
    final int highestLength = Math.max(Math.max(this.titleReel.frames().size(), this.subtitleReel.frames().size()), this.timesReel.frames().size());
    final List<Title> frames = new LinkedList<>();

    for (int i = 0; i < highestLength; i++) {
      Component title = Component.empty();
      Component subtitle = Component.empty();
      Title.Times times = DEFAULT_ANIMATION_TIMES;

      if (i < this.titleReel.frames().size())
        title = this.titleReel.frames().get(i);

      if (i < this.subtitleReel.frames().size())
        subtitle = this.subtitleReel.frames().get(i);

      if (i < this.timesReel.frames().size())
        times = this.timesReel.frames().get(i);

      frames.add(Title.title(title, subtitle, times));
    }

    return FrameReel.reel(frames);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("titleReel", this.titleReel),
      ExaminableProperty.of("subtitleReel", this.subtitleReel),
      ExaminableProperty.of("timesReel", this.timesReel)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

  static class BuilderImpl implements TitleMixerBuilder {

    private FrameReelGenerator<ComponentReel> titleReel;

    private FrameReelGenerator<ComponentReel> subtitleReel;

    private FrameReelGenerator<FrameReel<Title.Times>> timesReel;

    private int targetSize = -1;

    BuilderImpl(final FrameReelGenerator<ComponentReel> titleReel, final FrameReelGenerator<ComponentReel> subtitleReel, final FrameReelGenerator<FrameReel<Title.Times>> timesReel) {
      this.titleReel = titleReel;
      this.subtitleReel = subtitleReel;
      this.timesReel = timesReel;
    }

    BuilderImpl(final ComponentReel titleReel, final ComponentReel subtitleReel, final FrameReel<Title.Times> timesReel) {
      this.titleReel = FrameReelGenerator.fromComponentReel(titleReel);
      this.subtitleReel = FrameReelGenerator.fromComponentReel(subtitleReel);
      this.timesReel = FrameReelGenerator.fromReel(timesReel);
    }

    @Override
    public TitleMixerBuilder targetSize(final int size) {
      if (size < 0)
        throw new IllegalArgumentException("Size must not be negative");
      this.targetSize = size;
      return this;
    }

    @Override
    public TitleMixerBuilder titleReel(final ComponentReel componentReel) {
      return this.titleReel(FrameReelGenerator.fromComponentReel(componentReel));
    }

    @Override
    public BuilderImpl titleReel(final FrameReelGenerator<ComponentReel> titleReel) {
      this.titleReel = titleReel;
      return this;
    }

    @Override
    public TitleMixerBuilder subtitleReel(final ComponentReel componentReel) {
      return this.subtitleReel(FrameReelGenerator.fromComponentReel(componentReel));
    }

    @Override
    public BuilderImpl subtitleReel(final FrameReelGenerator<ComponentReel> subtitleReel) {
      this.subtitleReel = subtitleReel;
      return this;
    }

    @Override
    public TitleMixerBuilder timesReel(final FrameReel<Title.Times> timesReel) {
      return this.timesReel(FrameReelGenerator.fromReel(timesReel));
    }

    @Override
    public BuilderImpl timesReel(final FrameReelGenerator<FrameReel<Title.Times>> timesReel) {
      this.timesReel = timesReel;
      return this;
    }

    @Override
    public @NotNull TitleMixer build() {
      int targetSize = this.targetSize;
      if (targetSize < 0) {
        int size = Integer.MAX_VALUE;
        for (final FrameReelGenerator<?> r : Arrays.asList(this.titleReel, this.subtitleReel, this.titleReel)) {
          if (r instanceof FrameReelLike)
            size = Math.min(((FrameReelLike<?>) r).asFrameReel().size(), size);
          else if (r instanceof InflexibleFrameReelLike)
            size = Math.min(((InflexibleFrameReelLike<?>) r).createFrameReel().size(), size);
        }

        if (size == Integer.MAX_VALUE)
          throw new IllegalArgumentException("Can not build the reel because target size is unknown.");

        targetSize = size;
      }

      return new TitleMixerImpl(this.titleReel.createReel(targetSize), this.subtitleReel.createReel(targetSize), this.timesReel.createReel(targetSize));
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("titleReel", this.titleReel),
        ExaminableProperty.of("subtitleReel", this.subtitleReel),
        ExaminableProperty.of("timesReel", this.timesReel)
      );
    }

    @Override
    public String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }

  }

}
