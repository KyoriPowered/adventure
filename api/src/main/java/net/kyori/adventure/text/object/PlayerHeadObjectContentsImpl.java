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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record PlayerHeadObjectContentsImpl(@Nullable String name, @Nullable UUID id, List<ProfileProperty> profileProperties, boolean hat, @Nullable Key texture) implements PlayerHeadObjectContents {

  @Override
  public Builder toBuilder() {
    return new BuilderImpl(this);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  record ProfilePropertyImpl(String name, String value, @Nullable String signature) implements ProfileProperty {
    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  static final class BuilderImpl implements Builder {
    private @Nullable String name;
    private @Nullable UUID id;
    private final List<ProfileProperty> properties = new ArrayList<>();
    private boolean hat = true;
    private @Nullable Key texture;

    BuilderImpl() {
    }

    BuilderImpl(final PlayerHeadObjectContentsImpl playerHeadObjectContents) {
      this.name = playerHeadObjectContents.name;
      this.id = playerHeadObjectContents.id;
      this.properties.addAll(playerHeadObjectContents.profileProperties);
      this.hat = playerHeadObjectContents.hat;
      this.texture = playerHeadObjectContents.texture;
    }

    @Override
    public Builder name(final @Nullable String name) {
      this.name = name;
      return this;
    }

    @Override
    public Builder id(final @Nullable UUID id) {
      this.id = id;
      return this;
    }

    @Override
    public Builder profileProperty(final ProfileProperty property) {
      this.properties.add(requireNonNull(property, "property"));
      return this;
    }

    @Override
    public Builder profileProperties(final Collection<ProfileProperty> properties) {
      for (final ProfileProperty property : requireNonNull(properties, "properties")) {
        this.profileProperty(property);
      }
      return this;
    }

    private void clearProfile() {
      this.name = null;
      this.id = null;
      this.properties.clear();
      this.texture = null;
    }

    @Override
    public Builder skin(final SkinSource skinSource) {
      this.clearProfile(); // intent of this method is to override any existing profile data
      requireNonNull(skinSource, "skinSource").applySkinToPlayerHeadContents(this);
      return this;
    }

    @Override
    public Builder hat(final boolean hat) {
      this.hat = hat;
      return this;
    }

    @Override
    public Builder texture(final @Nullable Key texture) {
      this.texture = texture;
      return this;
    }

    @Override
    public PlayerHeadObjectContents build() {
      return new PlayerHeadObjectContentsImpl(this.name, this.id, List.copyOf(this.properties), this.hat, this.texture);
    }
  }
}
