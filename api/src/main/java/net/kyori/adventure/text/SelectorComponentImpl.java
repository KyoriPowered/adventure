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
package net.kyori.adventure.text;

import java.util.List;
import java.util.Objects;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

record SelectorComponentImpl(List<Component> children, Style style, String pattern, @Nullable Component separator) implements SelectorComponent {
  static SelectorComponent create(final List<? extends ComponentLike> children, final Style style, final String pattern, final @Nullable ComponentLike separator) {
    return new SelectorComponentImpl(
      ComponentLike.asComponents(children, IS_NOT_EMPTY),
      requireNonNull(style, "style"),
      requireNonNull(pattern, "pattern"),
      ComponentLike.unbox(separator)
    );
  }

  @Override
  public SelectorComponent pattern(final String pattern) {
    if (Objects.equals(this.pattern, pattern)) return this;
    return create(this.children, this.style, pattern, this.separator);
  }

  @Override
  public SelectorComponent separator(final @Nullable ComponentLike separator) {
    return create(this.children, this.style, this.pattern, separator);
  }

  @Override
  public SelectorComponent children(final List<? extends ComponentLike> children) {
    return create(children, this.style, this.pattern, this.separator);
  }

  @Override
  public SelectorComponent style(final Style style) {
    return create(this.children, style, this.pattern, this.separator);
  }

  @Override
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractComponentBuilder<SelectorComponent, Builder> implements Builder {
    private @Nullable String pattern;
    private @Nullable Component separator;

    BuilderImpl() {
    }

    BuilderImpl(final SelectorComponent component) {
      super(component);
      this.pattern = component.pattern();
      this.separator = component.separator();
    }

    @Override
    public Builder pattern(final String pattern) {
      this.pattern = requireNonNull(pattern, "pattern");
      return this;
    }

    @Override
    public Builder separator(final @Nullable ComponentLike separator) {
      this.separator = ComponentLike.unbox(separator);
      return this;
    }

    @Override
    public SelectorComponent build() {
      if (this.pattern == null) throw new IllegalStateException("pattern must be set");
      return create(this.children, this.buildStyle(), this.pattern, this.separator);
    }
  }
}
