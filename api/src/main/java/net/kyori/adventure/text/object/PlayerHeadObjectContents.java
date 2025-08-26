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

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
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
 * @sinceMinecraft 1.21.9
 * @since 4.25.0
 */
public interface PlayerHeadObjectContents extends ObjectContents {
  @Nullable String name();

  @Nullable UUID id();

  @Unmodifiable
  @NotNull Map<String, ProfileProperty> properties();

  boolean hat();

  static ProfileProperty property(final @NotNull String value) {
    return new PlayerHeadObjectContentsImpl.ProfilePropertyImpl(requireNonNull(value, "value"), null);
  }

  static ProfileProperty property(final @NotNull String value, final @Nullable String signature) {
    return new PlayerHeadObjectContentsImpl.ProfilePropertyImpl(requireNonNull(value, "value"), signature);
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

    @Contract(value = "_, _, _ -> this")
    @NotNull Builder property(final @NotNull String name, final @NotNull String value, final @Nullable String signature);

    @Contract(value = "_, _ -> this")
    @NotNull Builder property(final @NotNull String name, final @NotNull String value);

    @Contract(value = "_ -> this")
    @NotNull Builder properties(final @NotNull Map<String, ProfileProperty> properties);

    @Contract(value = "_ -> this")
    @NotNull Builder skin(final @NotNull SkinSource skinSource);

    @Contract(value = "_ -> this")
    @NotNull Builder hat(final boolean hat);

    @Contract(value = "-> new", pure = true)
    @NotNull PlayerHeadObjectContents build();
  }

  interface SkinSource {
    @PlatformAPI
    @ApiStatus.Internal
    void applySkinToPlayerHeadContents(@NotNull Builder builder);
  }
}
