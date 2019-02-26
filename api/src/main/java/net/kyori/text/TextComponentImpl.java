/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
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
package net.kyori.text;

import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

class TextComponentImpl extends AbstractComponent implements TextComponent {
  private final String content;

  protected TextComponentImpl(final @NonNull List<Component> children, final Style.@Nullable Builder style, final @NonNull String content) {
    super(children, style);
    this.content = content;
  }

  protected TextComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final @NonNull String content) {
    super(children, style);
    this.content = content;
  }

  @Override
  public @NonNull String content() {
    return this.content;
  }

  @Override
  public @NonNull TextComponent content(final @NonNull String content) {
    return new TextComponentImpl(this.children, this.style, requireNonNull(content, "content"));
  }

  @Override
  public @NonNull TextComponent children(final @NonNull List<Component> children) {
    return new TextComponentImpl(children, this.style, this.content);
  }

  @Override
  public @NonNull TextComponent style(final @NonNull Style style) {
    return new TextComponentImpl(this.children, style, this.content);
  }

  @Override
  public @NonNull TextComponent copy() {
    return new TextComponentImpl(this.children, this.style, this.content);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof TextComponentImpl)) return false;
    if(!super.equals(other)) return false;
    final TextComponentImpl component = (TextComponentImpl) other;
    return Objects.equals(this.content, component.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.content);
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("content", this.content);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  public static class BuilderImpl extends AbstractComponentBuilder<TextComponent, Builder> implements TextComponent.Builder {
    private @Nullable String content;

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull TextComponentImpl component) {
      super(component);
      this.content = component.content();
    }

    @Override
    public @NonNull Builder content(final @NonNull String content) {
      this.content = content;
      return this;
    }

    @Override
    public @NonNull TextComponentImpl build() {
      if(this.content == null) throw new IllegalStateException("content must be set");
      return new TextComponentImpl(this.children, this.buildStyle(), this.content);
    }
  }
}
