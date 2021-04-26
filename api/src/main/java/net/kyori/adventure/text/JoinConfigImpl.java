/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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

import java.util.Objects;
import java.util.function.UnaryOperator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

final class JoinConfigImpl implements JoinConfig {
  static final JoinConfig NULL = new JoinConfigImpl(null, null, null, null, UnaryOperator.identity());

  private final ComponentLike separator;
  private final ComponentLike lastSeparator;
  private final ComponentLike prefix;
  private final ComponentLike suffix;
  private final UnaryOperator<ComponentLike> operator;

  JoinConfigImpl(final @Nullable ComponentLike separator, final @Nullable ComponentLike lastSeparator, final @Nullable ComponentLike prefix, final @Nullable ComponentLike suffix, final @NotNull UnaryOperator<ComponentLike> operator) {
    this.separator = separator;
    this.lastSeparator = lastSeparator;
    this.prefix = prefix;
    this.suffix = suffix;
    this.operator = operator;
  }

  @Override
  public @Nullable ComponentLike prefix() {
    return this.prefix;
  }

  @Override
  public @Nullable ComponentLike suffix() {
    return this.suffix;
  }

  @Override
  public @Nullable ComponentLike separator() {
    return this.separator;
  }

  @Override
  public @Nullable ComponentLike lastSeparator() {
    return this.lastSeparator;
  }

  @Override
  public @NotNull UnaryOperator<ComponentLike> operator() {
    return this.operator;
  }

  @Override
  public JoinConfig.@NonNull Builder toBuilder() {
    return new BuilderImpl(this.separator, this.lastSeparator, this.prefix, this.suffix, this.operator);
  }

  static final class BuilderImpl implements JoinConfig.Builder {
    private ComponentLike separator;
    private ComponentLike lastSeparator;
    private ComponentLike prefix;
    private ComponentLike suffix;
    private UnaryOperator<ComponentLike> operator;

    BuilderImpl() {
      this(null, null, null, null, UnaryOperator.identity());
    }

    private BuilderImpl(final @Nullable ComponentLike separator, final @Nullable ComponentLike lastSeparator, final @Nullable ComponentLike prefix, final @Nullable ComponentLike suffix, final @NotNull UnaryOperator<ComponentLike> operator) {
      this.separator = separator;
      this.lastSeparator = lastSeparator;
      this.prefix = prefix;
      this.suffix = suffix;
      this.operator = operator;
    }

    @Override
    public @NonNull JoinConfig build() {
      return new JoinConfigImpl(this.separator, this.lastSeparator, this.prefix, this.suffix, this.operator);
    }

    @Override
    public @Nullable ComponentLike prefix() {
      return this.prefix;
    }

    @Override
    public @NonNull Builder prefix(final @Nullable ComponentLike prefix) {
      this.prefix = prefix;
      return this;
    }

    @Override
    public @Nullable ComponentLike suffix() {
      return this.suffix;
    }

    @Override
    public @NonNull Builder suffix(final @Nullable ComponentLike suffix) {
      this.suffix = suffix;
      return this;
    }

    @Override
    public @Nullable ComponentLike separator() {
      return this.separator;
    }

    @Override
    public @NonNull Builder separator(final @Nullable ComponentLike separator) {
      this.separator = separator;
      return this;
    }

    @Override
    public @Nullable ComponentLike lastSeparator() {
      return this.lastSeparator;
    }

    @Override
    public @NonNull Builder lastSeparator(final @Nullable ComponentLike lastSeparator) {
      this.lastSeparator = lastSeparator;
      return this;
    }

    @Override
    public @NotNull UnaryOperator<ComponentLike> operator() {
      return this.operator;
    }

    @Override
    public @NonNull Builder operator(final @NotNull UnaryOperator<ComponentLike> operator) {
      this.operator = Objects.requireNonNull(operator, "operator");
      return this;
    }
  }
}
