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
package net.kyori.adventure.sound;

import java.util.stream.Stream;
import net.kyori.adventure.util.ShadyPines;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class SoundImpl implements Sound {
  static final Emitter EMITTER_SELF = new Emitter() {
    @Override
    public String toString() {
      return "SelfSoundEmitter";
    }
  };

  private final Source source;
  private final float volume;
  private final float pitch;
  private SoundStop stop;

  SoundImpl(final @NotNull Source source, final float volume, final float pitch) {
    this.source = source;
    this.volume = volume;
    this.pitch = pitch;
  }

  @Override
  public @NotNull Source source() {
    return this.source;
  }

  @Override
  public float volume() {
    return this.volume;
  }

  @Override
  public float pitch() {
    return this.pitch;
  }

  @Override
  public @NotNull SoundStop asStop() {
    if (this.stop == null) this.stop = SoundStop.namedOnSource(this.name(), this.source());
    return this.stop;
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof SoundImpl)) return false;
    final SoundImpl that = (SoundImpl) other;
    return this.name().equals(that.name())
      && this.source == that.source
      && ShadyPines.equals(this.volume, that.volume)
      && ShadyPines.equals(this.pitch, that.pitch);
  }

  @Override
  public int hashCode() {
    int result = this.name().hashCode();
    result = (31 * result) + this.source.hashCode();
    result = (31 * result) + Float.hashCode(this.volume);
    result = (31 * result) + Float.hashCode(this.pitch);
    return result;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("name", this.name()),
      ExaminableProperty.of("source", this.source),
      ExaminableProperty.of("volume", this.volume),
      ExaminableProperty.of("pitch", this.pitch)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }
}
