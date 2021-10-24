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

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

final class SelectorComponentImpl extends AbstractComponent implements SelectorComponent {
  private final String pattern;
  private final @Nullable Component separator;

  SelectorComponentImpl(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final @NotNull String pattern, final @Nullable ComponentLike separator) {
    super(children, style);
    this.pattern = pattern;
    this.separator = ComponentLike.unbox(separator);
  }

  @Override
  public @NotNull String pattern() {
    return this.pattern;
  }

  @Override
  public @NotNull SelectorComponent pattern(final @NotNull String pattern) {
    if (Objects.equals(this.pattern, pattern)) return this;
    return new SelectorComponentImpl(this.children, this.style, requireNonNull(pattern, "pattern"), this.separator);
  }

  @Override
  public @Nullable Component separator() {
    return this.separator;
  }

  @Override
  public @NotNull SelectorComponent separator(final @Nullable ComponentLike separator) {
    return new SelectorComponentImpl(this.children, this.style, this.pattern, separator);
  }

  @Override
  public @NotNull SelectorComponent children(final @NotNull List<? extends ComponentLike> children) {
    return new SelectorComponentImpl(requireNonNull(children, "children"), this.style, this.pattern, this.separator);
  }

  @Override
  public @NotNull SelectorComponent style(final @NotNull Style style) {
    return new SelectorComponentImpl(this.children, requireNonNull(style, "style"), this.pattern, this.separator);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof SelectorComponent)) return false;
    if (!super.equals(other)) return false;
    final SelectorComponent that = (SelectorComponent) other;
    return Objects.equals(this.pattern, that.pattern()) && Objects.equals(this.separator, that.separator());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.pattern.hashCode();
    result = (31 * result) + Objects.hashCode(this.separator);
    return result;
  }

  @Override
  protected @NotNull Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("pattern", this.pattern),
        ExaminableProperty.of("separator", this.separator)
      ),
      super.examinablePropertiesWithoutChildren()
    );
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractComponentBuilder<SelectorComponent, Builder> implements SelectorComponent.Builder {
    private @Nullable String pattern;
    private @Nullable Component separator;

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull SelectorComponent component) {
      super(component);
      this.pattern = component.pattern();
    }

    @Override
    public @NotNull Builder pattern(final @NotNull String pattern) {
      this.pattern = pattern;
      return this;
    }

    @Override
    public @NotNull Builder separator(final @Nullable ComponentLike separator) {
      this.separator = ComponentLike.unbox(separator);
      return this;
    }

    @Override
    public @NotNull SelectorComponent build() {
      if (this.pattern == null) throw new IllegalStateException("pattern must be set");
      return new SelectorComponentImpl(this.children, this.buildStyle(), this.pattern, this.separator);
    }
  }
}
