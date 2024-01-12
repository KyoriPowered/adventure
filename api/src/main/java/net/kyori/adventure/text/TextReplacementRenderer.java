/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A renderer performing a replacement on every {@link TextComponent} element of a component tree.
 */
final class TextReplacementRenderer implements ComponentRenderer<TextReplacementRenderer.State> {
  static final TextReplacementRenderer INSTANCE = new TextReplacementRenderer();

  private TextReplacementRenderer() {
  }

  @Override
  public @NotNull Component render(final @NotNull Component component, final @NotNull State state) {
    if (!state.running) return component;
    final boolean prevFirstMatch = state.firstMatch;
    state.firstMatch = true;

    Component modified = component;
    Style oldStyle = component.style();
    List<Component> oldChildren = component.children();
    List<Component> children = new ArrayList<>();

    if (component instanceof TextComponent) {
      String content = ((TextComponent) component).content();
      Matcher matcher = state.pattern.matcher(content);

      int replacedUntil = 0; // last index handled
      while (matcher.find()) {
        final PatternReplacementResult result = state.continuer.shouldReplace(matcher, ++state.matchCount, state.replaceCount);
        if (result == PatternReplacementResult.CONTINUE) {
          // ignore this replacement
          continue;
        } else if (result == PatternReplacementResult.STOP) {
          // end replacement
          state.running = false;
          break;
        }

        if (matcher.start() == 0) {
          // if we're a full match, modify the component directly
          if (matcher.end() == content.length()) {
            final ComponentLike replacement = state.replacement.apply(matcher, Component.text().content(matcher.group())
              .style(component.style()));

            modified = replacement == null ? Component.empty() : replacement.asComponent();

            if (modified.style().hoverEvent() != null) {
              oldStyle = oldStyle.hoverEvent(null); // Remove original hover if it has been replaced completely
            }

            // merge style of the match into this component to prevent unexpected loss of style
            modified = modified.style(modified.style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));

            if (children.isEmpty()) { // Prepare children
              children.addAll(modified.children());
            }
          } else {
            // otherwise, work on a child of the root node
            modified = Component.text("", component.style());
            final ComponentLike child = state.replacement.apply(matcher, Component.text().content(matcher.group()));
            if (child != null) {
              children.add(child.asComponent());
            }
          }
        } else {
          if (state.firstMatch) {
            // truncate parent to content before match
            modified = ((TextComponent) component).content(content.substring(0, matcher.start()));
          } else if (replacedUntil < matcher.start()) {
            children.add(Component.text(content.substring(replacedUntil, matcher.start())));
          }
          final ComponentLike builder = state.replacement.apply(matcher, Component.text().content(matcher.group()));
          if (builder != null) {
            children.add(builder.asComponent());
          }
        }
        state.replaceCount++;
        state.firstMatch = false;
        replacedUntil = matcher.end();
      }
      if (replacedUntil < content.length()) {
        // append trailing content
        if (replacedUntil > 0) {
          children.add(Component.text(content.substring(replacedUntil)));
        }
        // otherwise, we haven't modified the component, so nothing to change
      }
    } else if (component instanceof TranslatableComponent) {
      modified = handleTranslatableComponent(modified, state);
    }

    if (state.running) {
      modified = handleHoverEvent(modified, oldStyle, state);
      modified = handleClickEvent(modified, oldStyle, state);
      modified = handleChildren(modified, children, oldChildren, state);
    } else {
      children.addAll(oldChildren);
    }

    state.firstMatch = prevFirstMatch;

    if (!children.isEmpty()) {
      return modified.children(children);
    }

    return modified;
  }

  private Component handleTranslatableComponent(Component component, State state) {
    TranslatableComponent translatableComponent = (TranslatableComponent) component;
    List<TranslationArgument> args = translatableComponent.arguments();
    List<TranslationArgument> newArgs = null;

    for (int i = 0, size = args.size(); i < size; i++) {
      final TranslationArgument original = args.get(i);
      final TranslationArgument replaced = original.value() instanceof Component ? TranslationArgument.component(this.render((Component) original.value(), state)) : original;

      if (replaced != original) {
        if (newArgs == null) {
          newArgs = new ArrayList<>(size);
          if (i > 0) {
            newArgs.addAll(args.subList(0, i));
          }
        }
      }
      if (newArgs != null) {
        newArgs.add(replaced);
      }
    }

    if (newArgs != null) {
      translatableComponent = translatableComponent.arguments(newArgs);
    }

    return translatableComponent;
  }

  private Component handleHoverEvent(Component component, Style oldStyle, State state) {
    HoverEvent<?> event = oldStyle.hoverEvent();

    if (event != null) {
      final HoverEvent<?> rendered = event.withRenderedValue(this, state);
      if (event != rendered) {
        return component.style(s -> s.hoverEvent(rendered));
      }
    }

    return component;
  }

  private Component handleClickEvent(Component component, Style oldStyle, State state) {
    ClickEvent event = oldStyle.clickEvent();

    if (event != null) {
      String oldValue = event.value();
      Component replacedValueComponent = this.render(Component.text(oldValue), state);
      TextComponent replacedValueText = (TextComponent) replacedValueComponent;
      StringBuilder finalValue = new StringBuilder(replacedValueText.content());

      for (Component child : replacedValueText.children()) {
        if (child instanceof TextComponent) {
          finalValue.append(((TextComponent) child).content());
        }
      }

      if (!oldValue.equalsIgnoreCase(finalValue.toString())) {
        return component.style(s -> s.clickEvent(ClickEvent.clickEvent(event.action(), finalValue.toString())));
      }
    }

    return component;
  }

  private Component handleChildren(Component component, List<Component> children, List<Component> oldChildren, State state) {
    boolean first = true;

    for (int i = 0; i < oldChildren.size(); i++) {
      final Component child = oldChildren.get(i);
      final Component replaced = this.render(child, state);
      if (replaced != child) {
        if (first) {
          children.addAll(oldChildren.subList(0, i));
          first = false;
        }
      }

      if (!oldChildren.isEmpty()) {
        children.add(replaced);
        first = false;
      }
    }

    return component.children(children);
  }

  static final class State {
    final Pattern pattern;
    final BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement;
    final TextReplacementConfig.Condition continuer;
    boolean running = true;
    int matchCount = 0;
    int replaceCount = 0;
    boolean firstMatch = true;

    State(final @NotNull Pattern pattern, final @NotNull BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> replacement, final TextReplacementConfig.@NotNull Condition continuer) {
      this.pattern = pattern;
      this.replacement = replacement;
      this.continuer = continuer;
    }
  }
}
