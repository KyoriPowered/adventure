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
package net.kyori.adventure.resource;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class ResourcePackRequestImpl implements ResourcePackRequest {
  private final UUID id;
  private final URI uri;
  private final String hash;
  private final boolean required;
  private final Component prompt;

  ResourcePackRequestImpl(final @NotNull UUID id, final @NotNull URI uri, final @NotNull String hash, final boolean required, final @Nullable Component prompt) {
    this.id = requireNonNull(id, "id");
    this.uri = requireNonNull(uri, "uri");
    this.hash = requireNonNull(hash, "hash");
    this.required = required;
    this.prompt = prompt;
  }

  @Override
  public @NotNull UUID id() {
    return this.id;
  }

  @Override
  public @NotNull URI uri() {
    return this.uri;
  }

  @Override
  public @NotNull String hash() {
    return this.hash;
  }

  @Override
  public boolean required() {
    return this.required;
  }

  @Override
  public @Nullable Component prompt() {
    return this.prompt;
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("id", this.id),
      ExaminableProperty.of("uri", this.uri),
      ExaminableProperty.of("hash", this.hash),
      ExaminableProperty.of("required", this.required),
      ExaminableProperty.of("prompt", this.prompt)
    );
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof ResourcePackRequestImpl)) return false;
    final ResourcePackRequestImpl that = (ResourcePackRequestImpl) other;
    return this.id.equals(that.id) &&
           this.uri.equals(that.uri) &&
           this.hash.equals(that.hash) &&
           this.required == that.required &&
           Objects.equals(this.prompt, that.prompt);
  }

  @Override
  public int hashCode() {
    int result = this.id.hashCode();
    result = 31 * result + this.uri.hashCode();
    result = 31 * result + this.hash.hashCode();
    result = 31 * result + (this.required ? 1 : 0);
    result = 31 * result + (this.prompt != null ? this.prompt.hashCode() : 0);
    return result;
  }

  static final class BuilderImpl implements Builder {
    private UUID id;
    private URI uri;
    private String hash;
    private boolean required;
    private Component prompt;

    BuilderImpl() {
    }

    @Override
    public @NotNull Builder id(final @NotNull UUID id) {
      this.id = requireNonNull(id, "id");
      return this;
    }

    @Override
    public @NotNull Builder uri(final @NotNull URI uri) {
      this.uri = requireNonNull(uri, "uri");
      return this;
    }

    @Override
    public @NotNull Builder hash(final @NotNull String hash) {
      this.hash = requireNonNull(hash, "hash");
      return this;
    }

    @Override
    public @NotNull Builder required(final boolean required) {
      this.required = required;
      return this;
    }

    @Override
    public @NotNull Builder prompt(final @Nullable Component prompt) {
      this.prompt = prompt;
      return this;
    }

    @Override
    public @NotNull ResourcePackRequest build() {
      return new ResourcePackRequestImpl(this.id, this.uri, this.hash, this.required, this.prompt);
    }
  }
}
