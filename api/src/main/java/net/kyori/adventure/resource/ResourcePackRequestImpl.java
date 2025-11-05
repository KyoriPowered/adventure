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
package net.kyori.adventure.resource;

import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.MonkeyBars;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record ResourcePackRequestImpl(List<ResourcePackInfo> packs, ResourcePackCallback callback, boolean replace, boolean required, @Nullable Component prompt) implements ResourcePackRequest {
  @Override
  @SuppressWarnings("UndefinedEquals")
  public ResourcePackRequest packs(final Iterable<? extends ResourcePackInfoLike> packs) {
    if (this.packs.equals(packs)) return this;

    return new ResourcePackRequestImpl(
      MonkeyBars.toUnmodifiableList(ResourcePackInfoLike::asResourcePackInfo, packs),
      this.callback,
      this.replace,
      this.required,
      this.prompt
    );
  }

  @Override
  public ResourcePackRequest callback(final ResourcePackCallback cb) {
    if (cb == this.callback) return this;

    return new ResourcePackRequestImpl(
      this.packs,
      requireNonNull(cb, "cb"),
      this.replace,
      this.required,
      this.prompt
    );
  }

  @Override
  public ResourcePackRequest replace(final boolean replace) {
    if (replace == this.replace) return this;

    return new ResourcePackRequestImpl(this.packs, this.callback, replace, this.required, this.prompt);
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

    BuilderImpl(final ResourcePackRequest req) {
      this.packs = req.packs();
      this.cb = req.callback();
      this.replace = req.replace();
      this.required = req.required();
      this.prompt = req.prompt();
    }

    @Override
    public Builder packs(final ResourcePackInfoLike first, final ResourcePackInfoLike ... others) {
      this.packs = MonkeyBars.nonEmptyArrayToList(ResourcePackInfoLike::asResourcePackInfo, first, others);
      return this;
    }

    @Override
    public Builder packs(final Iterable<? extends ResourcePackInfoLike> packs) {
      this.packs = MonkeyBars.toUnmodifiableList(ResourcePackInfoLike::asResourcePackInfo, packs);
      return this;
    }

    @Override
    public Builder callback(final ResourcePackCallback cb) {
      this.cb = requireNonNull(cb, "cb");
      return this;
    }

    @Override
    public Builder replace(final boolean replace) {
      this.replace = replace;
      return this;
    }

    @Override
    public Builder required(final boolean required) {
      this.required = required;
      return this;
    }

    @Override
    public Builder prompt(final @Nullable Component prompt) {
      this.prompt = prompt;
      return this;
    }

    @Override
    public ResourcePackRequest build() {
      return new ResourcePackRequestImpl(this.packs, this.cb, this.replace, this.required, this.prompt);
    }
  }
}
