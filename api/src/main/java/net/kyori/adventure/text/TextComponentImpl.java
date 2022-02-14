/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2022 KyoriPowered
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.internal.properties.AdventureProperties;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.util.Nag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import static java.util.Objects.requireNonNull;

final class TextComponentImpl extends AbstractComponent implements TextComponent {
  private static final boolean WARN_WHEN_LEGACY_FORMATTING_DETECTED = Boolean.TRUE.equals(AdventureProperties.TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED.value());
  @VisibleForTesting
  static final char SECTION_CHAR = '§';

  static final TextComponent EMPTY = createDirect("");
  static final TextComponent NEWLINE = createDirect("\n");
  static final TextComponent SPACE = createDirect(" ");

  private static @NotNull TextComponent createDirect(final @NotNull String content) {
    return new TextComponentImpl(Collections.emptyList(), Style.empty(), content);
  }

  private final String content;

  TextComponentImpl(final @NotNull List<? extends ComponentLike> children, final @NotNull Style style, final @NotNull String content) {
    super(children, style);
    this.content = requireNonNull(content, "content");

    if (WARN_WHEN_LEGACY_FORMATTING_DETECTED) {
      final LegacyFormattingDetected nag = this.warnWhenLegacyFormattingDetected();
      if (nag != null) {
        Nag.print(nag);
      }
    }
  }

  @VisibleForTesting
  final @Nullable LegacyFormattingDetected warnWhenLegacyFormattingDetected() {
    if (this.content.indexOf(SECTION_CHAR) != -1) {
      return new LegacyFormattingDetected(this);
    }
    return null;
  }

  @Override
  public @NotNull String content() {
    return this.content;
  }

  @Override
  public @NotNull TextComponent content(final @NotNull String content) {
    if (Objects.equals(this.content, content)) return this;
    return new TextComponentImpl(this.children, this.style, content);
  }

  @Override
  public @NotNull TextComponent children(final @NotNull List<? extends ComponentLike> children) {
    return new TextComponentImpl(requireNonNull(children, "children"), this.style, this.content);
  }

  @Override
  public @NotNull TextComponent style(final @NotNull Style style) {
    return new TextComponentImpl(this.children, requireNonNull(style, "style"), this.content);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if (this == other) return true;
    if (!(other instanceof TextComponentImpl)) return false;
    if (!super.equals(other)) return false;
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
  public String toString() {
    return Internals.toString(this);
  }

  @Override
  public @NotNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  static final class BuilderImpl extends AbstractComponentBuilder<TextComponent, Builder> implements TextComponent.Builder {
    /*
     * We default to an empty string to avoid needing to manually set the
     * content of a newly-created builder when we only want to append other
     * components to the one being built.
     */
    private String content = "";

    BuilderImpl() {
    }

    BuilderImpl(final @NotNull TextComponent component) {
      super(component);
      this.content = component.content();
    }

    @Override
    public @NotNull Builder content(final @NotNull String content) {
      this.content = requireNonNull(content, "content");
      return this;
    }

    @Override
    public @NotNull String content() {
      return this.content;
    }

    @Override
    public @NotNull TextComponent build() {
      if (this.isEmpty()) {
        return Component.empty();
      }
      return new TextComponentImpl(this.children, this.buildStyle(), this.content);
    }

    private boolean isEmpty() {
      return this.content.isEmpty() && this.children.isEmpty() && !this.hasStyle();
    }
  }
}
