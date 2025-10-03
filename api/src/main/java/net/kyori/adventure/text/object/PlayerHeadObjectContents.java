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
package net.kyori.adventure.text.object;

import java.util.Collection;
import java.util.List;
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
 * A player head contents.
 *
 * <p>This object closely mirrors the serialized form of the component contents. This means
 * the game will use it's standard heuristics to determine whether the profile needs resolving
 * before display. As of 1.21.9, the profile will be resolved if the name or id is present without
 * any properties.</p>
 *
 * @since 4.25.0
 * @sinceMinecraft 1.21.9
 */
@ApiStatus.NonExtendable
public interface PlayerHeadObjectContents extends ObjectContents {
  /**
   * The default value for whether the player's hat layer should render.
   *
   * @since 4.25.0
   */
  boolean DEFAULT_HAT = true;

  /**
   * Gets the name of the player if present.
   *
   * @return the name of the player or null
   * @since 4.25.0
   */
  @Nullable String name();

  /**
   * Gets the UUID of the player if present.
   *
   * @return the UUID of the player or null
   * @since 4.25.0
   */
  @Nullable UUID id();

  /**
   * Gets the profile properties for the player.
   *
   * @return the properties
   * @since 4.25.0
   */
  @Unmodifiable
  @NotNull List<ProfileProperty> profileProperties();

  /**
   * Whether the player head should render the player's hat layer.
   *
   * @return whether to render the hat layer
   * @since 4.25.0
   */
  boolean hat();

  /**
   * Optional namespaced ID of the skin texture to use for rendering.
   *
   * <p>The skin is specified relative to the textures folder and with a .png suffix
   * e.g. entity/player/wide/steve will use the default wide Steve skin.</p>
   *
   * <p>Overrides the skin specified by the profile properties if present.</p>
   *
   * @return the texture key
   * @since 4.25.0
   */
  @Nullable Key texture();

  /**
   * Creates a builder from the state of this object.
   *
   * @return a new builder
   * @since 4.25.0
   */
  @Contract(value = "-> new", pure = true)
  @NotNull Builder toBuilder();

  /**
   * Creates a profile property with the given value and no signature.
   *
   * @param name  the name
   * @param value the value
   * @return a profile property
   * @since 4.25.0
   */
  @Contract(value = "_, _ -> new", pure = true)
  static ProfileProperty property(final @NotNull String name, final @NotNull String value) {
    return new PlayerHeadObjectContentsImpl.ProfilePropertyImpl(requireNonNull(name, "name"), requireNonNull(value, "value"), null);
  }

  /**
   * Creates a profile property with the given value and signature.
   *
   * @param name      the name
   * @param value     the value
   * @param signature the signature, may be null
   * @return a profile property
   * @since 4.25.0
   */
  @Contract(value = "_, _, _ -> new", pure = true)
  static ProfileProperty property(final @NotNull String name, final @NotNull String value, final @Nullable String signature) {
    return new PlayerHeadObjectContentsImpl.ProfilePropertyImpl(requireNonNull(name, "name"), requireNonNull(value, "value"), signature);
  }

  /**
   * A player profile property value with an optional signature.
   *
   * @since 4.25.0
   */
  interface ProfileProperty extends Examinable {
    /**
     * Gets the name of the property.
     *
     * @return the name
     * @since 4.25.0
     */
    @NotNull String name();

    /**
     * Gets the value of the property.
     *
     * @return the value
     * @since 4.25.0
     */
    @NotNull String value();

    /**
     * Gets the signature of the property, if present.
     *
     * @return the signature or null
     * @since 4.25.0
     */
    @Nullable String signature();

    @Override
    default @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("name", this.name()),
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
      ExaminableProperty.of("profileProperties", this.profileProperties()),
      ExaminableProperty.of("hat", this.hat()),
      ExaminableProperty.of("texture", this.texture())
    );
  }

  /**
   * A builder for a {@link PlayerHeadObjectContents}.
   *
   * @since 4.25.0
   */
  interface Builder {
    /**
     * Sets the name of the player.
     *
     * @param name the name of the player, may be null
     * @return this builder
     * @since 4.25.0
     */
    @Contract(value = "_ -> this")
    @NotNull Builder name(final @Nullable String name);

    /**
     * Sets the UUID of the player.
     *
     * @param id the UUID of the player, may be null
     * @return this builder
     * @since 4.25.0
     */
    @Contract(value = "_ -> this")
    @NotNull Builder id(final @Nullable UUID id);

    /**
     * Sets a profile property.
     *
     * @param property the property
     * @return this builder
     * @since 4.25.0
     */
    @Contract(value = "_ -> this")
    @NotNull Builder profileProperty(final @NotNull ProfileProperty property);

    /**
     * Sets multiple profile properties.
     *
     * @param properties the properties
     * @return this builder
     * @since 4.25.0
     */
    @Contract(value = "_ -> this")
    @NotNull Builder profileProperties(final @NotNull Collection<ProfileProperty> properties);

    /**
     * Sets the skin (name, id, properties, and texture) from the given source, overriding any existing values.
     *
     * @param skinSource the skin source
     * @return this builder
     * @since 4.25.0
     */
    @Contract(value = "_ -> this")
    @NotNull Builder skin(final @NotNull SkinSource skinSource);

    /**
     * Sets whether the player head should render the player's hat layer.
     *
     * <p>Default is {@code true}.</p>
     *
     * @param hat whether to render the hat layer
     * @return this builder
     * @since 4.25.0
     */
    @Contract(value = "_ -> this")
    @NotNull Builder hat(final boolean hat);

    /**
     * Sets the optional namespaced ID of the skin texture to use for rendering.
     *
     * <p>The skin is specified relative to the textures folder and with a .png suffix
     * e.g. entity/player/wide/steve will use the default wide Steve skin.</p>
     *
     * <p>Overrides the skin specified by the profile properties if present.</p>
     *
     * @param texture the texture key or null
     * @return this builder
     * @since 4.25.0
     */
    @Contract(value = "_ -> this")
    @NotNull Builder texture(final @Nullable Key texture);

    /**
     * Builds the player head contents.
     *
     * @return a new player head contents
     * @since 4.25.0
     */
    @Contract(value = "-> new", pure = true)
    @NotNull PlayerHeadObjectContents build();
  }

  /**
   * A source of player skin data.
   *
   * @see Builder#skin(SkinSource)
   * @see ObjectContents#playerHead(SkinSource)
   * @since 4.25.0
   */
  interface SkinSource {
    /**
     * Applies this skin source to the given player head contents builder.
     *
     * <p>The name, id, and properties will be cleared prior to calling by the builder.</p>
     *
     * @param builder builder to apply to
     */
    @PlatformAPI
    @ApiStatus.Internal
    void applySkinToPlayerHeadContents(@NotNull Builder builder);
  }
}
