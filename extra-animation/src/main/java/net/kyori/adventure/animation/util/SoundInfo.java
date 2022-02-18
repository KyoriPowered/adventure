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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;

/**
 * Full information to play/stop sound.
 *
 * @since 1.10.0
 */
public interface SoundInfo extends Examinable {

  /**
   * This same as {@link Audience#playSound(Sound)}.
   *
   * @param sound sound
   * @return created sound info
   *
   * @since 1.10.0
   */
  static SoundInfo playSound(Sound sound) {
    return new SoundInfo() {

      @Override
      public void apply(final Audience target) {
        target.playSound(sound);
      }

      @Override
      public @NotNull String examinableName() {
        return "SoundInfo";
      }

      @Override
      public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
          ExaminableProperty.of("action", "playSound"),
          ExaminableProperty.of("sound", sound)
        );
      }

      @Override
      public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
      }

    };
  }

  /**
   * This same as {@link Audience#playSound(Sound, Sound.Emitter)}.
   *
   * @param sound sound
   * @param emitter emitter
   * @return created sound info
   *
   * @since 1.10.0
   */
  static SoundInfo playSound(Sound sound, Sound.Emitter emitter) {
    return new SoundInfo() {

      @Override
      public void apply(final Audience target) {
        target.playSound(sound, emitter);
      }

      @Override
      public @NotNull String examinableName() {
        return "SoundInfo";
      }

      @Override
      public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
          ExaminableProperty.of("action", "playSound"),
          ExaminableProperty.of("sound", sound),
          ExaminableProperty.of("emitter", emitter)
        );
      }

      @Override
      public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
      }

    };
  }

  /**
   * This same as {@link Audience#playSound(Sound, double, double, double)}.
   *
   * @param sound sound
   * @param x x
   * @param y y
   * @param z z
   * @return created sound info
   *
   * @since 1.10.0
   */
  static SoundInfo playSound(Sound sound, double x, double y, double z) {
    return new SoundInfo() {

      @Override
      public void apply(final Audience target) {
        target.playSound(sound, x, y, z);
      }

      @Override
      public @NotNull String examinableName() {
        return "SoundInfo";
      }

      @Override
      public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
          ExaminableProperty.of("action", "playSound"),
          ExaminableProperty.of("sound", sound),
          ExaminableProperty.of("x", x),
          ExaminableProperty.of("y", y),
          ExaminableProperty.of("z", z)
        );
      }

      @Override
      public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
      }

    };
  }

  /**
   * This same as {@link Audience#stopSound(Sound)}.
   *
   * @param sound sound
   * @return created sound info
   *
   * @since 1.10.0
   */
  static SoundInfo stopSound(Sound sound) {
    return new SoundInfo() {

      @Override
      public void apply(final Audience target) {
        target.stopSound(sound);
      }

      @Override
      public @NotNull String examinableName() {
        return "SoundInfo";
      }

      @Override
      public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
          ExaminableProperty.of("action", "stopSound"),
          ExaminableProperty.of("sound", sound)
        );
      }

      @Override
      public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
      }

    };
  }

  /**
   * This same as {@link Audience#stopSound(SoundStop)}.
   *
   * @param stop sound stop
   * @return created sound info
   *
   * @since 1.10.0
   */
  static SoundInfo stopSound(SoundStop stop) {
    return new SoundInfo() {

      @Override
      public void apply(final Audience target) {
        target.stopSound(stop);
      }

      @Override
      public @NotNull String examinableName() {
        return "SoundInfo";
      }

      @Override
      public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
          ExaminableProperty.of("action", "stopSound"),
          ExaminableProperty.of("soundStop", stop)
        );
      }

      @Override
      public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
      }

    };
  }

  /**
   * Applies operation. (Plays or stops sound)
   *
   * @param target target
   *
   * @since 1.10.0
   */
  void apply(Audience target);

  /**
   * Combines some {@link SoundInfo} actions.
   *
   * @param another sound info array
   * @return combined sound info
   *
   * @since 1.10.0
   */
  default SoundInfo combine(SoundInfo... another) {
    final Set<SoundInfo> components = new HashSet<>(Arrays.asList(another));

    components.add(this);

    return new SoundInfo() {

      @Override
      public void apply(final Audience target) {
        for (final SoundInfo info : components) {
          info.apply(target);
        }
      }

      @Override
      public @NotNull String examinableName() {
        return "CombinedSoundInfo";
      }

      @Override
      public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
          ExaminableProperty.of("components", components)
        );
      }

      @Override
      public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
      }

    };

  }

}
