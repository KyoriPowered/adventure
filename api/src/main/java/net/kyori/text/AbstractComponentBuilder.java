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

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An abstract implementation of a component builder.
 *
 * @param <C> the component type
 * @param <B> the builder type
 */
abstract class AbstractComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> implements ComponentBuilder<C, B> {
  /**
   * The list of children.
   *
   * <p>This list is set to {@link AbstractComponent#EMPTY_COMPONENT_LIST an empty list of components}
   * by default to prevent unnecessary list creation for components with no children.</p>
   */
  protected List<Component> children = AbstractComponent.EMPTY_COMPONENT_LIST;
  /**
   * The style.
   */
  protected Style.@MonotonicNonNull Builder style;

  protected AbstractComponentBuilder() {
  }

  protected AbstractComponentBuilder(final @NonNull C component) {
    this.children = new ArrayList<>(component.children());
    if(component.hasStyling()) {
      this.style = component.style().toBuilder();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B append(final @NonNull Component component) {
    if(this.children == AbstractComponent.EMPTY_COMPONENT_LIST) this.children = new ArrayList<>();
    this.children.add(component);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B append(final @NonNull Iterable<? extends Component> components) {
    if(this.children == AbstractComponent.EMPTY_COMPONENT_LIST) this.children = new ArrayList<>();
    components.forEach(this.children::add);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B applyDeep(final @NonNull Consumer<ComponentBuilder<?, ?>> consumer) {
    this.apply(consumer);
    if(this.children == AbstractComponent.EMPTY_COMPONENT_LIST) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while(it.hasNext()) {
      final Component child = it.next();
      if(!(child instanceof BuildableComponent)) {
        continue;
      }
      final ComponentBuilder<?, ?> childBuilder = ((BuildableComponent) child).toBuilder();
      childBuilder.applyDeep(consumer);
      it.set(childBuilder.build());
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public @NonNull B mapChildren(final @NonNull Function<BuildableComponent<? ,?>, BuildableComponent<? ,?>> function) {
    if(this.children == AbstractComponent.EMPTY_COMPONENT_LIST) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while(it.hasNext()) {
      final Component child = it.next();
      if(!(child instanceof BuildableComponent)) {
        continue;
      }
      final BuildableComponent mappedChild = function.apply((BuildableComponent) child);
      if(child == mappedChild) {
        continue;
      }
      it.set(mappedChild);
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public @NonNull B mapChildrenDeep(final @NonNull Function<BuildableComponent<? ,?>, BuildableComponent<? ,?>> function) {
    if(this.children == AbstractComponent.EMPTY_COMPONENT_LIST) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while(it.hasNext()) {
      final Component child = it.next();
      if(!(child instanceof BuildableComponent)) {
        continue;
      }
      final BuildableComponent mappedChild = function.apply((BuildableComponent) child);
      if(mappedChild.children().isEmpty()) {
        if(child == mappedChild) {
          continue;
        }
        it.set(mappedChild);
      } else {
        final ComponentBuilder<?, ?> builder = mappedChild.toBuilder();
        builder.mapChildrenDeep(function);
        it.set(builder.build());
      }
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B color(final @Nullable TextColor color) {
    this.style().color(color);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B colorIfAbsent(final @Nullable TextColor color) {
    this.style().colorIfAbsent(color);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    this.style().decoration(decoration, state);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B clickEvent(final @Nullable ClickEvent event) {
    this.style().clickEvent(event);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B hoverEvent(final @Nullable HoverEvent event) {
    this.style().hoverEvent(event);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B insertion(final @Nullable String insertion) {
    this.style().insertion(insertion);
    return (B) this;
  }

  private Style.@NonNull Builder style() {
    if(this.style == null) {
      this.style = Style.builder();
    }
    return this.style;
  }
}
