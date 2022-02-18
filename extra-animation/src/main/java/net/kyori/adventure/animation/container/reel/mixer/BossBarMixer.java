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
import java.util.Set;
import net.kyori.adventure.animation.container.reel.ComponentReel;
import net.kyori.adventure.animation.container.reel.FrameReel;
import net.kyori.adventure.animation.container.reel.ReelTransformer;
import net.kyori.adventure.animation.container.reel.generator.FrameReelLike;
import net.kyori.adventure.animation.util.BossBarState;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.util.Buildable;
import net.kyori.examination.Examinable;

/**
 * A reel mixer utilizing creating bossbar frames from its parts.
 *
 * @since 1.10.0
 */
public interface BossBarMixer extends ReelMixer, FrameReelLike<FrameReel<BossBarState>>, Buildable<BossBarMixer, BossBarMixerBuilder>, Examinable {

  /**
   * Gets the name reel.
   *
   * @return name reel
   *
   * @since 1.10.0
   */
  ComponentReel nameReel();

  /**
   * Sets the name reel.
   *
   * @param nameReel name reel to set
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarMixer nameReel(ComponentReel nameReel);

  /**
   * Transforms the name reel.
   *
   * @param transformer transformer
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  default BossBarMixer transformNameReel(ReelTransformer<ComponentReel> transformer) {
    return this.nameReel(transformer.transform(this.nameReel()));
  }

  /**
   * Gets the progress reel.
   *
   * @return progress reel
   *
   * @since 1.10.0
   */
  FrameReel<Float> progressReel();

  /**
   * Sets the progress reel.
   *
   * @param progressReel progress reel to set
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarMixer progressReel(FrameReel<Float> progressReel);

  /**
   * Transforms the progress reel.
   *
   * @param transformer transformer
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  default BossBarMixer transformProgressReel(ReelTransformer<FrameReel<Float>> transformer) {
    return this.progressReel(transformer.transform(this.progressReel()));
  }

  /**
   * Gets the color reel.
   *
   * @return color reel
   *
   * @since 1.10.0
   */
  FrameReel<BossBar.Color> colorReel();

  /**
   * Sets the color reel.
   *
   * @param colorReel color reel to set
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarMixer colorReel(FrameReel<BossBar.Color> colorReel);

  /**
   * Transforms the color reel.
   *
   * @param transformer transformer
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  default BossBarMixer transformColorReel(ReelTransformer<FrameReel<BossBar.Color>> transformer) {
    return this.colorReel(transformer.transform(this.colorReel()));
  }

  /**
   * Gets the overlay reel.
   *
   * @return overlay reel
   *
   * @since 1.10.0
   */
  FrameReel<BossBar.Overlay> overlayReel();

  /**
   * Sets the overlay reel.
   *
   * @param overlayReel overlay reel to set
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  BossBarMixer overlayReel(FrameReel<BossBar.Overlay> overlayReel);

  /**
   * Transforms the overlay reel.
   *
   * @param transformer transformer
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  default BossBarMixer transformOverlayReel(ReelTransformer<FrameReel<BossBar.Overlay>> transformer) {
    return this.overlayReel(transformer.transform(this.overlayReel()));
  }

  /**
   * Gets the flags reel.
   *
   * @return flags reel
   *
   * @since 1.10.0
   */
  FrameReel<Set<BossBar.Flag>> flagsReel();

  /**
   * Sets the flags reel.
   *
   * @param flagsReel flags reel to set
   * @param <C> type of collection containing flags
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  <C extends Collection<BossBar.Flag>> BossBarMixer flagsReel(FrameReel<C> flagsReel);

  /**
   * Transforms the flags reel.
   *
   * @param transformer transformer
   * @return new instance with change performed
   *
   * @since 1.10.0
   */
  default BossBarMixer transformFlagsReel(ReelTransformer<FrameReel<Set<BossBar.Flag>>> transformer) {
    return this.flagsReel(transformer.transform(this.flagsReel()));
  }

}
