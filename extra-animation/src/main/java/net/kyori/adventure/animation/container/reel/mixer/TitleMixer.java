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

import java.time.Duration;
import net.kyori.adventure.animation.container.reel.ComponentReel;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.ReelTransformer;
import net.kyori.adventure.animation.container.reel.generator.FrameReelLike;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.Examinable;

/**
 * A reel mixer utilizing creating title frames from its parts.
 *
 * @since 1.10.0
 */
public interface TitleMixer extends ReelMixer, FrameReelLike<FrameReel<Title>>, Buildable<TitleMixer, TitleMixerBuilder>, Examinable {

  /**
   * Default times for titles for this mixer.
   *
   * @since 1.10.0
   */
  Title.Times DEFAULT_ANIMATION_TIMES = Title.Times.times(Duration.ZERO, Title.DEFAULT_TIMES.stay(), Title.DEFAULT_TIMES.fadeOut());

  /**
   * Gets the title reel.
   *
   * @return title reel
   *
   * @since 1.10.0
   */
  ComponentReel titleReel();

  /**
   * Sets the title reel.
   *
   * @param componentReel title reel
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  TitleMixer titleReel(ComponentReel componentReel);

  /**
   * Transforms the title reel.
   *
   * @param transformer transformer
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  TitleMixer transformTitleReel(ReelTransformer<ComponentReel> transformer);

  /**
   * Gets the subtitle reel.
   *
   * @return subtitleReel
   *
   * @since 1.10.0
   */
  ComponentReel subtitleReel();

  /**
   * Sets the subtitle reel.
   *
   * @param componentReel subtitle reel
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  TitleMixer subtitleReel(ComponentReel componentReel);

  /**
   * Transforms the subtitle reel.
   *
   * @param transformer transformer
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  TitleMixer transformSubtitleReel(ReelTransformer<ComponentReel> transformer);

  /**
   * Gets the times reel.
   *
   * @return times reel
   *
   * @since 1.10.0
   */
  FrameReel<Title.Times> timesReel();

  /**
   * Sets the times reel.
   *
   * @param timesReel times reel
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  TitleMixer timesReel(FrameReel<Title.Times> timesReel);

  /**
   * Transforms the times reel.
   *
   * @param transformer transformer
   * @return a new instance with change performed
   *
   * @since 1.10.0
   */
  TitleMixer transformTimesReel(ReelTransformer<FrameReel<Title.Times>> transformer);

}
