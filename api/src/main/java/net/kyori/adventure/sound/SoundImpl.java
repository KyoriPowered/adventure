/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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

import java.util.OptionalLong;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.ShadyPines;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import static java.util.Objects.requireNonNull;

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
  private final OptionalLong seed;
  private SoundStop stop;

  SoundImpl(final @NotNull Source source, final float volume, final float pitch, final OptionalLong seed) {
    this.source = source;
    this.volume = volume;
    this.pitch = pitch;
    this.seed = seed;
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
  public OptionalLong seed() {
    return this.seed;
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
      && ShadyPines.equals(this.pitch, that.pitch)
      && this.seed.equals(that.seed);
  }

  @Override
  public int hashCode() {
    int result = this.name().hashCode();
    result = (31 * result) + this.source.hashCode();
    result = (31 * result) + Float.hashCode(this.volume);
    result = (31 * result) + Float.hashCode(this.pitch);
    result = (31 * result) + this.seed.hashCode();
    return result;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("name", this.name()),
      ExaminableProperty.of("source", this.source),
      ExaminableProperty.of("volume", this.volume),
      ExaminableProperty.of("pitch", this.pitch),
      ExaminableProperty.of("seed", this.seed)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  static final class BuilderImpl implements Builder {
    private static final float DEFAULT_VOLUME = 1f;
    private static final float DEFAULT_PITCH = 1f;
    private Key eagerType;
    private Supplier<? extends Type> lazyType;
    private Source source = Source.MASTER;
    private float volume = DEFAULT_VOLUME;
    private float pitch = DEFAULT_PITCH;
    private OptionalLong seed = OptionalLong.empty();

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull Sound existing) {
      if (existing instanceof Eager) {
        this.type(((Eager) existing).name);
      } else if (existing instanceof Lazy) {
        this.type(((Lazy) existing).supplier);
      } else {
        throw new IllegalArgumentException("Unknown sound type " + existing + ", must be Eager or Lazy");
      }

      this.source(existing.source())
        .volume(existing.volume())
        .pitch(existing.pitch())
        .seed(existing.seed());
    }

    @Override
    public @NotNull Builder type(final @NotNull Key type) {
      this.eagerType = requireNonNull(type, "type");
      this.lazyType = null;
      return this;
    }

    @Override
    public @NotNull Builder type(final @NotNull Type type) {
      this.eagerType = requireNonNull(requireNonNull(type, "type").key(), "type.key()");
      this.lazyType = null;
      return this;
    }

    @Override
    public @NotNull Builder type(final @NotNull Supplier<? extends Type> typeSupplier) {
      this.lazyType = requireNonNull(typeSupplier, "typeSupplier");
      this.eagerType = null;
      return this;
    }

    @Override
    public @NotNull Builder source(final @NotNull Source source) {
      this.source = requireNonNull(source, "source");
      return this;
    }

    @Override
    public @NotNull Builder source(final Source.@NotNull Provider source) {
      return this.source(source.soundSource());
    }

    @Override
    public @NotNull Builder volume(final @Range(from = 0, to = Integer.MAX_VALUE) float volume) {
      this.volume = volume;
      return this;
    }

    @Override
    public @NotNull Builder pitch(final @Range(from = -1, to = 1) float pitch) {
      this.pitch = pitch;
      return this;
    }

    @Override
    public @NotNull Builder seed(final long seed) {
      this.seed = OptionalLong.of(seed);
      return this;
    }

    @Override
    public @NotNull Builder seed(final @NotNull OptionalLong seed) {
      this.seed = requireNonNull(seed, "seed");
      return this;
    }

    @Override
    public @NotNull Sound build() {
      if (this.eagerType != null) {
        return new Eager(this.eagerType, this.source, this.volume, this.pitch, this.seed);
      } else if (this.lazyType != null) {
        return new Lazy(this.lazyType, this.source, this.volume, this.pitch, this.seed);
      } else {
        throw new IllegalStateException("A sound type must be provided to build a sound");
      }
    }
  }

  static final class Eager extends SoundImpl {
    final Key name;

    Eager(final @NotNull Key name, final @NotNull Source source, final float volume, final float pitch, final OptionalLong seed) {
      super(source, volume, pitch, seed);
      this.name = name;
    }

    @Override
    public @NotNull Key name() {
      return this.name;
    }
  }

  static final class Lazy extends SoundImpl {
    final Supplier<? extends Type> supplier;

    Lazy(final @NotNull Supplier<? extends Type> supplier, final @NotNull Source source, final float volume, final float pitch, final OptionalLong seed) {
      super(source, volume, pitch, seed);
      this.supplier = supplier;
    }

    @Override
    public @NotNull Key name() {
      return this.supplier.get().key();
    }
  }
}
