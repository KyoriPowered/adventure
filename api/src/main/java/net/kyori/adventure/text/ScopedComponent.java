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
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ARGBLike;
import org.jspecify.annotations.Nullable;

/**
 * Some magic to change return types.
 *
 * @param <C> the component type
 * @since 4.0.0
 */
public sealed interface ScopedComponent<C extends Component> extends Component permits BlockNBTComponent, EntityNBTComponent, KeybindComponent, ObjectComponent, ScoreComponent, SelectorComponent, StorageNBTComponent, TextComponent, TranslatableComponent {
  @Override
  @SuppressWarnings("unchecked")
  default C asComponent() {
    return (C) Component.super.asComponent();
  }

  @Override
  C children(final List<? extends ComponentLike> children);

  @Override
  C style(final Style style);

  @Override
  @SuppressWarnings("unchecked")
  default C style(final Consumer<Style.Builder> style) {
    return (C) Component.super.style(style);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C style(final Style.Builder style) {
    return (C) Component.super.style(style);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C style(final Consumer<Style.Builder> consumer, final Style.Merge.Strategy strategy) {
    return (C) Component.super.style(consumer, strategy);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C mergeStyle(final Component that) {
    return (C) Component.super.mergeStyle(that);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C mergeStyle(final Component that, final Style.Merge... merges) {
    return (C) Component.super.mergeStyle(that, merges);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C append(final Component component) {
    return (C) Component.super.append(component);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C append(final ComponentLike like) {
    return (C) Component.super.append(like);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C append(final ComponentBuilder<?, ?> builder) {
    return (C) Component.super.append(builder);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C append(final List<? extends ComponentLike> components) {
    return (C) Component.super.append(components);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C append(final ComponentLike ... components) {
    return (C) Component.super.append(components);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C appendNewline() {
    return (C) Component.super.appendNewline();
  }

  @Override
  @SuppressWarnings("unchecked")
  default C appendSpace() {
    return (C) Component.super.appendSpace();
  }

  @Override
  @SuppressWarnings("unchecked")
  default C applyFallbackStyle(final StyleBuilderApplicable ... style) {
    return (C) Component.super.applyFallbackStyle(style);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C applyFallbackStyle(final Style style) {
    return (C) Component.super.applyFallbackStyle(style);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C mergeStyle(final Component that, final Set<Style.Merge> merges) {
    return (C) Component.super.mergeStyle(that, merges);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C color(final @Nullable TextColor color) {
    return (C) Component.super.color(color);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C colorIfAbsent(final @Nullable TextColor color) {
    return (C) Component.super.colorIfAbsent(color);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C shadowColor(final @Nullable ARGBLike argb) {
    return (C) Component.super.shadowColor(argb);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C shadowColorIfAbsent(final @Nullable ARGBLike argb) {
    return (C) Component.super.shadowColorIfAbsent(argb);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C decorate(final TextDecoration decoration) {
    return (C) Component.super.decorate(decoration);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C decoration(final TextDecoration decoration, final boolean flag) {
    return (C) Component.super.decoration(decoration, flag);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C decoration(final TextDecoration decoration, final TextDecoration.State state) {
    return (C) Component.super.decoration(decoration, state);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C decorationIfAbsent(final TextDecoration decoration, final TextDecoration.State state) {
    return (C) Component.super.decorationIfAbsent(decoration, state);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C decorations(final Map<TextDecoration, TextDecoration.State> decorations) {
    return (C) Component.super.decorations(decorations);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C clickEvent(final @Nullable ClickEvent<?> event) {
    return (C) Component.super.clickEvent(event);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C hoverEvent(final @Nullable HoverEventSource<?> event) {
    return (C) Component.super.hoverEvent(event);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C insertion(final @Nullable String insertion) {
    return (C) Component.super.insertion(insertion);
  }

  @Override
  @SuppressWarnings("unchecked")
  default C font(final @Nullable Key key) {
    return (C) Component.super.font(key);
  }

  @Override
  ComponentBuilder<C, ?> toBuilder();
}
