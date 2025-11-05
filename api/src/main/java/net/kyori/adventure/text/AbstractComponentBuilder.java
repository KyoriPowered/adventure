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
import net.kyori.adventure.util.ARGBLike;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * An abstract implementation of a component builder.
 *
 * @param <C> the component type
 * @param <B> the builder type
 */
abstract sealed class AbstractComponentBuilder<C extends Component, B extends ComponentBuilder<C, B>> implements ComponentBuilder<C, B> permits AbstractNBTComponentBuilder, KeybindComponentImpl.BuilderImpl, ObjectComponentImpl.BuilderImpl, ScoreComponentImpl.BuilderImpl, SelectorComponentImpl.BuilderImpl, TextComponentImpl.BuilderImpl, TranslatableComponentImpl.BuilderImpl {
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

  protected AbstractComponentBuilder(final C component) {
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
  public B append(final Component component) {
    if (component == Component.empty()) return (B) this;
    this.prepareChildren();
    this.children.add(requireNonNull(component, "component"));
    return (B) this;
  }

  @Override
  public B append(final Component... components) {
    return this.append((ComponentLike[]) components);
  }

  @Override
  @SuppressWarnings("unchecked")
  public B append(final ComponentLike... components) {
    requireNonNull(components, "components");
    boolean prepared = false;
    for (final ComponentLike componentLike : components) {
      final Component component = requireNonNull(componentLike, "components[?]").asComponent();
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
  public B append(final Iterable<? extends ComponentLike> components) {
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
  public B applyDeep(final Consumer<? super ComponentBuilder<?, ?>> consumer) {
    this.apply(consumer);
    if (this.children == Collections.<Component>emptyList()) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while (it.hasNext()) {
      final ComponentBuilder<?, ?> childBuilder = it.next().toBuilder();
      childBuilder.applyDeep(consumer);
      it.set(childBuilder.build());
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B mapChildren(final Function<Component, ? extends Component> function) {
    if (this.children == Collections.<Component>emptyList()) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while (it.hasNext()) {
      final Component child = it.next();
      final Component mappedChild = requireNonNull(function.apply(child), "mappedChild");
      if (child == mappedChild) {
        continue;
      }
      it.set(mappedChild);
    }
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B mapChildrenDeep(final Function<Component, ? extends Component> function) {
    if (this.children == Collections.<Component>emptyList()) {
      return (B) this;
    }
    final ListIterator<Component> it = this.children.listIterator();
    while (it.hasNext()) {
      final Component child = it.next();
      final Component mappedChild = requireNonNull(function.apply(child), "mappedChild");
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
  public List<Component> children() {
    return Collections.unmodifiableList(this.children);
  }

  @Override
  @SuppressWarnings("unchecked")
  public B style(final Style style) {
    this.style = style;
    this.styleBuilder = null;
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B style(final Consumer<Style.Builder> consumer) {
    consumer.accept(this.styleBuilder());
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B font(final @Nullable Key font) {
    this.styleBuilder().font(font);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B color(final @Nullable TextColor color) {
    this.styleBuilder().color(color);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B colorIfAbsent(final @Nullable TextColor color) {
    this.styleBuilder().colorIfAbsent(color);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B shadowColor(final @Nullable ARGBLike argb) {
    this.styleBuilder().shadowColor(argb);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B shadowColorIfAbsent(final @Nullable ARGBLike argb) {
    this.styleBuilder().shadowColorIfAbsent(argb);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B decoration(final TextDecoration decoration, final TextDecoration.State state) {
    this.styleBuilder().decoration(decoration, state);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B decorationIfAbsent(final TextDecoration decoration, final TextDecoration.State state) {
    this.styleBuilder().decorationIfAbsent(decoration, state);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B clickEvent(final @Nullable ClickEvent<?> event) {
    this.styleBuilder().clickEvent(event);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B hoverEvent(final @Nullable HoverEventSource<?> source) {
    this.styleBuilder().hoverEvent(source);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B insertion(final @Nullable String insertion) {
    this.styleBuilder().insertion(insertion);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B mergeStyle(final Component that, final Set<Style.Merge> merges) {
    final Style thatStyle = requireNonNull(that, "that").style();
    if (thatStyle.isEmpty() && merges.isEmpty()) return (B) this;
    this.styleBuilder().merge(thatStyle, merges);
    return (B) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public B resetStyle() {
    this.style = null;
    this.styleBuilder = null;
    return (B) this;
  }

  private Style.Builder styleBuilder() {
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

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  protected final boolean hasStyle() {
    return this.styleBuilder != null || this.style != null;
  }

  protected Style buildStyle() {
    if (this.styleBuilder != null) {
      return this.styleBuilder.build();
    } else if (this.style != null) {
      return this.style;
    } else {
      return Style.empty();
    }
  }
}
