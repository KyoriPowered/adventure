/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
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

import com.google.common.collect.Iterables;
import net.kyori.blizzard.NonNull;
import net.kyori.blizzard.Nullable;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract implementation of a buildable component.
 *
 * @param <C> the component type
 * @param <B> the builder type
 */
public abstract class AbstractBuildableComponent<C extends BuildableComponent<C, B>, B extends AbstractBuildableComponent.AbstractBuilder<C, B>> extends AbstractComponent implements BuildableComponent<C, B> {

  protected AbstractBuildableComponent(@NonNull final B builder) {
    super(builder.children, builder.color, builder.obfuscated, builder.bold, builder.strikethrough, builder.underlined, builder.italic, builder.clickEvent, builder.hoverEvent, builder.insertion);
  }

  protected AbstractBuildableComponent(@NonNull final List<Component> children, @Nullable final TextColor color, @NonNull final TextDecoration.State obfuscated, @NonNull final TextDecoration.State bold, @NonNull final TextDecoration.State strikethrough, @NonNull final TextDecoration.State underlined, @NonNull final TextDecoration.State italic, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent hoverEvent, @Nullable final String insertion) {
    super(children, color, obfuscated, bold, strikethrough, underlined, italic, clickEvent, hoverEvent, insertion);
  }

  /**
   * An abstract implementation of a component builder.
   *
   * @param <C> the component type
   * @param <B> the builder type
   */
  protected static abstract class AbstractBuilder<C extends BuildableComponent<C, B>, B extends AbstractBuilder<C, B>> implements Builder<C, B> {

    /**
     * The list of children.
     *
     * <p>This list is set to {@link #EMPTY_COMPONENT_LIST an empty list of components}
     * by default to prevent unnecessary list creation for components with no children.</p>
     */
    @NonNull protected List<Component> children = EMPTY_COMPONENT_LIST;
    /**
     * The color of this component.
     */
    @Nullable protected TextColor color;
    /**
     * If this component should have the {@link TextDecoration#OBFUSCATED obfuscated} decoration.
     */
    @NonNull protected TextDecoration.State obfuscated = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#BOLD bold} decoration.
     */
    @NonNull protected TextDecoration.State bold = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#STRIKETHROUGH strikethrough} decoration.
     */
    @NonNull protected TextDecoration.State strikethrough = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#UNDERLINE underlined} decoration.
     */
    @NonNull protected TextDecoration.State underlined = TextDecoration.State.NOT_SET;
    /**
     * If this component should have the {@link TextDecoration#ITALIC italic} decoration.
     */
    @NonNull protected TextDecoration.State italic = TextDecoration.State.NOT_SET;
    /**
     * The click event to apply to this component.
     */
    @Nullable protected ClickEvent clickEvent;
    /**
     * The hover event to apply to this component.
     */
    @Nullable protected HoverEvent hoverEvent;
    /**
     * The string to insert when this component is shift-clicked in chat.
     */
    @Nullable protected String insertion;

    protected AbstractBuilder() {
    }

    protected AbstractBuilder(@NonNull final C component) {
      this.children = new ArrayList<>(component.children());
      this.color = component.color();
      this.obfuscated = component.decoration(TextDecoration.OBFUSCATED);
      this.bold = component.decoration(TextDecoration.BOLD);
      this.strikethrough = component.decoration(TextDecoration.STRIKETHROUGH);
      this.underlined = component.decoration(TextDecoration.UNDERLINE);
      this.italic = component.decoration(TextDecoration.ITALIC);
      this.clickEvent = Optional.ofNullable(component.clickEvent()).map(ClickEvent::copy).orElse(null);
      this.hoverEvent = Optional.ofNullable(component.hoverEvent()).map(HoverEvent::copy).orElse(null);
      this.insertion = component.insertion();
    }

    @NonNull
    @Override
    public B append(@NonNull final Component component) {
      if(this.children == EMPTY_COMPONENT_LIST) this.children = new ArrayList<>();
      this.children.add(component);
      return (B) this;
    }

    @NonNull
    @Override
    public B append(@NonNull final Iterable<? extends Component> components) {
      if(this.children == EMPTY_COMPONENT_LIST) this.children = new ArrayList<>();
      Iterables.addAll(this.children, components);
      return (B) this;
    }

    @NonNull
    @Override
    public B applyDeep(@NonNull final Consumer<Builder<?, ?>> consumer) {
      this.apply(consumer);
      if(this.children == EMPTY_COMPONENT_LIST) {
        return (B) this;
      }
      final ListIterator<Component> it = this.children.listIterator();
      while(it.hasNext()) {
        final Component child = it.next();
        if(!(child instanceof BuildableComponent)) {
          continue;
        }
        final Builder<?, ?> childBuilder = ((BuildableComponent) child).toBuilder();
        childBuilder.applyDeep(consumer);
        it.set(childBuilder.build());
      }
      return (B) this;
    }

    @NonNull
    @Override
    public B mapChildren(@NonNull final Function<BuildableComponent<? ,?>, BuildableComponent<? ,?>> function) {
      if(this.children == EMPTY_COMPONENT_LIST) {
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

    @NonNull
    @Override
    public B mapChildrenDeep(@NonNull final Function<BuildableComponent<? ,?>, BuildableComponent<? ,?>> function) {
      if(this.children == EMPTY_COMPONENT_LIST) {
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
          final Builder<?, ?> builder = mappedChild.toBuilder();
          builder.mapChildrenDeep(function);
          it.set(builder.build());
        }
      }
      return (B) this;
    }

    @NonNull
    @Override
    public B color(@Nullable final TextColor color) {
      this.color = color;
      return (B) this;
    }

    @NonNull
    @Override
    public B decoration(@NonNull final TextDecoration decoration, @NonNull final TextDecoration.State state) {
      switch(decoration) {
        case BOLD: this.bold = checkNotNull(state, "flag"); return (B) this;
        case ITALIC: this.italic = checkNotNull(state, "flag"); return (B) this;
        case UNDERLINE: this.underlined = checkNotNull(state, "flag"); return (B) this;
        case STRIKETHROUGH: this.strikethrough = checkNotNull(state, "flag"); return (B) this;
        case OBFUSCATED: this.obfuscated = checkNotNull(state, "flag"); return (B) this;
        default: throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
      }
    }

    @NonNull
    @Override
    public B clickEvent(@Nullable final ClickEvent event) {
      this.clickEvent = event;
      return (B) this;
    }

    @NonNull
    @Override
    public B hoverEvent(@Nullable final HoverEvent event) {
      this.hoverEvent = event;
      return (B) this;
    }

    @NonNull
    @Override
    public B insertion(@Nullable final String insertion) {
      this.insertion = insertion;
      return (B) this;
    }
  }
}
