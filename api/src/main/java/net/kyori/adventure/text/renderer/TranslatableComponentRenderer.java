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
package net.kyori.adventure.text.renderer;

import java.text.AttributedCharacterIterator;
import java.text.MessageFormat;
import java.util.List;
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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A {@link MessageFormat}-based translatable component renderer.
 */
public abstract class TranslatableComponentRenderer<C> extends AbstractComponentRenderer<C> {
  public static <C> @NonNull TranslatableComponentRenderer<C> from(final @NonNull TranslationFinder<C> finder) {
    return new TranslatableComponentRenderer<C>() {
      @Override
      protected @Nullable MessageFormat translation(final @NonNull C context, final @NonNull String key) {
        return finder.findTranslation(context, key);
      }
    };
  }

  @Override
  protected @NonNull Component renderBlockNbt(final @NonNull BlockNBTComponent component, final @NonNull C context) {
    final BlockNBTComponent.Builder builder = nbt(BlockNBTComponent.builder(), component)
      .pos(component.pos());
    return this.deepRender(component, builder, context);
  }

  @Override
  protected @NonNull Component renderEntityNbt(final @NonNull EntityNBTComponent component, final @NonNull C context) {
    final EntityNBTComponent.Builder builder = nbt(EntityNBTComponent.builder(), component)
      .selector(component.selector());
    return this.deepRender(component, builder, context);
  }

  @Override
  protected @NonNull Component renderStorageNbt(final @NonNull StorageNBTComponent component, final @NonNull C context) {
    final StorageNBTComponent.Builder builder = nbt(StorageNBTComponent.builder(), component)
      .storage(component.storage());
    return this.deepRender(component, builder, context);
  }

  private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(final B builder, final C oldComponent) {
    return builder
      .nbtPath(oldComponent.nbtPath())
      .interpret(oldComponent.interpret());
  }

  @Override
  protected @NonNull Component renderKeybind(final @NonNull KeybindComponent component, final @NonNull C context) {
    final KeybindComponent.Builder builder = KeybindComponent.builder(component.keybind());
    return this.deepRender(component, builder, context);
  }

  @Override
  protected @NonNull Component renderScore(final @NonNull ScoreComponent component, final @NonNull C context) {
    final ScoreComponent.Builder builder = ScoreComponent.builder()
      .name(component.name())
      .objective(component.objective())
      .value(component.value());
    return this.deepRender(component, builder, context);
  }

  @Override
  protected @NonNull Component renderSelector(final @NonNull SelectorComponent component, final @NonNull C context) {
    final SelectorComponent.Builder builder = SelectorComponent.builder(component.pattern());
    return this.deepRender(component, builder, context);
  }

  @Override
  protected @NonNull Component renderText(final @NonNull TextComponent component, final @NonNull C context) {
    final TextComponent.Builder builder = TextComponent.builder(component.content());
    return this.deepRender(component, builder, context);
  }

  @Override
  protected @NonNull Component renderTranslatable(final @NonNull TranslatableComponent component, final @NonNull C context) {
    final /* @Nullable */ MessageFormat format = this.translation(context, component.key());
    if(format == null) {
      return component;
    }

    final List<Component> args = component.args();

    final TextComponent.Builder builder = TextComponent.builder();
    this.mergeStyle(component, builder, context);

    // no arguments makes this render very simple
    if(args.isEmpty()) {
      return builder.content(format.format(null, new StringBuffer(), null).toString()).build();
    }

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

    return this.deepRender(component, builder, context);
  }

  // TODO(kashike): expose?
  private <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O deepRender(final Component component, final B builder, final C context) {
    this.mergeStyle(component, builder, context);
    component.children().forEach(child -> builder.append(this.render(child, context)));
    return builder.build();
  }

  private <B extends ComponentBuilder<?, ?>> void mergeStyle(final Component component, final B builder, final C context) {
    builder.mergeStyle(component, Style.Merge.colorAndDecorations());
    builder.clickEvent(component.clickEvent());
    final /* @Nullable */ HoverEvent<?> hoverEvent = component.hoverEvent();
    if(hoverEvent != null) {
      builder.hoverEvent(hoverEvent.withRenderedValue(this, context));
    }
  }

  /**
   * Gets a translation for a translation key in the given context.
   *
   * @param context the context
   * @param key the translation key
   * @return the translation
   */
  protected abstract @Nullable MessageFormat translation(final @NonNull C context, final @NonNull String key);

  /**
   * A translation finder.
   *
   * @param <C> the context type
   */
  @FunctionalInterface
  public interface TranslationFinder<C> {
    /**
     * Finds a translation.
     *
     * @param context the context
     * @param key the translation key
     * @return the translation
     */
    @Nullable MessageFormat findTranslation(final @NonNull C context, final @NonNull String key);
  }
}
