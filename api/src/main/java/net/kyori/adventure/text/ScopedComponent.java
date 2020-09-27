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

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Some magic to change return types.
 *
 * @param <C> the component type
 */
public interface ScopedComponent<C extends Component> extends Component {
  @Override
  @NonNull C children(final @NonNull List<? extends ComponentLike> children);

  @Override
  @NonNull C style(final @NonNull Style style);

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C style(final @NonNull Consumer<Style.Builder> style) {
    return (C) Component.super.style(style);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C style(final Style.@NonNull Builder style) {
    return (C) Component.super.style(style);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C mergeStyle(final @NonNull Component that) {
    return (C) Component.super.mergeStyle(that);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C mergeStyle(final @NonNull Component that, final Style.@NonNull Merge@NonNull... merges) {
    return (C) Component.super.mergeStyle(that, merges);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C append(final @NonNull Component component) {
    if(component == Component.empty()) return (C) this;
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> oldChildren = this.children();
    return this.children(AbstractComponent.addOne(oldChildren, component));
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C append(final @NonNull ComponentLike component) {
    return (C) Component.super.append(component);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C append(final @NonNull ComponentBuilder<?, ?> builder) {
    return (C) Component.super.append(builder);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C mergeStyle(final @NonNull Component that, final @NonNull Set<Style.Merge> merges) {
    return (C) Component.super.mergeStyle(that, merges);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C color(final @Nullable TextColor color) {
    return (C) Component.super.color(color);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C colorIfAbsent(final @Nullable TextColor color) {
    return (C) Component.super.colorIfAbsent(color);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull Component decorate(final @NonNull TextDecoration decoration) {
    return (C) Component.super.decorate(decoration);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C decoration(final @NonNull TextDecoration decoration, final boolean flag) {
    return (C) Component.super.decoration(decoration, flag);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    return (C) Component.super.decoration(decoration, state);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C clickEvent(final @Nullable ClickEvent event) {
    return (C) Component.super.clickEvent(event);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C hoverEvent(final @Nullable HoverEventSource<?> event) {
    return (C) Component.super.hoverEvent(event);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C insertion(final @Nullable String insertion) {
    return (C) Component.super.insertion(insertion);
  }
}
