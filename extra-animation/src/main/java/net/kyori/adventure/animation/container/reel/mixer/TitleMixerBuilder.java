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

import net.kyori.adventure.animation.container.reel.ComponentReel;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.generator.FrameReelGenerator;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.Examinable;

/**
 * Builder of {@link TitleMixer}.
 *
 * @since 1.10.0
 */
public interface TitleMixerBuilder extends Buildable.Builder<TitleMixer>, Examinable {

  /**
   * Sets size of target reel.
   *
   * @param size target size
   * @return builder instance
   *
   * @since 1.10.0
   */
  TitleMixerBuilder targetSize(int size);

  /**
   * Sets the title reel.
   *
   * @param componentReel title reel
   * @return this builder
   *
   * @since 1.10.0
   */
  TitleMixerBuilder titleReel(ComponentReel componentReel);

  /**
   * Sets the title reel to generated reel.
   *
   * @param generator title reel generator
   * @return this builder
   *
   * @since 1.10.0
   */
  TitleMixerBuilder titleReel(FrameReelGenerator<ComponentReel> generator);

  /**
   * Sets the subtitle reel.
   *
   * @param componentReel subtitle reel
   * @return this builder
   *
   * @since 1.10.0
   */
  TitleMixerBuilder subtitleReel(ComponentReel componentReel);

  /**
   * Sets the subtitle reel to generated reel.
   *
   * @param generator subtitle reel generator
   * @return this builder
   *
   * @since 1.10.0
   */
  TitleMixerBuilder subtitleReel(FrameReelGenerator<ComponentReel> generator);

  /**
   * Sets the times reel.
   *
   * @param timesReel times reel
   * @return this builder
   *
   * @since 1.10.0
   */
  TitleMixerBuilder timesReel(FrameReel<Title.Times> timesReel);

  /**
   * Sets the times reel to generated reel.
   *
   * @param generator times reel generator
   * @return this builder
   *
   * @since 1.10.0
   */
  TitleMixerBuilder timesReel(FrameReelGenerator<FrameReel<Title.Times>> generator);

}
