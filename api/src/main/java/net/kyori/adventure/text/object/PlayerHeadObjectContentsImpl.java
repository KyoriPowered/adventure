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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.kyori.adventure.internal.Internals;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class PlayerHeadObjectContentsImpl implements PlayerHeadObjectContents {
  private final @Nullable String name;
  private final @Nullable UUID id;
  private final Map<String, ProfileProperty> properties;
  private final boolean hat;

  PlayerHeadObjectContentsImpl(
    final @Nullable String name,
    final @Nullable UUID id,
    final @NotNull Map<String, ProfileProperty> properties,
    final boolean hat
  ) {
    if (name == null && id == null && properties.isEmpty()) {
      throw new IllegalArgumentException("At least one of name, id, or properties must be non-null/non-empty");
    }
    this.name = name;
    this.id = id;
    if (properties.isEmpty()) {
      this.properties = Collections.emptyMap();
    } else {
      this.properties = Collections.unmodifiableMap(new HashMap<>(requireNonNull(properties, "properties")));
    }
    this.hat = hat;
  }

  @Override
  public @Nullable String name() {
    return this.name;
  }

  @Override
  public @Nullable UUID id() {
    return this.id;
  }

  @Override
  public @NotNull Map<String, ProfileProperty> profileProperties() {
    return this.properties;
  }

  @Override
  public boolean hat() {
    return this.hat;
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof PlayerHeadObjectContents)) return false;
    final PlayerHeadObjectContentsImpl that = (PlayerHeadObjectContentsImpl) other;
    return Objects.equals(this.name, that.name)
      && Objects.equals(this.id, that.id)
      && Objects.equals(this.properties, that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.id, this.properties);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  static final class ProfilePropertyImpl implements PlayerHeadObjectContents.ProfileProperty {
    private final String value;
    private final @Nullable String signature;

    ProfilePropertyImpl(final @NotNull String value, final @Nullable String signature) {
      this.value = value;
      this.signature = signature;
    }

    @Override
    public @NotNull String value() {
      return this.value;
    }

    @Override
    public @Nullable String signature() {
      return this.signature;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if (this == other) return true;
      if (!(other instanceof ProfilePropertyImpl)) return false;
      final ProfilePropertyImpl that = (ProfilePropertyImpl) other;
      return Objects.equals(this.value, that.value)
        && Objects.equals(this.signature, that.signature);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.value, this.signature);
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  static final class BuilderImpl implements PlayerHeadObjectContents.Builder {
    private @Nullable String name;
    private @Nullable UUID id;
    private final Map<String, PlayerHeadObjectContents.ProfileProperty> properties = new HashMap<>();
    private boolean hat;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull PlayerHeadObjectContentsImpl playerHeadObjectContents) {
      this.name = playerHeadObjectContents.name;
      this.id = playerHeadObjectContents.id;
      this.properties.putAll(playerHeadObjectContents.properties);
      this.hat = playerHeadObjectContents.hat;
    }

    @Override
    public PlayerHeadObjectContents.@NotNull Builder name(final @Nullable String name) {
      this.name = name;
      return this;
    }

    @Override
    public PlayerHeadObjectContents.@NotNull Builder id(final @Nullable UUID id) {
      this.id = id;
      return this;
    }

    @Override
    public PlayerHeadObjectContents.@NotNull Builder profileProperty(final @NotNull String name, final PlayerHeadObjectContents.@NotNull ProfileProperty property) {
      this.properties.put(requireNonNull(name, "name"), requireNonNull(property, "property"));
      return this;
    }

    @Override
    public PlayerHeadObjectContents.@NotNull Builder profileProperty(final @NotNull String name, final @NotNull String value, final @Nullable String signature) {
      this.properties.put(requireNonNull(name, "name"), new ProfilePropertyImpl(requireNonNull(value, "value"), signature));
      return this;
    }

    @Override
    public PlayerHeadObjectContents.@NotNull Builder profileProperty(final @NotNull String name, final @NotNull String value) {
      this.properties.put(requireNonNull(name, "name"), new ProfilePropertyImpl(requireNonNull(value, "value"), null));
      return this;
    }

    @Override
    public PlayerHeadObjectContents.@NotNull Builder profileProperties(final @NotNull Map<String, PlayerHeadObjectContents.ProfileProperty> properties) {
      this.properties.putAll(requireNonNull(properties, "properties"));
      return this;
    }

    private void clearProfile() {
      this.name = null;
      this.id = null;
      this.properties.clear();
    }

    @Override
    public PlayerHeadObjectContents.@NotNull Builder skin(final PlayerHeadObjectContents.@NotNull SkinSource skinSource) {
      this.clearProfile(); // intent of this method is to override any existing profile data
      requireNonNull(skinSource, "skinSource").applySkinToPlayerHeadContents(this);
      return this;
    }

    @Override
    public PlayerHeadObjectContents.@NotNull Builder hat(final boolean hat) {
      this.hat = hat;
      return this;
    }

    @Override
    public @NotNull PlayerHeadObjectContents build() {
      return new PlayerHeadObjectContentsImpl(this.name, this.id, this.properties, this.hat);
    }
  }
}
