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

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Some magic to change return types.
 *
 * @param <C> the component type
 * @since 4.0.0
 */
public interface ScopedComponent<C extends Component> extends Component {
  @Override
  @NotNull C children(final @NotNull List<? extends ComponentLike> children);

  @Override
  @NotNull C style(final @NotNull Style style);

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C style(final @NotNull Consumer<Style.Builder> style) {
    return (C) Component.super.style(style);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C style(final Style.@NotNull Builder style) {
    return (C) Component.super.style(style);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C mergeStyle(final @NotNull Component that) {
    return (C) Component.super.mergeStyle(that);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C mergeStyle(final @NotNull Component that, final Style.@NotNull Merge@NotNull... merges) {
    return (C) Component.super.mergeStyle(that, merges);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C append(final @NotNull Component component) {
    return (C) Component.super.append(component);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C append(final @NotNull ComponentLike like) {
    return (C) Component.super.append(like);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C append(final @NotNull ComponentBuilder<?, ?> builder) {
    return (C) Component.super.append(builder);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C mergeStyle(final @NotNull Component that, final @NotNull Set<Style.Merge> merges) {
    return (C) Component.super.mergeStyle(that, merges);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C color(final @Nullable TextColor color) {
    return (C) Component.super.color(color);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C colorIfAbsent(final @Nullable TextColor color) {
    return (C) Component.super.colorIfAbsent(color);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C decorate(final @NotNull TextDecoration decoration) {
    return (C) Component.super.decorate(decoration);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C decoration(final @NotNull TextDecoration decoration, final boolean flag) {
    return (C) Component.super.decoration(decoration, flag);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C decoration(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
    return (C) Component.super.decoration(decoration, state);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C clickEvent(final @Nullable ClickEvent event) {
    return (C) Component.super.clickEvent(event);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C hoverEvent(final @Nullable HoverEventSource<?> event) {
    return (C) Component.super.hoverEvent(event);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NotNull C insertion(final @Nullable String insertion) {
    return (C) Component.super.insertion(insertion);
  }
}
