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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.MonkeyBars;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class ResourcePackRequestImpl implements ResourcePackRequest {
  private final List<ResourcePackInfo> packs;
  private final ResourcePackCallback cb;
  private final boolean replace;
  private final boolean required;
  private final @Nullable Component prompt;

  ResourcePackRequestImpl(final List<ResourcePackInfo> packs, final ResourcePackCallback cb, final boolean replace, final boolean required, final @Nullable Component prompt) {
    this.packs = packs;
    this.cb = cb;
    this.replace = replace;
    this.required = required;
    this.prompt = prompt;
  }

  @Override
  public @NotNull List<ResourcePackInfo> packs() {
    return this.packs;
  }

  @Override
  @SuppressWarnings("UndefinedEquals")
  public @NotNull ResourcePackRequest packs(final@NotNull Iterable<? extends ResourcePackInfoLike> packs) {
    if (this.packs.equals(packs)) return this;

    return new ResourcePackRequestImpl(
      MonkeyBars.toUnmodifiableList(ResourcePackInfoLike::asResourcePackInfo, packs),
      this.cb,
      this.replace,
      this.required,
      this.prompt
    );
  }

  @Override
  public @NotNull ResourcePackCallback callback() {
    return this.cb;
  }

  @Override
  public @NotNull ResourcePackRequest callback(final @NotNull ResourcePackCallback cb) {
    if (cb == this.cb) return this;

    return new ResourcePackRequestImpl(
      this.packs,
      requireNonNull(cb, "cb"),
      this.replace,
      this.required,
      this.prompt
    );
  }

  @Override
  public boolean replace() {
    return this.replace;
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
  public @NotNull ResourcePackRequest replace(final boolean replace) {
    if (replace == this.replace) return this;

    return new ResourcePackRequestImpl(this.packs, this.cb, replace, this.required, this.prompt);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    final ResourcePackRequestImpl that = (ResourcePackRequestImpl) other;
    return this.replace == that.replace
      && Objects.equals(this.packs, that.packs)
      && Objects.equals(this.cb, that.cb)
      && this.required == that.required
      && Objects.equals(this.prompt, that.prompt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.packs, this.cb, this.replace, this.required, this.prompt);
  }

  @Override
  public @NotNull String toString() {
    return Internals.toString(this);
  }

  @Override
  public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("packs", this.packs),
      ExaminableProperty.of("callback", this.cb),
      ExaminableProperty.of("replace", this.replace),
      ExaminableProperty.of("required", this.required),
      ExaminableProperty.of("prompt", this.prompt)
    );
  }

  static final class BuilderImpl implements Builder {
    private List<ResourcePackInfo> packs;
    private ResourcePackCallback cb;
    private boolean replace;
    private boolean required;
    private @Nullable Component prompt;

    BuilderImpl() {
      this.packs = Collections.emptyList();
      this.cb = ResourcePackCallback.noOp();
      this.replace = false;
    }

    BuilderImpl(final @NotNull ResourcePackRequest req) {
      this.packs = req.packs();
      this.cb = req.callback();
      this.replace = req.replace();
      this.required = req.required();
      this.prompt = req.prompt();
    }

    @Override
    public @NotNull Builder packs(final @NotNull ResourcePackInfoLike first, final @NotNull ResourcePackInfoLike @NotNull ... others) {
      this.packs = MonkeyBars.nonEmptyArrayToList(ResourcePackInfoLike::asResourcePackInfo, first, others);
      return this;
    }

    @Override
    public @NotNull Builder packs(final @NotNull Iterable<? extends ResourcePackInfoLike> packs) {
      this.packs = MonkeyBars.toUnmodifiableList(ResourcePackInfoLike::asResourcePackInfo, packs);
      return this;
    }

    @Override
    public @NotNull Builder callback(final @NotNull ResourcePackCallback cb) {
      this.cb = requireNonNull(cb, "cb");
      return this;
    }

    @Override
    public @NotNull Builder replace(final boolean replace) {
      this.replace = replace;
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
      return new ResourcePackRequestImpl(this.packs, this.cb, this.replace, this.required, this.prompt);
    }
  }
}
