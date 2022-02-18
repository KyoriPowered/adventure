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
import java.util.List;
import java.util.Set;
import net.kyori.adventure.animation.container.FrameContainer;
import net.kyori.adventure.animation.container.reel.ComponentReelBuilder;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.FrameReelBuilder;
import net.kyori.adventure.animation.container.reel.GenericFrameReel;
import net.kyori.adventure.animation.container.reel.generator.FrameReelGenerator;
import net.kyori.adventure.animation.util.BossBarState;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

/**
 * An object that combines frame reel into a bigger integrity. E.g. title part frames to title frames
 *
 * @since 1.10.0
 */
public interface ReelMixer {

  /**
   * Creates {@link MonoTypeReelMixer}.
   *
   * @param reels list of reels
   * @param <R> reel type
   * @return created mixer
   *
   * @since 1.10.0
   */
  static <R extends GenericFrameReel<?, R, ?>> MonoTypeReelMixer<R> mixer(final List<R> reels) {
    return new MonoTypeReelMixerImpl<>(reels);
  }

  /**
   * Creates {@link MonoTypeReelMixer}.
   *
   * @param reels array of reels
   * @param <R> reel type
   * @return created mixer
   *
   * @since 1.10.0
   */
  @SafeVarargs
  static <R extends GenericFrameReel<?, R, ?>> MonoTypeReelMixer<R> mixer(final R... reels) {
    return mixer(Arrays.asList(reels));
  }

  /**
   * Creates new {@link MonoTypeReelMixerBuilder} instance.
   *
   * @param <R> reel type
   * @return created builder
   *
   * @since 1.10.0
   */
  static <R extends GenericFrameReel<?, R, ?>> MonoTypeReelMixerBuilder<R> mixerBuilder() {
    return new MonoTypeReelMixerImpl.BuilderImpl<>();
  }

  /**
   * Creates new {@link TitleMixer}.
   *
   * @param titles titles
   * @param subtitles subtitles
   * @param timesFrames times
   * @return created mixer
   *
   * @since 1.10.0
   */
  static TitleMixer title(final FrameContainer<Component> titles, final FrameContainer<Component> subtitles, final FrameContainer<Title.Times> timesFrames) {
    return new TitleMixerImpl(FrameReel.component(titles), FrameReel.component(subtitles), FrameReel.reel(timesFrames));
  }

  /**
   * Creates new {@link TitleMixer}.
   *
   * @param container container of frames
   * @return created mixer
   *
   * @since 1.10.0
   */
  static TitleMixer title(final FrameContainer<Title> container) {
    final ComponentReelBuilder titles = FrameReel.componentReelBuilder();
    final ComponentReelBuilder subtitles = FrameReel.componentReelBuilder();
    final FrameReelBuilder<Title.Times> timesFrames = FrameReel.reelBuilder();

    container.frames().forEach(f -> {
      titles.append(f.title());
      subtitles.append(f.subtitle());
      timesFrames.append(f.times());
    });

    return new TitleMixerImpl(titles.build(), subtitles.build(), timesFrames.build());
  }

  /**
   * Creates new {@link TitleMixerBuilder}.
   *
   * @return created builder
   *
   * @since 1.10.0
   */
  static TitleMixerBuilder titleMixerBuilder() {
    return new TitleMixerImpl.BuilderImpl(FrameReelGenerator.componentFrameReplicator(Component.empty()), FrameReelGenerator.componentFrameReplicator(Component.empty()), FrameReelGenerator.frameReplicator(TitleMixer.DEFAULT_ANIMATION_TIMES));
  }

  /**
   * Creates new {@link BossBarMixer}.
   *
   * @param names names
   * @param progresses progresses
   * @param colors colors
   * @param overlays overlays
   * @param flags flags
   * @param <C> flags collection type
   * @return created mixer
   *
   * @since 1.10.0
   */
  static <C extends Collection<BossBar.Flag>> BossBarMixer bossBar(FrameContainer<Component> names, FrameContainer<Float> progresses, FrameContainer<BossBar.Color> colors, FrameContainer<BossBar.Overlay> overlays, FrameContainer<C> flags) {
    return new BossBarMixerImpl(FrameReel.component(names), FrameReel.reel(progresses), FrameReel.reel(colors), FrameReel.reel(overlays), FrameReel.reel(flags));
  }

  /**
   * Creates new {@link BossBarMixer}.
   *
   * @param container frame container
   * @return created mixer
   *
   * @since 1.10.0
   */
  static BossBarMixer bossBar(FrameContainer<BossBarState> container) {
    final ComponentReelBuilder names = FrameReel.componentReelBuilder();
    final FrameReelBuilder<Float> progresses = FrameReel.reelBuilder();
    final FrameReelBuilder<BossBar.Color> colors = FrameReel.reelBuilder();
    final FrameReelBuilder<BossBar.Overlay> overlays = FrameReel.reelBuilder();
    final FrameReelBuilder<Set<BossBar.Flag>> flags = FrameReel.reelBuilder();

    container.frames().forEach(f -> {
      names.append(f.name());
      progresses.append(f.progress());
      colors.append(f.color());
      overlays.append(f.overlay());
      flags.append(f.flags());
    });

    return new BossBarMixerImpl(names.build(), progresses.build(), colors.build(), overlays.build(), flags.build());
  }

  /**
   * Creates new {@link BossBarMixerBuilder}.
   *
   * @return created builder
   *
   * @since 1.10.0
   */
  static BossBarMixerBuilder bossBarMixerBuilder() {
    return new BossBarMixerImpl.BuilderImpl();
  }

}
