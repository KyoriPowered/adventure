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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
  /*
   * We maintain two separate fields here - a style, and style builder. If we're creating this component builder from
   * another component, or someone provides a style via style(Style), then we don't need a builder - unless someone later
   * calls one of the style modification methods in this builder, at which time we'll convert 'style' to a style builder.
   */
  /**
   * The style.
   */
  private @Nullable Style style;
  /**
   * The style builder.
   */
  private Style.@MonotonicNonNull Builder styleBuilder;

  protected AbstractComponentBuilder() {
  }

  protected AbstractComponentBuilder(final @NonNull C component) {
    this.children = new ArrayList<>(component.children());
    if(component.hasStyling()) {
      this.style = component.style();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B append(final @NonNull Component component) {
    this.prepareChildren();
    this.children.add(component);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B append(final @NonNull Component... components) {
    this.prepareChildren();
    Collections.addAll(this.children, components);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B append(final @NonNull Iterable<? extends Component> components) {
    this.prepareChildren();
    components.forEach(this.children::add);
    return (B) this;
  }

  private void prepareChildren() {
    if(this.children == AbstractComponent.EMPTY_COMPONENT_LIST) {
      this.children = new ArrayList<>();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B applyDeep(final @NonNull Consumer<? super ComponentBuilder<?, ?>> consumer) {
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
      final ComponentBuilder<?, ?> childBuilder = ((BuildableComponent<?, ?>) child).toBuilder();
      childBuilder.applyDeep(consumer);
      it.set(childBuilder.build());
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public @NonNull B mapChildren(final @NonNull Function<BuildableComponent<? ,?>, ? extends BuildableComponent<? ,?>> function) {
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
  public @NonNull B mapChildrenDeep(final @NonNull Function<BuildableComponent<? ,?>, ? extends BuildableComponent<? ,?>> function) {
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
  public @NonNull B style(final @NonNull Style style) {
    this.style = style;
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B style(final @NonNull Consumer<Style.Builder> consumer) {
    consumer.accept(this.styleBuilder());
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B color(final @Nullable TextColor color) {
    this.styleBuilder().color(color);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B colorIfAbsent(final @Nullable TextColor color) {
    this.styleBuilder().colorIfAbsent(color);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B decoration(final @NonNull TextDecoration decoration, final TextDecoration.@NonNull State state) {
    this.styleBuilder().decoration(decoration, state);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B clickEvent(final @Nullable ClickEvent event) {
    this.styleBuilder().clickEvent(event);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B hoverEvent(final @Nullable HoverEvent event) {
    this.styleBuilder().hoverEvent(event);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B insertion(final @Nullable String insertion) {
    this.styleBuilder().insertion(insertion);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B mergeStyle(final @NonNull Component that, final @NonNull Set<Style.Merge> merges) {
    this.styleBuilder().merge(that.style(), merges);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull B resetStyle() {
    this.style = null;
    this.styleBuilder = null;
    return (B) this;
  }

  private Style.@NonNull Builder styleBuilder() {
    if(this.styleBuilder == null) {
      if(this.style != null) {
        this.styleBuilder = this.style.toBuilder();
        this.style = null;
      } else {
        this.styleBuilder = Style.builder();
      }
    }
    return this.styleBuilder;
  }

  protected final boolean hasStyle() {
    return this.styleBuilder != null || this.style != null;
  }

  protected @NonNull Style buildStyle() {
    if(this.styleBuilder != null) {
      return this.styleBuilder.build();
    } else if(this.style != null) {
      return this.style;
    } else {
      return Style.empty();
    }
  }
}
