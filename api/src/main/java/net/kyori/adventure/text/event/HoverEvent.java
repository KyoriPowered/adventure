/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text.event;

import java.util.Objects;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.util.Index;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * A hover event.
 *
 * <p>A hover event displays a {@link HoverEvent#value component} when hovered
 * over by a mouse on the client.</p>
 *
 * @param <V> the value type
 * @since 4.0.0
 */
public final class HoverEvent<V> implements Examinable, HoverEventSource<V>, StyleBuilderApplicable {
  /**
   * Creates a hover event that shows text on hover.
   *
   * @param text the text to show on hover
   * @return a hover event
   * @since 4.2.0
   */
  public static @NonNull HoverEvent<Component> showText(final @NonNull ComponentLike text) {
    return showText(text.asComponent());
  }

  /**
   * Creates a hover event that shows text on hover.
   *
   * @param text the text to show on hover
   * @return a hover event
   * @since 4.0.0
   */
  public static @NonNull HoverEvent<Component> showText(final @NonNull Component text) {
    return new HoverEvent<>(Action.SHOW_TEXT, text);
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @return a hover event
   * @since 4.0.0
   */
  public static @NonNull HoverEvent<ShowItem> showItem(final @NonNull Key item, final @NonNegative int count) {
    return showItem(item, count, null);
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @return a hover event
   * @since 4.6.0
   */
  public static @NonNull HoverEvent<ShowItem> showItem(final @NonNull Keyed item, final @NonNegative int count) {
    return showItem(item, count, null);
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @param nbt the nbt
   * @return a hover event
   * @since 4.0.0
   */
  public static @NonNull HoverEvent<ShowItem> showItem(final @NonNull Key item, final @NonNegative int count, final @Nullable BinaryTagHolder nbt) {
    return showItem(ShowItem.of(item, count, nbt));
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @param nbt the nbt
   * @return a hover event
   * @since 4.6.0
   */
  public static @NonNull HoverEvent<ShowItem> showItem(final @NonNull Keyed item, final @NonNegative int count, final @Nullable BinaryTagHolder nbt) {
    return showItem(ShowItem.of(item, count, nbt));
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item to show on hover
   * @return a hover event
   * @since 4.0.0
   */
  public static @NonNull HoverEvent<ShowItem> showItem(final @NonNull ShowItem item) {
    return new HoverEvent<>(Action.SHOW_ITEM, item);
  }

  /**
   * Creates a hover event that show information about an entity on hover.
   *
   * <p>In the official <em>Minecraft: Java Edition</em> client, no information will be shown unless the "Advanced tooltips" debug option is enabled.</p>
   *
   * @param type the type
   * @param id the id
   * @return a {@code ShowEntity}
   * @since 4.0.0
   */
  public static @NonNull HoverEvent<ShowEntity> showEntity(final @NonNull Key type, final @NonNull UUID id) {
    return showEntity(type, id, null);
  }

  /**
   * Creates a hover event that show information about an entity on hover.
   *
   * <p>In the official <em>Minecraft: Java Edition</em> client, no information will be shown unless the "Advanced tooltips" debug option is enabled.</p>
   *
   * @param type the type
   * @param id the id
   * @return a {@code ShowEntity}
   * @since 4.6.0
   */
  public static @NonNull HoverEvent<ShowEntity> showEntity(final @NonNull Keyed type, final @NonNull UUID id) {
    return showEntity(type, id, null);
  }

  /**
   * Creates a hover event that show information about an entity on hover.
   *
   * <p>In the official <em>Minecraft: Java Edition</em> client, no information will be shown unless the "Advanced tooltips" debug option is enabled.</p>
   *
   * @param type the type
   * @param id the id
   * @param name the name
   * @return a {@code ShowEntity}
   * @since 4.0.0
   */
  public static @NonNull HoverEvent<ShowEntity> showEntity(final @NonNull Key type, final @NonNull UUID id, final @Nullable Component name) {
    return showEntity(ShowEntity.of(type, id, name));
  }

  /**
   * Creates a hover event that show information about an entity on hover.
   *
   * <p>In the official <em>Minecraft: Java Edition</em> client, no information will be shown unless the "Advanced tooltips" debug option is enabled.</p>
   *
   * @param type the type
   * @param id the id
   * @param name the name
   * @return a {@code ShowEntity}
   * @since 4.6.0
   */
  public static @NonNull HoverEvent<ShowEntity> showEntity(final @NonNull Keyed type, final @NonNull UUID id, final @Nullable Component name) {
    return showEntity(ShowEntity.of(type, id, name));
  }

  /**
   * Creates a hover event that show information about an entity on hover.
   *
   * <p>In the official <em>Minecraft: Java Edition</em> client, no information will be shown unless the "Advanced tooltips" debug option is enabled.</p>
   *
   * @param entity the entity to show on hover
   * @return a hover event
   * @since 4.0.0
   */
  public static @NonNull HoverEvent<ShowEntity> showEntity(final @NonNull ShowEntity entity) {
    return new HoverEvent<>(Action.SHOW_ENTITY, entity);
  }

  /**
   * Creates a hover event.
   *
   * @param action the action
   * @param value the value
   * @param <V> the value type
   * @return a click event
   * @since 4.0.0
   */
  public static <V> @NonNull HoverEvent<V> hoverEvent(final @NonNull Action<V> action, final @NonNull V value) {
    return new HoverEvent<>(action, value);
  }

  private final Action<V> action;
  private final V value;

  private HoverEvent(final @NonNull Action<V> action, final @NonNull V value) {
    this.action = requireNonNull(action, "action");
    this.value = requireNonNull(value, "value");
  }

  /**
   * Gets the hover event action.
   *
   * @return the hover event action
   * @since 4.0.0
   */
  public @NonNull Action<V> action() {
    return this.action;
  }

  /**
   * Gets the hover event value.
   *
   * @return the hover event value
   * @since 4.0.0
   */
  public @NonNull V value() {
    return this.value;
  }

  /**
   * Sets the hover event value.
   *
   * @param value the hover event value
   * @return a hover event
   * @since 4.0.0
   */
  public @NonNull HoverEvent<V> value(final @NonNull V value) {
    return new HoverEvent<>(this.action, value);
  }

  /**
   * Returns a hover event with the value rendered using {@code renderer} when possible.
   *
   * @param renderer the renderer
   * @param context the render context
   * @param <C> the context type
   * @return a hover event
   * @since 4.0.0
   */
  public <C> @NonNull HoverEvent<V> withRenderedValue(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context) {
    final V oldValue = this.value;
    final V newValue = this.action.renderer.render(renderer, context, oldValue);
    if(newValue != oldValue) return new HoverEvent<>(this.action, newValue);
    return this;
  }

  @Override
  public @NonNull HoverEvent<V> asHoverEvent() {
    return this; // i already am a hover event! hehehehe
  }

  @Override
  public @NonNull HoverEvent<V> asHoverEvent(final @NonNull UnaryOperator<V> op) {
    if(op == UnaryOperator.<V>identity()) return this; // nothing to do, can return ourself
    return new HoverEvent<>(this.action, op.apply(this.value));
  }

  @Override
  public void styleApply(final Style.@NonNull Builder style) {
    style.hoverEvent(this);
  }

  @Override
  public boolean equals(final @Nullable Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final HoverEvent<?> that = (HoverEvent<?>) other;
    return this.action == that.action && this.value.equals(that.value);
  }

  @Override
  public int hashCode() {
    int result = this.action.hashCode();
    result = (31 * result) + this.value.hashCode();
    return result;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("action", this.action),
      ExaminableProperty.of("value", this.value)
    );
  }

  @Override
  public String toString() {
    return this.examine(StringExaminer.simpleEscaping());
  }

  /**
   * The value of a {@link Action#SHOW_ITEM show_item} hover event.
   *
   * @since 4.0.0
   */
  public static final class ShowItem implements Examinable {
    private final Key item;
    private final int count;
    private final @Nullable BinaryTagHolder nbt;

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    public static @NonNull ShowItem of(final @NonNull Key item, final @NonNegative int count) {
      return of(item, count, null);
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.6.0
     */
    public static @NonNull ShowItem of(final @NonNull Keyed item, final @NonNegative int count) {
      return of(item, count, null);
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    public static @NonNull ShowItem of(final @NonNull Key item, final @NonNegative int count, final @Nullable BinaryTagHolder nbt) {
      return new ShowItem(requireNonNull(item, "item"), count, nbt);
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.6.0
     */
    public static @NonNull ShowItem of(final @NonNull Keyed item, final @NonNegative int count, final @Nullable BinaryTagHolder nbt) {
      return new ShowItem(requireNonNull(item, "item").key(), count, nbt);
    }

    private ShowItem(final @NonNull Key item, final @NonNegative int count, final @Nullable BinaryTagHolder nbt) {
      this.item = item;
      this.count = count;
      this.nbt = nbt;
    }

    /**
     * Gets the item.
     *
     * @return the item
     * @since 4.0.0
     */
    public @NonNull Key item() {
      return this.item;
    }

    /**
     * Sets the item.
     *
     * @param item the item
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    public @NonNull ShowItem item(final @NonNull Key item) {
      if(requireNonNull(item, "item").equals(this.item)) return this;
      return new ShowItem(item, this.count, this.nbt);
    }

    /**
     * Gets the count.
     *
     * @return the count
     * @since 4.0.0
     */
    public @NonNegative int count() {
      return this.count;
    }

    /**
     * Sets the count.
     *
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    public @NonNull ShowItem count(final @NonNegative int count) {
      if(count == this.count) return this;
      return new ShowItem(this.item, count, this.nbt);
    }

    /**
     * Gets the nbt.
     *
     * @return the nbt
     * @since 4.0.0
     */
    public @Nullable BinaryTagHolder nbt() {
      return this.nbt;
    }

    /**
     * Sets the nbt.
     *
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    public @NonNull ShowItem nbt(final @Nullable BinaryTagHolder nbt) {
      if(Objects.equals(nbt, this.nbt)) return this;
      return new ShowItem(this.item, this.count, nbt);
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if(this == other) return true;
      if(other == null || this.getClass() != other.getClass()) return false;
      final ShowItem that = (ShowItem) other;
      return this.item.equals(that.item) && this.count == that.count && Objects.equals(this.nbt, that.nbt);
    }

    @Override
    public int hashCode() {
      int result = this.item.hashCode();
      result = (31 * result) + Integer.hashCode(this.count);
      result = (31 * result) + Objects.hashCode(this.nbt);
      return result;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("item", this.item),
        ExaminableProperty.of("count", this.count),
        ExaminableProperty.of("nbt", this.nbt)
      );
    }

    @Override
    public String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }
  }

  /**
   * The value of a {@link Action#SHOW_ENTITY show_entity} hover event.
   *
   * @since 4.0.0
   */
  public static final class ShowEntity implements Examinable {
    private final Key type;
    private final UUID id;
    private final Component name;

    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    public static @NonNull ShowEntity of(final @NonNull Key type, final @NonNull UUID id) {
      return of(type, id, null);
    }

    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @return a {@code ShowEntity}
     * @since 4.6.0
     */
    public static @NonNull ShowEntity of(final @NonNull Keyed type, final @NonNull UUID id) {
      return of(type, id, null);
    }

    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @param name the name
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    public static @NonNull ShowEntity of(final @NonNull Key type, final @NonNull UUID id, final @Nullable Component name) {
      return new ShowEntity(requireNonNull(type, "type"), requireNonNull(id, "id"), name);
    }

    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @param name the name
     * @return a {@code ShowEntity}
     * @since 4.6.0
     */
    public static @NonNull ShowEntity of(final @NonNull Keyed type, final @NonNull UUID id, final @Nullable Component name) {
      return new ShowEntity(requireNonNull(type, "type").key(), requireNonNull(id, "id"), name);
    }

    private ShowEntity(final @NonNull Key type, final @NonNull UUID id, final @Nullable Component name) {
      this.type = type;
      this.id = id;
      this.name = name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     * @since 4.0.0
     */
    public @NonNull Key type() {
      return this.type;
    }

    /**
     * Sets the type.
     *
     * @param type the type
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    public @NonNull ShowEntity type(final @NonNull Key type) {
      if(requireNonNull(type, "type").equals(this.type)) return this;
      return new ShowEntity(type, this.id, this.name);
    }

    /**
     * Sets the type.
     *
     * @param type the type
     * @return a {@code ShowEntity}
     * @since 4.6.0
     */
    public @NonNull ShowEntity type(final @NonNull Keyed type) {
      return this.type(requireNonNull(type, "type").key());
    }

    /**
     * Gets the id.
     *
     * @return the id
     * @since 4.0.0
     */
    public @NonNull UUID id() {
      return this.id;
    }

    /**
     * Sets the id.
     *
     * @param id the id
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    public @NonNull ShowEntity id(final @NonNull UUID id) {
      if(requireNonNull(id).equals(this.id)) return this;
      return new ShowEntity(this.type, id, this.name);
    }

    /**
     * Gets the name.
     *
     * @return the name
     * @since 4.0.0
     */
    public @Nullable Component name() {
      return this.name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    public @NonNull ShowEntity name(final @Nullable Component name) {
      if(Objects.equals(name, this.name)) return this;
      return new ShowEntity(this.type, this.id, name);
    }

    @Override
    public boolean equals(final @Nullable Object other) {
      if(this == other) return true;
      if(other == null || this.getClass() != other.getClass()) return false;
      final ShowEntity that = (ShowEntity) other;
      return this.type.equals(that.type) && this.id.equals(that.id) && Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
      int result = this.type.hashCode();
      result = (31 * result) + this.id.hashCode();
      result = (31 * result) + Objects.hashCode(this.name);
      return result;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("type", this.type),
        ExaminableProperty.of("id", this.id),
        ExaminableProperty.of("name", this.name)
      );
    }

    @Override
    public String toString() {
      return this.examine(StringExaminer.simpleEscaping());
    }
  }

  /**
   * An enumeration of hover event actions.
   *
   * @since 4.0.0
   */
  public static final class Action<V> {
    /**
     * Shows a {@link Component} when hovered over.
     *
     * @since 4.0.0
     */
    public static final Action<Component> SHOW_TEXT = new Action<>("show_text", Component.class, true, new Renderer<Component>() {
      @Override
      public <C> @NonNull Component render(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context, final @NonNull Component value) {
        return renderer.render(value, context);
      }
    });
    /**
     * Shows an item instance when hovered over.
     *
     * @since 4.0.0
     */
    public static final Action<ShowItem> SHOW_ITEM = new Action<>("show_item", ShowItem.class, true, new Renderer<ShowItem>() {
      @Override
      public <C> @NonNull ShowItem render(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context, final @NonNull ShowItem value) {
        return value;
      }
    });
    /**
     * Shows an entity when hovered over.
     *
     * @since 4.0.0
     */
    public static final Action<ShowEntity> SHOW_ENTITY = new Action<>("show_entity", ShowEntity.class, true, new Renderer<ShowEntity>() {
      @Override
      public <C> @NonNull ShowEntity render(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context, final @NonNull ShowEntity value) {
        if(value.name == null) return value;
        return value.name(renderer.render(value.name, context));
      }
    });

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    public static final Index<String, Action<?>> NAMES = Index.create(constant -> constant.name, SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY);
    private final String name;
    private final Class<V> type;
    /**
     * If this action is readable.
     *
     * <p>When an action is not readable it will not be deserialized.</p>
     */
    private final boolean readable;
    private final Renderer<V> renderer;

    Action(final String name, final Class<V> type, final boolean readable, final Renderer<V> renderer) {
      this.name = name;
      this.type = type;
      this.readable = readable;
      this.renderer = renderer;
    }

    /**
     * Gets the value type.
     *
     * @return the value type
     * @since 4.0.0
     */
    public @NonNull Class<V> type() {
      return this.type;
    }

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     *     action is not readable
     * @since 4.0.0
     */
    public boolean readable() {
      return this.readable;
    }

    @Override
    public @NonNull String toString() {
      return this.name;
    }

    @FunctionalInterface
    interface Renderer<V> {
      <C> @NonNull V render(final @NonNull ComponentRenderer<C> renderer, final @NonNull C context, final @NonNull V value);
    }
  }
}
