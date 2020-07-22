/*
 * This file is part of adventure, licensed under the MIT License.
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
package net.kyori.adventure.text;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.Style;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/* package */ class TextComponentImpl extends AbstractComponent implements TextComponent {
  /* package */ static final TextComponent EMPTY = createDirect("");
  /* package */ static final TextComponent NEWLINE = createDirect("\n");
  /* package */ static final TextComponent SPACE = createDirect(" ");

  private static @NonNull TextComponent createDirect(final @NonNull String content) {
    return new TextComponentImpl(Collections.emptyList(), Style.empty(), content);
  }

  private final String content;

  TextComponentImpl(final @NonNull List<? extends ComponentLike> children, final @NonNull Style style, final @NonNull String content) {
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
  public @NonNull TextComponent replace(final @NonNull Pattern pattern, final @NonNull UnaryOperator<Builder> replacement, int numberOfReplacements) {
    final List<Component> produced = new ArrayList<>();
    final Queue<TextComponent> queue = new ArrayDeque<>();
    queue.add(this);

    int replaced = 0;

    while(!queue.isEmpty()) {
      final TextComponent current = queue.remove();
      final String content = current.content();
      final Matcher matcher = pattern.matcher(content);
      final TextComponent withoutChildren = current.children(Collections.emptyList());

      if((numberOfReplacements > -1 && replaced < numberOfReplacements) && matcher.find()) {
        int lastEnd = 0;
        replaced++;
        do {
          final int start = matcher.start();
          final int end = matcher.end();
          final String matched = matcher.group();

          final String prefix = content.substring(lastEnd, start);
          if(!prefix.isEmpty()) {
            produced.add(withoutChildren.content(prefix));
          }

          produced.add(replacement.apply(withoutChildren.toBuilder().content(matched)).build());
          lastEnd = end;
        } while(matcher.find());

        if(content.length() - lastEnd > 0) {
          produced.add(withoutChildren.content(content.substring(lastEnd)));
        }
      } else {
        // children are handled separately
        produced.add(withoutChildren);
      }

      for(final Component child : current.children()) {
        if(child instanceof TextComponent) {
          queue.add((TextComponent) child);
        } else {
          produced.add(child);
        }
      }
    }

    if(produced.size() == 1) {
      return (TextComponent) produced.get(0);
    } else {
      final List<Component> children = produced.subList(1, produced.size());
      return (TextComponent) produced.get(0).children(children);
    }
  }

  @Override
  public @NonNull TextComponent replace(final @NonNull Pattern pattern, final @NonNull UnaryOperator<Builder> replacement) {
    return replace(pattern, replacement, -1);
  }

  @Override
  public @NonNull TextComponent replaceFirst(final @NonNull Pattern pattern, final @NonNull UnaryOperator<Builder> replacement) {
    return replace(pattern, replacement, 1);
  }

  @Override
  public @NonNull TextComponent children(final @NonNull List<? extends ComponentLike> children) {
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
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("content", this.content)
      ),
      super.examinableProperties()
    );
  }

  @Override
  public @NonNull Builder toBuilder() {
    return new BuilderImpl(this);
  }

  /* package */ static final class BuilderImpl extends AbstractComponentBuilder<TextComponent, Builder> implements TextComponent.Builder {
    /*
     * We default to an empty string to avoid needing to manually set the
     * content of a newly-created builder when we only want to append other
     * components to the one being built.
     */
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
    public @NonNull String content() {
      return this.content;
    }

    @Override
    public @NonNull TextComponent build() {
      if(this.isEmpty()) {
        return TextComponent.empty();
      }
      return new TextComponentImpl(this.children, this.buildStyle(), this.content);
    }

    private boolean isEmpty() {
      return this.content.isEmpty() && this.children.isEmpty() && !this.hasStyle();
    }
  }
}
