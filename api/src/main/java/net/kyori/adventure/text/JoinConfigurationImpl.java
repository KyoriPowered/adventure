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
import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

final class JoinConfigurationImpl implements JoinConfiguration {
  static final JoinConfigurationImpl NULL = new JoinConfigurationImpl();

  private final ComponentLike separator;
  private final ComponentLike lastSeparator;
  private final ComponentLike prefix;
  private final ComponentLike suffix;
  private final UnaryOperator<ComponentLike> operator;

  private JoinConfigurationImpl() {
    this.separator = null;
    this.lastSeparator = null;
    this.prefix = null;
    this.suffix = null;
    this.operator = UnaryOperator.identity();
  }

  private JoinConfigurationImpl(final @NotNull BuilderImpl builder) {
    this.separator = builder.separator;
    this.lastSeparator = builder.lastSeparator;
    this.prefix = builder.prefix;
    this.suffix = builder.suffix;
    this.operator = builder.operator;
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
  public JoinConfiguration.@NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("separator", this.separator),
      ExaminableProperty.of("lastSeparator", this.lastSeparator),
      ExaminableProperty.of("prefix", this.prefix),
      ExaminableProperty.of("suffix", this.suffix),
      ExaminableProperty.of("operator", this.operator)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

  static final class BuilderImpl implements JoinConfiguration.Builder {
    private ComponentLike separator;
    private ComponentLike lastSeparator;
    private ComponentLike prefix;
    private ComponentLike suffix;
    private UnaryOperator<ComponentLike> operator;

    BuilderImpl() {
      this(JoinConfigurationImpl.NULL);
    }

    private BuilderImpl(final @NotNull JoinConfigurationImpl joinConfig) {
      this.separator = joinConfig.separator;
      this.lastSeparator = joinConfig.lastSeparator;
      this.prefix = joinConfig.prefix;
      this.suffix = joinConfig.suffix;
      this.operator = joinConfig.operator;
    }

    @Override
    public @NonNull Builder prefix(final @Nullable ComponentLike prefix) {
      this.prefix = prefix;
      return this;
    }

    @Override
    public @NonNull Builder suffix(final @Nullable ComponentLike suffix) {
      this.suffix = suffix;
      return this;
    }

    @Override
    public @NonNull Builder separator(final @Nullable ComponentLike separator) {
      this.separator = separator;
      return this;
    }

    @Override
    public @NonNull Builder lastSeparator(final @Nullable ComponentLike lastSeparator) {
      this.lastSeparator = lastSeparator;
      return this;
    }

    @Override
    public @NonNull Builder operator(final @NotNull UnaryOperator<ComponentLike> operator) {
      this.operator = Objects.requireNonNull(operator, "operator");
      return this;
    }

    @Override
    public @NonNull JoinConfiguration build() {
      return new JoinConfigurationImpl(this);
    }
  }
}
