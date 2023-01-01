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
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * An abstract implementation of a component builder.
 *
 * @param <C> the component type
 * @param <B> the builder type
 */
abstract class AbstractComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> implements ComponentBuilder<C, B> {
  // We use an empty list by default to prevent unnecessary list creation for components with no children
  protected List<Component> children = Collections.emptyList();
  /*
   * We maintain two separate fields here - a style, and style builder. If we're creating this component builder from
   * another component, or someone provides a style via style(Style), then we don't need a builder - unless someone later
   * calls one of the style modification methods in this builder, at which time we'll convert 'style' to a style builder.
   */
  private @Nullable Style style;
  private Style.@Nullable Builder styleBuilder;

  protected AbstractComponentBuilder() {
  }

  protected AbstractComponentBuilder(final @NotNull C component) {
    final List<Component> children = component.children();
    if (!children.isEmpty()) {
      this.children = new ArrayList<>(children);
    }
    if (component.hasStyling()) {
      this.style = component.style();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B append(final @NotNull Component component) {
    if (component == Component.empty()) return (B) this;
    this.prepareChildren();
    this.children.add(requireNonNull(component, "component"));
    return (B) this;
  }

  @Override
  public @NotNull B append(final @NotNull Component@NotNull... components) {
    return this.append((ComponentLike[]) components);
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B append(final @NotNull ComponentLike@NotNull... components) {
    requireNonNull(components, "components");
    boolean prepared = false;
    for (int i = 0, length = components.length; i < length; i++) {
      final Component component = requireNonNull(components[i], "components[?]").asComponent();
      if (component != Component.empty()) {
        if (!prepared) {
          this.prepareChildren();
          prepared = true;
        }
        this.children.add(requireNonNull(component, "components[?]"));
      }
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B append(final @NotNull Iterable<? extends ComponentLike> components) {
    requireNonNull(components, "components");
    boolean prepared = false;
    for (final ComponentLike like : components) {
      final Component component = requireNonNull(like, "components[?]").asComponent();
      if (component != Component.empty()) {
        if (!prepared) {
          this.prepareChildren();
          prepared = true;
        }
        this.children.add(requireNonNull(component, "components[?]"));
      }
    }
    return (B) this;
  }

  private void prepareChildren() {
    if (this.children == Collections.<Component>emptyList()) {
      this.children = new ArrayList<>();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B applyDeep(final @NotNull Consumer<? super ComponentBuilder<?, ?>> consumer) {
    this.apply(consumer);
    if (this.children == Collections.<Component>emptyList()) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while (it.hasNext()) {
      final Component child = it.next();
      if (!(child instanceof BuildableComponent<?, ?>)) {
        continue;
      }
      final ComponentBuilder<?, ?> childBuilder = ((BuildableComponent<?, ?>) child).toBuilder();
      childBuilder.applyDeep(consumer);
      it.set(childBuilder.build());
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B mapChildren(final @NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
    if (this.children == Collections.<Component>emptyList()) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while (it.hasNext()) {
      final Component child = it.next();
      if (!(child instanceof BuildableComponent<?, ?>)) {
        continue;
      }
      final BuildableComponent<?, ?> mappedChild = requireNonNull(function.apply((BuildableComponent<?, ?>) child), "mappedChild");
      if (child == mappedChild) {
        continue;
      }
      it.set(mappedChild);
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B mapChildrenDeep(final @NotNull Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
    if (this.children == Collections.<Component>emptyList()) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while (it.hasNext()) {
      final Component child = it.next();
      if (!(child instanceof BuildableComponent<?, ?>)) {
        continue;
      }
      final BuildableComponent<?, ?> mappedChild = requireNonNull(function.apply((BuildableComponent<?, ?>) child), "mappedChild");
      if (mappedChild.children().isEmpty()) {
        if (child == mappedChild) {
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
  public @NotNull List<Component> children() {
    return Collections.unmodifiableList(this.children);
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B style(final @NotNull Style style) {
    this.style = style;
    this.styleBuilder = null;
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B style(final @NotNull Consumer<Style.Builder> consumer) {
    consumer.accept(this.styleBuilder());
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B font(final @Nullable Key font) {
    this.styleBuilder().font(font);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B color(final @Nullable TextColor color) {
    this.styleBuilder().color(color);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B colorIfAbsent(final @Nullable TextColor color) {
    this.styleBuilder().colorIfAbsent(color);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B decoration(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
    this.styleBuilder().decoration(decoration, state);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B decorationIfAbsent(final @NotNull TextDecoration decoration, final TextDecoration.@NotNull State state) {
    this.styleBuilder().decorationIfAbsent(decoration, state);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B clickEvent(final @Nullable ClickEvent event) {
    this.styleBuilder().clickEvent(event);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B hoverEvent(final @Nullable HoverEventSource<?> source) {
    this.styleBuilder().hoverEvent(source);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B insertion(final @Nullable String insertion) {
    this.styleBuilder().insertion(insertion);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B mergeStyle(final @NotNull Component that, final @NotNull Set<Style.Merge> merges) {
    this.styleBuilder().merge(requireNonNull(that, "component").style(), merges);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull B resetStyle() {
    this.style = null;
    this.styleBuilder = null;
    return (B) this;
  }

  private Style.@NotNull Builder styleBuilder() {
    if (this.styleBuilder == null) {
      if (this.style != null) {
        this.styleBuilder = this.style.toBuilder();
        this.style = null;
      } else {
        this.styleBuilder = Style.style();
      }
    }
    return this.styleBuilder;
  }

  protected final boolean hasStyle() {
    return this.styleBuilder != null || this.style != null;
  }

  protected @NotNull Style buildStyle() {
    if (this.styleBuilder != null) {
      return this.styleBuilder.build();
    } else if (this.style != null) {
      return this.style;
    } else {
      return Style.empty();
    }
  }
}
