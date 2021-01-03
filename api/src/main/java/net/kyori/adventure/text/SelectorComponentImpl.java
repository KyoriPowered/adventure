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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

final class SelectorComponentImpl extends AbstractComponent implements SelectorComponent {
  private final String pattern;

  SelectorComponentImpl(final @NonNull List<? extends ComponentLike> children, final @NonNull Style style, final @NonNull String pattern) {
    super(children, style);
    this.pattern = pattern;
  }

  @Override
  public @NonNull String pattern() {
    return this.pattern;
  }

  @Override
  public @NonNull SelectorComponent pattern(final @NonNull String pattern) {
    if(Objects.equals(this.pattern, pattern)) return this;
    return new SelectorComponentImpl(this.children, this.style, requireNonNull(pattern, "pattern"));
  }

  @Override
  public @NonNull SelectorComponent children(final @NonNull List<? extends ComponentLike> children) {
    return new SelectorComponentImpl(children, this.style, this.pattern);
  }

  @Override
  public @NonNull SelectorComponent style(final @NonNull Style style) {
    return new SelectorComponentImpl(this.children, style, this.pattern);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof SelectorComponent)) return false;
    if(!super.equals(other)) return false;
    final SelectorComponent that = (SelectorComponent) other;
    return Objects.equals(this.pattern, that.pattern());
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.pattern.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("pattern", this.pattern)
      ),
      super.examinableProperties()
    );
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractComponentBuilder<SelectorComponent, Builder> implements SelectorComponent.Builder {
    private @Nullable String pattern;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull SelectorComponent component) {
      super(component);
      this.pattern = component.pattern();
    }

    @Override
    public @NonNull Builder pattern(final @NonNull String pattern) {
      this.pattern = pattern;
      return this;
    }

    @Override
    public @NonNull SelectorComponent build() {
      if(this.pattern == null) throw new IllegalStateException("pattern must be set");
      return new SelectorComponentImpl(this.children, this.buildStyle(), this.pattern);
    }
  }
}
