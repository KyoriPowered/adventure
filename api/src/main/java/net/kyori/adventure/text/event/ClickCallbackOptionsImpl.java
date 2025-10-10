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
package net.kyori.adventure.text.event;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

record ClickCallbackOptionsImpl(int uses, Duration lifetime) implements ClickCallback.Options {
  static final ClickCallback.Options DEFAULT = new BuilderImpl().build();

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("uses", this.uses),
      ExaminableProperty.of("expiration", this.lifetime)
    );
  }

  @Override
  public @NotNull String toString() {
    return Internals.toString(this);
  }

  static final class BuilderImpl implements Builder {
    private static final int DEFAULT_USES = 1;

    private int uses;
    private Duration lifetime;

    BuilderImpl() {
      this.uses = DEFAULT_USES;
      this.lifetime = ClickCallback.DEFAULT_LIFETIME;
    }

    BuilderImpl(final ClickCallback.@NotNull Options existing) {
      this.uses = existing.uses();
      this.lifetime = existing.lifetime();
    }

    @Override
    public ClickCallback.@NotNull Options build() {
      return new ClickCallbackOptionsImpl(this.uses, this.lifetime);
    }

    @Override
    public @NotNull Builder uses(final int uses) {
      this.uses = uses;
      return this;
    }

    @Override
    public @NotNull Builder lifetime(final @NotNull TemporalAmount lifetime) {
      this.lifetime = lifetime instanceof Duration ? (Duration) lifetime : Duration.from(requireNonNull(lifetime, "lifetime"));
      return this;
    }
  }
}
