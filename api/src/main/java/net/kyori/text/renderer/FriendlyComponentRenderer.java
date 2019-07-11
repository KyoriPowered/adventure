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
package net.kyori.text.renderer;

import net.kyori.text.Component;
import net.kyori.text.ComponentBuilder;
import net.kyori.text.KeybindComponent;
import net.kyori.text.ScoreComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.AttributedCharacterIterator;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A friendly component renderer.
 */
public abstract class FriendlyComponentRenderer<C> implements ComponentRenderer<C> {
  public static <C> @NonNull FriendlyComponentRenderer<C> from(final @NonNull BiFunction<C, String, MessageFormat> translations) {
    return new FriendlyComponentRenderer<C>() {
      @Override
      protected @NonNull MessageFormat translation(final @NonNull C context, final @NonNull String key) {
        return translations.apply(context, key);
      }
    };
  }

  @Override
  public @NonNull Component render(final @NonNull Component component, final @NonNull C context) {
    if(component instanceof TranslatableComponent) {
      return this.render((TranslatableComponent) component, context);
    } else if(component instanceof TextComponent) {
      final TextComponent.Builder builder = TextComponent.builder(((TextComponent) component).content());
      return this.deepRender(component, builder, context).build();
    } else if(component instanceof KeybindComponent) {
      final KeybindComponent.Builder builder = KeybindComponent.builder(((KeybindComponent) component).keybind());
      return this.deepRender(component, builder, context).build();
    } else if(component instanceof ScoreComponent) {
      final ScoreComponent sc = (ScoreComponent) component;
      final ScoreComponent.Builder builder = ScoreComponent.builder()
        .name(sc.name())
        .objective(sc.objective())
        .value(sc.value());
      return this.deepRender(component, builder, context).build();
    } else if(component instanceof SelectorComponent) {
      final SelectorComponent.Builder builder = SelectorComponent.builder(((SelectorComponent) component).pattern());
      return this.deepRender(component, builder, context).build();
    } else {
      return component;
    }
  }

  private <B extends ComponentBuilder<?, ?>> B deepRender(final Component component, final B builder, final C context) {
    this.mergeStyle(component, builder, context);
    component.children().forEach(child -> builder.append(this.render(child, context)));
    return builder;
  }

  private <B extends ComponentBuilder<?, ?>> void mergeStyle(final Component component, final B builder, final C context) {
    builder.mergeStyle(component, Style.Merge.of(Style.Merge.COLOR, Style.Merge.DECORATIONS));
    builder.clickEvent(component.clickEvent());
    Optional.ofNullable(component.hoverEvent()).ifPresent(hoverEvent -> {
      builder.hoverEvent(HoverEvent.of(
        hoverEvent.action(),
        this.render(hoverEvent.value(), context)
      ));
    });
  }

  private Component render(final TranslatableComponent component, final C context) {
    final /* @Nullable */ MessageFormat format = this.translation(context, component.key());
    if(format == null) {
      return component;
    }

    final List<Component> args = component.args();

    final TextComponent.Builder builder = TextComponent.builder();
    this.mergeStyle(component, builder, context);

    // no arguments makes this render very simple
    if(args.isEmpty()) {
      builder.content(format.format(null, new StringBuffer(), null).toString());
    } else {
      final Object[] nulls = new Object[args.size()];
      final StringBuffer sb = format.format(nulls, new StringBuffer(), null);
      final AttributedCharacterIterator it = format.formatToCharacterIterator(nulls);

      while(it.getIndex() < it.getEndIndex()) {
        final int end = it.getRunLimit();
        final Integer index = (Integer) it.getAttribute(MessageFormat.Field.ARGUMENT);
        if(index != null) {
          builder.append(this.render(args.get(index), context));
        } else {
          builder.append(TextComponent.of(sb.substring(it.getIndex(), end)));
        }
        it.setIndex(end);
      }
    }

    return this.deepRender(component, builder, context).build();
  }

  /**
   * Gets a translation for a translation key in the given context.
   *
   * @param context the context
   * @param key the translation key
   * @return the translation
   */
  protected abstract @Nullable MessageFormat translation(final @NonNull C context, final @NonNull String key);
}
