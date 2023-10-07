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
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.util.Index;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import static java.util.Objects.requireNonNull;

/**
 * Represents an in-game sound which can be played to the client.
 *
 * <p>A sound consists of:</p>
 * <dl>
 *   <dt>key/type</dt>
 *   <dd>the resource location of this sound (e.g minecraft:ambient.cave or my_plugin:custom_sound</dd>
 *   <dt>source</dt>
 *   <dd>a {@link Source} telling the game where the sound is coming from</dd>
 *   <dt>volume</dt>
 *   <dd>a number in the range [0,∞) representing how loud the sound should be played.
 *   Increasing volume does not actually play the sound louder, but increases the radius
 *   of where it can be heard</dd>
 *   <dt>pitch</dt>
 *   <dd>a number in the range [0,2] representing which pitch the sound should be played at</dd>
 * </dl>
 *
 * @see SoundStop
 * @since 4.0.0
 */
@ApiStatus.NonExtendable
public interface Sound extends Examinable {
  /**
   * Create a new builder for {@link Sound} instances.
   *
   * @return a new builder
   * @since 4.12.0
   */
  static @NotNull Builder sound() {
    return new SoundImpl.BuilderImpl();
  }

  /**
   * Create a new builder for {@link Sound} instances.
   *
   * @param existing an existing sound to populate the builder with
   * @return a new builder
   * @since 4.12.0
   */
  static @NotNull Builder sound(final @NotNull Sound existing) {
    return new SoundImpl.BuilderImpl(existing);
  }

  /**
   * Create a new {@link Sound} instance configured by the provided function.
   *
   * @param configurer a function that configures a builder
   * @return a new builder
   * @since 4.12.0
   */
  static @NotNull Sound sound(final @NotNull Consumer<Sound.Builder> configurer) {
    return AbstractBuilder.configureAndBuild(sound(), configurer);
  }

  /**
   * Creates a new sound.
   *
   * @param name the name
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.0.0
   */
  static @NotNull Sound sound(final @NotNull Key name, final @NotNull Source source, final float volume, final float pitch) {
    return sound().type(name).source(source).volume(volume).pitch(pitch).build();
  }

  /**
   * Creates a new sound.
   *
   * @param type the type
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.0.0
   */
  static @NotNull Sound sound(final @NotNull Type type, final @NotNull Source source, final float volume, final float pitch) {
    requireNonNull(type, "type");
    return sound(type.key(), source, volume, pitch);
  }

  /**
   * Creates a new sound.
   *
   * @param type the type
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.0.0
   */
  static @NotNull Sound sound(final @NotNull Supplier<? extends Type> type, final @NotNull Source source, final float volume, final float pitch) {
    return sound().type(type).source(source).volume(volume).pitch(pitch).build();
  }

  /**
   * Creates a new sound.
   *
   * @param name the name
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.8.0
   */
  static @NotNull Sound sound(final @NotNull Key name, final Source.@NotNull Provider source, final float volume, final float pitch) {
    return sound(name, source.soundSource(), volume, pitch);
  }

  /**
   * Creates a new sound.
   *
   * @param type the type
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.8.0
   */
  static @NotNull Sound sound(final @NotNull Type type, final Source.@NotNull Provider source, final float volume, final float pitch) {
    return sound(type, source.soundSource(), volume, pitch);
  }

  /**
   * Creates a new sound.
   *
   * @param type the type
   * @param source the source
   * @param volume the volume
   * @param pitch the pitch
   * @return the sound
   * @since 4.8.0
   */
  static @NotNull Sound sound(final @NotNull Supplier<? extends Type> type, final Source.@NotNull Provider source, final float volume, final float pitch) {
    return sound(type, source.soundSource(), volume, pitch);
  }

  /**
   * Gets the name.
   *
   * @return the name
   * @since 4.0.0
   */
  @NotNull Key name();

  /**
   * Gets the source.
   *
   * @return the source
   * @since 4.0.0
   */
  @NotNull Source source();

  /**
   * Gets the volume.
   *
   * @return the volume
   * @since 4.0.0
   */
  float volume();

  /**
   * Gets the pitch.
   *
   * @return the pitch
   * @since 4.0.0
   */
  float pitch();

  /**
   * Get the seed used for playback of weighted sound effects.
   *
   * <p>When the seed is not provided, the seed of the receiver's world will be used instead.</p>
   *
   * @return the seed to use
   * @since 4.12.0
   */
  @NotNull OptionalLong seed();

  /**
   * Gets the {@link SoundStop} that will stop this specific sound.
   *
   * @return the sound stop
   * @since 4.8.0
   */
  @NotNull SoundStop asStop();

  /**
   * The sound source.
   *
   * @since 4.0.0
   */
  enum Source {
    MASTER("master"),
    MUSIC("music"),
    RECORD("record"),
    WEATHER("weather"),
    BLOCK("block"),
    HOSTILE("hostile"),
    NEUTRAL("neutral"),
    PLAYER("player"),
    AMBIENT("ambient"),
    VOICE("voice");

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    public static final Index<String, Source> NAMES = Index.create(Source.class, source -> source.name);
    private final String name;

    Source(final String name) {
      this.name = name;
    }

    /**
     * A provider of sound sources.
     *
     * @since 4.8.0
     */
    public interface Provider {
      /**
       * Gets the source.
       *
       * @return the source
       * @since 4.8.0
       */
      @NotNull Source soundSource();
    }
  }

  /**
   * A sound type.
   *
   * @since 4.0.0
   */
  interface Type extends Keyed {
    /**
     * Gets the key.
     *
     * @return the key
     * @since 4.0.0
     */
    @Override
    @NotNull Key key();
  }

  /**
   * An emitter of sounds.
   *
   * @see net.kyori.adventure.audience.Audience#playSound(Sound, Emitter)
   * @since 4.8.0
   */
  interface Emitter {
    /**
     * An emitter representing the recipient of a sound.
     *
     * <p>When used with {@link net.kyori.adventure.audience.Audience#playSound(Sound, Emitter)}, the sound will be emitted from the recipient of the sound.</p>
     *
     * @return the emitter
     * @since 4.8.0
     */
    static @NotNull Emitter self() {
      return SoundImpl.EMITTER_SELF;
    }
  }

  /**
   * A builder for sound instances.
   *
   * <p>Type is required, all other options are optional.</p>
   *
   * @since 4.12.0
   */
  interface Builder extends AbstractBuilder<Sound> {
    /**
     * Set the type of this sound.
     *
     * <p>Required.</p>
     *
     * @param type resource location of the sound event to play
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder type(final @NotNull Key type);

    /**
     * Set the type of this sound.
     *
     * <p>Required.</p>
     *
     * @param type a type of sound to play
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder type(final @NotNull Type type);

    /**
     * Set the type of this sound.
     *
     * <p>Required.</p>
     *
     * @param typeSupplier a type of sound to play, evaluated lazily
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder type(final @NotNull Supplier<? extends Type> typeSupplier);

    /**
     * A {@link Source} to tell the game where the sound is coming from.
     *
     * <p>By default, {@link Source#MASTER} is used.</p>
     *
     * @param source a source
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder source(final @NotNull Source source);

    /**
     * A {@link Source} to tell the game where the sound is coming from.
     *
     * <p>By default, {@link Source#MASTER} is used.</p>
     *
     * @param source a source provider, evaluated eagerly
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder source(final Source.@NotNull Provider source);

    /**
     * The volume for this sound, indicating how far away it can be heard.
     *
     * <p>Default value is {@code 1}.</p>
     *
     * @param volume the sound volume
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder volume(final @Range(from = 0, to = Integer.MAX_VALUE) float volume);

    /**
     * The pitch for this sound, indicating how high or low the sound can be heard.
     *
     * <p>Default value is {@code 1}.</p>
     *
     * @param pitch the sound pitch
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder pitch(final @Range(from = -1, to = 1) float pitch);

    /**
     * The seed for this sound, used for weighted choices.
     *
     * <p>The default seed is the world seed of the receiver's current world.</p>
     *
     * @param seed the seed
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder seed(final long seed);

    /**
     * The seed for this sound, used for weighted choices.
     *
     * <p>The default seed is the world seed of the receiver's current world.</p>
     *
     * @param seed the seed
     * @return this builder
     * @since 4.12.0
     */
    @NotNull Builder seed(final @NotNull OptionalLong seed);
  }
}
