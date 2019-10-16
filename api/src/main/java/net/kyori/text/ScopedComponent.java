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
package net.kyori.text;

import java.util.Set;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Some magic to change return types.
 *
 * @param <C> the component style
 */
public interface ScopedComponent<C extends Component> extends Component {
  @Override
  @NonNull C children(final @NonNull List<Component> children);

  @Override
  default @NonNull C append(final @NonNull Component component) {
    this.detectCycle(component); // detect cycle before modifying
    final List<Component> oldChildren = this.children();
    final List<Component> newChildren = new ArrayList<>(oldChildren.size() + 1);
    newChildren.addAll(oldChildren);
    newChildren.add(component);
    return this.children(newChildren);
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
  default @NonNull C hoverEvent(final @Nullable HoverEvent event) {
    return (C) Component.super.hoverEvent(event);
  }

  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C insertion(final @Nullable String insertion) {
    return (C) Component.super.insertion(insertion);
  }

  @Deprecated
  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C mergeColor(final @NonNull Component that) {
    return (C) Component.super.mergeColor(that);
  }

  @Deprecated
  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C mergeDecorations(final @NonNull Component that) {
    return (C) Component.super.mergeDecorations(that);
  }

  @Deprecated
  @Override
  @SuppressWarnings("unchecked")
  default @NonNull C mergeEvents(final @NonNull Component that) {
    return (C) Component.super.mergeEvents(that);
  }
}
