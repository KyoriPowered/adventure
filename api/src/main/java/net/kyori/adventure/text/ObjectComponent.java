/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.text;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.PlatformAPI;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import static java.util.Objects.requireNonNull;

/**
 * Displays a non-text object.
 *
 * @since 4.25.0
 * @sinceMinecraft 1.21.9
 */
public interface ObjectComponent extends BuildableComponent<ObjectComponent, ObjectComponent.Builder>, ScopedComponent<ObjectComponent> {
  /**
   * Gets the contents of this object component.
   *
   * @return the contents
   * @since 4.25.0
   */
  @NotNull Contents contents();

  /**
   * Creates a copy of this object component with the given contents.
   *
   * @param contents the contents to set
   * @return new object component
   * @since 4.25.0
   */
  @NotNull ObjectComponent contents(@NotNull Contents contents);

  @Override
  default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(ExaminableProperty.of("contents", this.contents())),
      BuildableComponent.super.examinableProperties()
    );
  }

  /**
   * An object component contents.
   *
   * @since 4.25.0
   */
  /*sealed*/ interface Contents extends Examinable /*permits SpriteContents, PlayerHeadContents*/ {
    /**
     * Creates a sprite contents with the given atlas and sprite.
     *
     * @param atlas the atlas
     * @param sprite the sprite
     * @return a sprite contents
     * @since 4.25.0
     */
    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull SpriteContents sprite(final @NotNull Key atlas, final @NotNull Key sprite) {
      return new ObjectComponentImpl.SpriteContentsImpl(requireNonNull(atlas, "atlas"), requireNonNull(sprite, "sprite"));
    }

    /**
     * Creates a sprite contents with the given sprite and the default atlas.
     *
     * @param sprite the sprite
     * @return a sprite contents
     * @since 4.25.0
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull SpriteContents sprite(final @NotNull Key sprite) {
      return new ObjectComponentImpl.SpriteContentsImpl(SpriteContents.DEFAULT_ATLAS, requireNonNull(sprite, "sprite"));
    }

    /**
     * Creates a player head contents builder.
     *
     * @return a player head contents builder
     * @since 4.25.0
     */
    @Contract(value = "-> new", pure = true)
    static PlayerHeadContents.@NotNull Builder playerHead() {
      return new ObjectComponentImpl.PlayerHeadContentsBuilderImpl();
    }

    /**
     * Creates a player head contents with the given parameters.
     *
     * @param name the player name, may be null
     * @param id the player UUID, may be null
     * @param hat whether to show the hat layer
     * @return a player head contents
     * @since 4.25.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull PlayerHeadContents playerHead(final @Nullable String name, final @Nullable UUID id, final boolean hat) {
      return new ObjectComponentImpl.PlayerHeadContentsImpl(name, id, Collections.emptyMap(), hat);
    }

    /**
     * Creates a player head contents with the given parameters.
     *
     * @param name the player name, may be null
     * @param id the player UUID, may be null
     * @param properties the player properties, must not be null
     * @param hat whether to show the hat layer
     * @return a player head contents
     * @since 4.25.0
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull PlayerHeadContents playerHead(final @Nullable String name, final @Nullable UUID id, final @NotNull Map<String, PlayerHeadContents.ProfileProperty> properties, final boolean hat) {
      return new ObjectComponentImpl.PlayerHeadContentsImpl(name, id, requireNonNull(properties, "properties"), hat);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull PlayerHeadContents playerHead(final PlayerHeadContents.@NotNull SkinSource skinSource, final boolean hat) {
      return playerHead().skin(skinSource).hat(hat).build();
    }
  }

  /**
   * A sprite contents.
   *
   * <p>Represents a sprite in an atlas, such as a block texture.</p>
   *
   * @since 4.25.0
   * @sinceMinecraft 1.21.9
   */
  interface SpriteContents extends Contents {
    /**
     * The default atlas key for sprites, used by vanilla when the atlas is not specified in a serialized object component.
     *
     * @since 4.25.0
     */
    Key DEFAULT_ATLAS = Key.key("minecraft:blocks");

    /**
     * Gets the atlas key.
     *
     * @return the atlas key
     * @since 4.25.0
     */
    @NotNull Key atlas();

    /**
     * Gets the sprite key.
     *
     * @return the sprite key
     * @since 4.25.0
     */
    @NotNull Key sprite();

    @Override
    default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("atlas", this.atlas()),
        ExaminableProperty.of("sprite", this.sprite())
      );
    }
  }

  /**
   * A player head contents.
   *
   * @since 4.25.0
   * @sinceMinecraft 1.21.9
   */
  interface PlayerHeadContents extends Contents {
    @Nullable String name();

    @Nullable UUID id();

    @Unmodifiable
    @NotNull Map<String, ProfileProperty> properties();

    boolean hat();

    static ProfileProperty property(final @NotNull String value, final @Nullable String signature) {
      return new ObjectComponentImpl.ProfilePropertyImpl(requireNonNull(value, "value"), signature);
    }

    interface ProfileProperty extends Examinable {
      @NotNull String value();

      @Nullable String signature();

      @Override
      default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
          ExaminableProperty.of("value", this.value()),
          ExaminableProperty.of("signature", this.signature())
        );
      }
    }

    @Override
    default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("name", this.name()),
        ExaminableProperty.of("id", this.id()),
        ExaminableProperty.of("properties", this.properties())
      );
    }

    interface Builder {
      @Contract(value = "_ -> this")
      @NotNull Builder name(final @Nullable String name);

      @Contract(value = "_ -> this")
      @NotNull Builder id(final @Nullable UUID id);

      @Contract(value = "_, _ -> this")
      @NotNull Builder property(final @NotNull String name, final @NotNull ProfileProperty property);

      @Contract(value = "_ -> this")
      @NotNull Builder properties(final @NotNull Map<String, ProfileProperty> properties);

      @Contract(value = "_ -> this")
      @NotNull Builder skin(final @NotNull SkinSource skinSource);

      @Contract(value = "_ -> this")
      @NotNull Builder hat(final boolean hat);

      @Contract(value = "-> new", pure = true)
      @NotNull PlayerHeadContents build();
    }

    interface SkinSource {
      @PlatformAPI
      @ApiStatus.Internal
      void applySkinToPlayerHeadContents(PlayerHeadContents.@NotNull Builder builder);
    }
  }

  /**
   * An object component builder.
   *
   * @since 4.25.0
   */
  interface Builder extends ComponentBuilder<ObjectComponent, Builder> {
    /**
     * Sets the contents of this object component builder.
     *
     * @param contents the contents to set
     * @return this builder
     * @since 4.25.0
     */
    @NotNull Builder contents(@NotNull Contents contents);
  }
}
