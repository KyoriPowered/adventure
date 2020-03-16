/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

final class TextComponentImpl extends AbstractComponent implements TextComponent {
  static final TextComponent EMPTY = createDirect("");
  static final TextComponent NEWLINE = createDirect("\n");
  static final TextComponent SPACE = createDirect(" ");

  private static @NonNull TextComponent createDirect(final @NonNull String content) {
    return new TextComponentImpl(EMPTY_COMPONENT_LIST, Style.empty(), content);
  }

  private final String content;

  TextComponentImpl(final @NonNull List<Component> children, final @NonNull Style style, final @NonNull String content) {
    super(children, style);
    this.content = content;
  }

  @Override
  public @NonNull String content() {
    return this.content;
  }

  @Override
  public @NonNull TextComponent content(final @NonNull String content) {
    if(Objects.equals(this.content, content)) return this;
    return new TextComponentImpl(this.children, this.style, requireNonNull(content, "content"));
  }

  @Override
  public boolean isEmpty() {
    return this == EMPTY;
  }

  @Override
  public @NonNull TextComponent children(final @NonNull List<Component> children) {
    return new TextComponentImpl(children, this.style, this.content);
  }

  @Override
  public @NonNull TextComponent style(final @NonNull Style style) {
    if(Objects.equals(this.style, style)) return this;
    return new TextComponentImpl(this.children, style, this.content);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(!(other instanceof TextComponentImpl)) return false;
    if(!super.equals(other)) return false;
    final TextComponentImpl that = (TextComponentImpl) other;
    return Objects.equals(this.content, that.content);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = (31 * result) + this.content.hashCode();
    return result;
  }

  @Override
  protected void populateToString(final @NonNull Map<String, Object> builder) {
    builder.put("content", this.content);
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractComponentBuilder<TextComponent, Builder> implements TextComponent.Builder {
    private String content = "";

    BuilderImpl() {
    }

    BuilderImpl(final @NonNull TextComponent component) {
      super(component);
      this.content = component.content();
    }

    @Override
    public @NonNull Builder content(final @NonNull String content) {
      this.content = requireNonNull(content, "content");
      return this;
    }

    @Override
    public @NonNull TextComponent build() {
      if(this.isEmpty()) {
        return EMPTY;
      }
      return new TextComponentImpl(this.children, this.buildStyle(), this.content);
    }

    private boolean isEmpty() {
      return this.content.isEmpty() && this.children.isEmpty() && !this.hasStyle();
    }
  }
}
