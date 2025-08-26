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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class ObjectComponentImpl extends AbstractComponent implements ObjectComponent {
  private final Contents contents;

  private ObjectComponentImpl(final @NotNull List<Component> children, final @NotNull Style style, final @NotNull Contents contents) {
    super(children, style);
    this.contents = contents;
  }

  @Override
  public @NotNull Contents contents() {
    return this.contents;
  }

  @Override
  public @NotNull ObjectComponent contents(final @NotNull Contents contents) {
    return create(this.children, this.style, contents);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof ObjectComponent)) return false;
    if (!super.equals(other)) return false;
    final ObjectComponentImpl that = (ObjectComponentImpl) other;
    return Objects.equals(this.contents, that.contents());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.contents.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static @NotNull ObjectComponentImpl create(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final @NotNull Contents contents) {
    return new ObjectComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      requireNonNull(contents, "contents")
    );
  }

  @Override
  public @NotNull ObjectComponent children(final @NotNull List<? extends ComponentLike> children) {
    return create(children, this.style, this.contents);
  }

  @Override
  public @NotNull ObjectComponent style(final @NotNull Style style) {
    return create(this.children, style, this.contents);
  }

  static final class SpriteContentsImpl implements SpriteContents {
    private final Key atlas;
    private final Key sprite;

    SpriteContentsImpl(final @NotNull Key atlas, final @NotNull Key sprite) {
      this.atlas = atlas;
      this.sprite = sprite;
    }

    @Override
    public @NotNull Key atlas() {
      return this.atlas;
    }

    @Override
    public @NotNull Key sprite() {
      return this.sprite;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if (this == other) return true;
      if (!(other instanceof SpriteContents)) return false;
      final SpriteContentsImpl that = (SpriteContentsImpl) other;
      return Objects.equals(this.atlas, that.atlas())
        && Objects.equals(this.sprite, that.sprite());
    }

    @Override
    public int hashCode() {
      int result = this.atlas.hashCode();
      result = (31 * result) + this.sprite.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  static final class PlayerHeadContentsImpl implements PlayerHeadContents {
    private final @Nullable String name;
    private final @Nullable UUID id;
    private final Map<String, ProfileProperty> properties;
    private final boolean hat;

    PlayerHeadContentsImpl(
      final @Nullable String name,
      final @Nullable UUID id,
      final @NotNull Map<String, ProfileProperty> properties,
      final boolean hat
    ) {
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
    public @NotNull Map<String, ProfileProperty> properties() {
      return this.properties;
    }

    @Override
    public boolean hat() {
      return this.hat;
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if (this == other) return true;
      if (!(other instanceof PlayerHeadContents)) return false;
      final PlayerHeadContentsImpl that = (PlayerHeadContentsImpl) other;
      return Objects.equals(name, that.name)
        && Objects.equals(id, that.id)
        && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.name, this.id, this.properties);
    }

    @Override
    public String toString() {
      return Internals.toString(this);
    }
  }

  static final class ProfilePropertyImpl implements PlayerHeadContents.ProfileProperty {
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
      return Objects.equals(value, that.value)
        && Objects.equals(signature, that.signature);
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

  static final class PlayerHeadContentsBuilderImpl implements PlayerHeadContents.Builder {
    private @Nullable String name;
    private @Nullable UUID id;
    private final Map<String, PlayerHeadContents.ProfileProperty> properties = new HashMap<>();
    private boolean hat;

    @Override
    public PlayerHeadContents.@NotNull Builder name(final @Nullable String name) {
      this.name = name;
      return this;
    }

    @Override
    public PlayerHeadContents.@NotNull Builder id(final @Nullable UUID id) {
      this.id = id;
      return this;
    }

    @Override
    public PlayerHeadContents.@NotNull Builder property(final @NotNull String name, final PlayerHeadContents.@NotNull ProfileProperty property) {
      this.properties.put(requireNonNull(name, "name"), requireNonNull(property, "property"));
      return this;
    }

    @Override
    public PlayerHeadContents.@NotNull Builder property(final @NotNull String name, final @NotNull String value, final @Nullable String signature) {
      this.properties.put(requireNonNull(name, "name"), new ProfilePropertyImpl(requireNonNull(value, "value"), signature));
      return this;
    }

    @Override
    public PlayerHeadContents.@NotNull Builder property(final @NotNull String name, final @NotNull String value) {
      this.properties.put(requireNonNull(name, "name"), new ProfilePropertyImpl(requireNonNull(value, "value"), null));
      return this;
    }

    @Override
    public PlayerHeadContents.@NotNull Builder properties(final @NotNull Map<String, PlayerHeadContents.ProfileProperty> properties) {
      this.properties.putAll(requireNonNull(properties, "properties"));
      return this;
    }

    @Override
    public PlayerHeadContents.@NotNull Builder skin(final PlayerHeadContents.@NotNull SkinSource skinSource) {
      requireNonNull(skinSource, "skinSource").applySkinToPlayerHeadContents(this);
      return this;
    }

    @Override
    public PlayerHeadContents.@NotNull Builder hat(final boolean hat) {
      this.hat = hat;
      return this;
    }

    @Override
    public @NotNull PlayerHeadContents build() {
      return new PlayerHeadContentsImpl(this.name, this.id, this.properties, this.hat);
    }
  }

  static final class BuilderImpl extends AbstractComponentBuilder<ObjectComponent, ObjectComponent.Builder> implements ObjectComponent.Builder {
    private Contents contents;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull ObjectComponent component) {
      super(component);
      this.contents = component.contents();
    }

    @Override
    public @NotNull Builder contents(final @NotNull Contents contents) {
      this.contents = requireNonNull(contents, "contents");
      return this;
    }

    @Override
    public @NotNull ObjectComponent build() {
      if (this.contents == null) throw new IllegalStateException("contents must be set");
      return create(this.children, this.buildStyle(), this.contents);
    }
  }
}
