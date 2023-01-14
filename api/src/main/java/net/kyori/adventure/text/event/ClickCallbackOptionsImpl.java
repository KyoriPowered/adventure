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
package net.kyori.adventure.text.event;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class ClickCallbackOptionsImpl implements ClickCallback.Options {
  static final ClickCallback.Options DEFAULT = new ClickCallbackOptionsImpl.BuilderImpl().build();

  private final boolean multiUse;
  private final Duration lifetime;

  ClickCallbackOptionsImpl(final boolean multiUse, final Duration lifetime) {
    this.multiUse = multiUse;
    this.lifetime = lifetime;
  }

  @Override
  public boolean multiUse() {
    return this.multiUse;
  }

  @Override
  public @NotNull Duration lifetime() {
    return this.lifetime;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("multiUse", this.multiUse),
      ExaminableProperty.of("expiration", this.lifetime)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  static final class BuilderImpl implements Builder {
    private boolean multiUse;
    private Duration lifetime;

    BuilderImpl() {
      this.lifetime = Duration.ofHours(12);
    }

    BuilderImpl(final ClickCallback.@NotNull Options existing) {
      this.multiUse = existing.multiUse();
      this.lifetime = existing.lifetime();
    }

    @Override
    public ClickCallback.@NotNull Options build() {
      return new ClickCallbackOptionsImpl(this.multiUse, this.lifetime);
    }

    @Override
    public @NotNull Builder multiUse(final boolean multiUse) {
      this.multiUse = multiUse;
      return this;
    }

    @Override
    public @NotNull Builder lifetime(final @NotNull TemporalAmount lifetime) {
      this.lifetime = lifetime instanceof Duration ? (Duration) lifetime : Duration.from(requireNonNull(lifetime, "lifetime"));
      return this;
    }
  }
}
