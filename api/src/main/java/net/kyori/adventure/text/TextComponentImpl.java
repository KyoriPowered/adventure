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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.util.IntFunction2;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

final class TextComponentImpl extends AbstractComponent implements TextComponent {
  static final TextComponent EMPTY = createDirect("");
  static final TextComponent NEWLINE = createDirect("\n");
  static final TextComponent SPACE = createDirect(" ");

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
  public @NonNull TextComponent children(final @NonNull List<? extends ComponentLike> children) {
    return new TextComponentImpl(children, this.style, this.content);
  }

  @Override
  public @NonNull TextComponent style(final @NonNull Style style) {
    return new TextComponentImpl(this.children, style, this.content);
  }

  @Override
  public @NonNull Component replaceText(final @NonNull Pattern pattern, final @NonNull UnaryOperator<Builder> replacement,
                                        final @NonNull IntFunction2<PatternReplacementResult> fn) {
    return Replacer.INSTANCE.render(this, new ReplaceState(pattern, (result, build) -> replacement.apply(build), fn));
  }

  static final class ReplaceState {
    final Pattern pattern;
    final BiFunction<MatchResult, Builder, @Nullable Builder> replacement;
    final IntFunction2<PatternReplacementResult> continuer;
    boolean running = true;
    int matchCount = 0;
    int replaceCount = 0;

    ReplaceState(final @NonNull Pattern pattern, final @NonNull BiFunction<MatchResult, Builder, @Nullable Builder> replacement, final @NonNull IntFunction2<PatternReplacementResult> continuer) {
      this.pattern = pattern;
      this.replacement = replacement;
      this.continuer = continuer;
    }
  }

  static final class Replacer implements ComponentRenderer<ReplaceState> {
    static Replacer INSTANCE = new Replacer();

    private Replacer() {
    }

    @Override
    public @NonNull Component render(final @NonNull Component component, final @NonNull ReplaceState state) {
      if(!state.running) return component;

      List<Component> children = null;
      Component modified = component;
      // replace the component itself
      if(component instanceof TextComponent) {
        final String content = ((TextComponent) component).content();
        final Matcher matcher = state.pattern.matcher(content);
        int replacedUntil = 0; // last index handled
        while(matcher.find()) {
          final PatternReplacementResult result = state.continuer.apply(++state.matchCount, state.replaceCount);
          if(result == PatternReplacementResult.CONTINUE) {
            // ignore this replacement
            continue;
          } else if(result == PatternReplacementResult.STOP) {
            // end replacement
            state.running = false;
            break;
          }

          if(matcher.start() == 0) {
            // if we're a full match, modify the component directly
            if(matcher.end() == content.length()) {
              final TextComponent.Builder replacement = state.replacement.apply(matcher, TextComponent.builder(matcher.group())
                .style(component.style()));

              modified = replacement == null ? TextComponent.empty() : replacement.build();
            } else {
              // otherwise, work on a child of the root node
              modified = TextComponent.of("", component.style());
              final TextComponent.Builder child = state.replacement.apply(matcher, TextComponent.builder(matcher.group()));
              if(child != null) {
                children = this.listOrNew(children, component.children().size() + 1);
                children.add(child.build());
              }
            }
          } else {
            children = this.listOrNew(children, component.children().size() + 2);
            if(state.replaceCount == 0) {
              // truncate parent to content before match
              modified = ((TextComponent) component).content(content.substring(0, matcher.start()));
            } else if(replacedUntil < matcher.start()) {
              children.add(TextComponent.of(content.substring(replacedUntil, matcher.start())));
            }
            final TextComponent.Builder builder = state.replacement.apply(matcher, TextComponent.builder(matcher.group()));
            if(builder != null) {
              children.add(builder.build());
            }
          }
          state.replaceCount++;
          replacedUntil = matcher.end();
        }
        if(replacedUntil < content.length()) {
          // append trailing content
          if(replacedUntil > 0) {
            children = this.listOrNew(children, component.children().size());
            children.add(TextComponent.of(content.substring(replacedUntil)));
          }
          // otherwise, we haven't modified the component, so nothing to change
        }
      } else if(modified instanceof TranslatableComponent) { // get TranslatableComponent with() args
        final List<Component> args = ((TranslatableComponent) modified).args();
        List<Component> newArgs = null;
        for(int i = 0; i < args.size(); i++) {
          final Component original = args.get(i);
          final Component replaced = this.render(original, state);
          if(replaced != component) {
            if(newArgs == null) {
              newArgs = new ArrayList<>(args.size());
              if(i > 0) {
                newArgs.addAll(args.subList(0, i));
              }
            }
          }
          if(newArgs != null) {
            newArgs.add(replaced);
          }
        }
        if(newArgs != null) {
          modified = ((TranslatableComponent) modified).args(newArgs);
        }
      }
      // Only visit children if we're running
      if(state.running) {
        // hover event
        final HoverEvent<?> event = modified.style().hoverEvent();
        if(event != null) {
          final HoverEvent<?> rendered = event.withRenderedValue(this, state);
          if(event != rendered) {
            modified = modified.style(s -> s.hoverEvent(rendered));
          }
        }
        // Children
        boolean first = true;
        for(int i = 0; i < component.children().size(); ++i) {
          final Component child = component.children().get(i);
          final Component replaced = this.render(child, state);
          if(replaced != child) {
            children = this.listOrNew(children, component.children().size());
            if(first) {
              children.addAll(component.children().subList(0, i));
            }
            first = false;
          }
          if(children != null) {
            children.add(replaced);
          }
        }
      } else {
        // we're not visiting children, re-add original children if necessary
        if(children != null) {
          children.addAll(component.children());
        }
      }

      // Update the modified component with new children
      if(children != null) {
        return modified.children(children);
      }
      return modified;
    }

    private <T> @NonNull List<T> listOrNew(final @Nullable List<T> init, final int size) {
      return init == null ? new ArrayList<>(size) : init;
    }
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

  static final class BuilderImpl extends AbstractComponentBuilder<TextComponent, Builder> implements TextComponent.Builder {
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
