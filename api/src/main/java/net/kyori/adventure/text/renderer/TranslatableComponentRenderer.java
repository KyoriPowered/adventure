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
package net.kyori.adventure.text.renderer;

import java.text.AttributedCharacterIterator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A component renderer that does server-side translation rendering.
 *
 * @param <C> the context type, usually {@link java.util.Locale}.
 * @since 4.0.0
 */
public abstract class TranslatableComponentRenderer<C> extends AbstractComponentRenderer<C> {
  private static final Set<Style.Merge> MERGES = Style.Merge.merges(Style.Merge.COLOR, Style.Merge.DECORATIONS, Style.Merge.INSERTION, Style.Merge.FONT);

  /**
   * Creates a {@link TranslatableComponentRenderer} using the {@link Translator} to translate.
   *
   * @param source the translation source
   * @return the renderer
   * @since 4.0.0
   */
  public static @NotNull TranslatableComponentRenderer<Locale> usingTranslationSource(final @NotNull Translator source) {
    requireNonNull(source, "source");
    return new TranslatableComponentRenderer<Locale>() {
      @Override
      protected @Nullable MessageFormat translate(final @NotNull String key, final @NotNull Locale context) {
        return source.translate(key, context);
      }
    };
  }

  /**
   * Gets a message format from a key and context.
   *
   * @param key a translation key
   * @param context a context
   * @return a message format or {@code null} to skip translation
   */
  protected abstract @Nullable MessageFormat translate(final @NotNull String key, final @NotNull C context);

  @Override
  protected @NotNull Component renderBlockNbt(final @NotNull BlockNBTComponent component, final @NotNull C context) {
    final BlockNBTComponent.Builder builder = this.nbt(context, Component.blockNBT(), component)
      .pos(component.pos());
    return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
  }

  @Override
  protected @NotNull Component renderEntityNbt(final @NotNull EntityNBTComponent component, final @NotNull C context) {
    final EntityNBTComponent.Builder builder = this.nbt(context, Component.entityNBT(), component)
      .selector(component.selector());
    return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
  }

  @Override
  protected @NotNull Component renderStorageNbt(final @NotNull StorageNBTComponent component, final @NotNull C context) {
    final StorageNBTComponent.Builder builder = this.nbt(context, Component.storageNBT(), component)
      .storage(component.storage());
    return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
  }

  protected <O extends NBTComponent<O, B>, B extends NBTComponentBuilder<O, B>> B nbt(final @NotNull C context, final B builder, final O oldComponent) {
    builder
      .nbtPath(oldComponent.nbtPath())
      .interpret(oldComponent.interpret());
    final @Nullable Component separator = oldComponent.separator();
    if (separator != null) {
      builder.separator(this.render(separator, context));
    }
    return builder;
  }

  @Override
  protected @NotNull Component renderKeybind(final @NotNull KeybindComponent component, final @NotNull C context) {
    final KeybindComponent.Builder builder = Component.keybind().keybind(component.keybind());
    return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
  }

  @Override
  @SuppressWarnings("deprecation")
  protected @NotNull Component renderScore(final @NotNull ScoreComponent component, final @NotNull C context) {
    final ScoreComponent.Builder builder = Component.score()
      .name(component.name())
      .objective(component.objective())
      .value(component.value());
    return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
  }

  @Override
  protected @NotNull Component renderSelector(final @NotNull SelectorComponent component, final @NotNull C context) {
    final SelectorComponent.Builder builder = Component.selector().pattern(component.pattern());
    return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
  }

  @Override
  protected @NotNull Component renderText(final @NotNull TextComponent component, final @NotNull C context) {
    final TextComponent.Builder builder = Component.text().content(component.content());
    return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
  }

  @Override
  @SuppressWarnings("JdkObsolete") // MessageFormat requires StringBuffer in its api
  protected @NotNull Component renderTranslatable(final @NotNull TranslatableComponent component, final @NotNull C context) {
    final @Nullable MessageFormat format = this.translate(component.key(), context);
    if (format == null) {
      // we don't have a translation for this component, but the arguments or children
      // of this component might need additional rendering

      final TranslatableComponent.Builder builder = Component.translatable()
        .key(component.key());
      if (!component.args().isEmpty()) {
        final List<Component> args = new ArrayList<>(component.args());
        for (int i = 0, size = args.size(); i < size; i++) {
          args.set(i, this.render(args.get(i), context));
        }
        builder.args(args);
      }
      return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
    }

    final List<Component> args = component.args();

    final TextComponent.Builder builder = Component.text();
    this.mergeStyle(component, builder, context);

    // no arguments makes this render very simple
    if (args.isEmpty()) {
      builder.content(format.format(null, new StringBuffer(), null).toString());
      return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
    }

    final Object[] nulls = new Object[args.size()];
    final StringBuffer sb = format.format(nulls, new StringBuffer(), null);
    final AttributedCharacterIterator it = format.formatToCharacterIterator(nulls);

    while (it.getIndex() < it.getEndIndex()) {
      final int end = it.getRunLimit();
      final Integer index = (Integer) it.getAttribute(MessageFormat.Field.ARGUMENT);
      if (index != null) {
        builder.append(this.render(args.get(index), context));
      } else {
        builder.append(Component.text(sb.substring(it.getIndex(), end)));
      }
      it.setIndex(end);
    }

    return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
  }

  protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O mergeStyleAndOptionallyDeepRender(final Component component, final B builder, final C context) {
    this.mergeStyle(component, builder, context);
    return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
  }

  protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O optionallyRenderChildrenAppendAndBuild(final List<Component> children, final B builder, final C context) {
    if (!children.isEmpty()) {
      children.forEach(child -> builder.append(this.render(child, context)));
    }
    return builder.build();
  }

  protected <B extends ComponentBuilder<?, ?>> void mergeStyle(final Component component, final B builder, final C context) {
    builder.mergeStyle(component, MERGES);
    builder.clickEvent(component.clickEvent());
    final @Nullable HoverEvent<?> hoverEvent = component.hoverEvent();
    if (hoverEvent != null) {
      builder.hoverEvent(hoverEvent.withRenderedValue(this, context));
    }
  }
}
