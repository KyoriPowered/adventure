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

import java.util.Collection;
import net.kyori.adventure.animation.container.reel.ComponentReel;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.generator.FrameReelGenerator;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.Examinable;

/**
 * The builder of {@link BossBarMixer}.
 *
 * @since 1.10.0
 */
public interface BossBarMixerBuilder extends Buildable.Builder<BossBarMixer>, Examinable {

  /**
   * Sets size of target reel.
   *
   * @param size target size
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder targetSize(int size);

  /**
   * Sets the name reel.
   *
   * @param nameReel name reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder nameReel(ComponentReel nameReel);

  /**
   * Sets the name reel to a generated reel.
   *
   * @param generator generator to create reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder nameReel(FrameReelGenerator<ComponentReel> generator);

  /**
   * Sets the progress reel.
   *
   * @param progressReel progress reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder progressReel(FrameReel<Float> progressReel);


  /**
   * Sets the progress reel to generated reel.
   *
   * @param generator generator to create the reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder progressReel(FrameReelGenerator<FrameReel<Float>> generator);

  /**
   * Sets the color reel.
   *
   * @param colorReel color reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder colorReel(FrameReel<BossBar.Color> colorReel);


  /**
   * Sets the color reel to generated reel.
   *
   * @param generator generator to create the reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder colorReel(FrameReelGenerator<FrameReel<BossBar.Color>> generator);

  /**
   * Sets the overlay reel.
   *
   * @param overlayReel overlay reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder overlayReel(FrameReel<BossBar.Overlay> overlayReel);

  /**
   * Sets the overlay reel to generated reel.
   *
   * @param generator generator to create reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  BossBarMixerBuilder overlayReel(FrameReelGenerator<FrameReel<BossBar.Overlay>> generator);

  /**
   * Sets the flags reel.
   *
   * @param flagsReel the flags reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  <C extends Collection<BossBar.Flag>> BossBarMixerBuilder flagsReel(FrameReel<C> flagsReel);

  /**
   * Sets the flags reel to generated reel.
   *
   * @param generator generator to create the reel
   * @return builder instance
   *
   * @since 1.10.0
   */
  <C extends Collection<BossBar.Flag>> BossBarMixerBuilder flagsReel(FrameReelGenerator<FrameReel<C>> generator);

}
