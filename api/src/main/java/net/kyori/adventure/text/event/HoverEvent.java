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
package net.kyori.adventure.text.event;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.util.Index;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

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
public sealed interface HoverEvent<V> extends Examinable, HoverEventSource<V>, StyleBuilderApplicable permits HoverEventImpl {
  /**
   * Creates a hover event that shows text on hover.
   *
   * @param text the text to show on hover
   * @return a hover event
   * @since 4.2.0
   */
  static HoverEvent<Component> showText(ComponentLike text) {
    return showText(text.asComponent());
  }

  /**
   * Creates a hover event that shows text on hover.
   *
   * @param text the text to show on hover
   * @return a hover event
   * @since 4.0.0
   */
  static HoverEvent<Component> showText(Component text) {
    return new HoverEventImpl<>(Action.SHOW_TEXT, text);
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @return a hover event
   * @since 4.0.0
   */
  static HoverEvent<ShowItem> showItem(Key item, @Range(from = 0, to = Integer.MAX_VALUE) int count) {
    return showItem(item, count, Collections.emptyMap());
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @return a hover event
   * @since 4.6.0
   */
  static HoverEvent<ShowItem> showItem(Keyed item, @Range(from = 0, to = Integer.MAX_VALUE) int count) {
    return showItem(item, count, Collections.emptyMap());
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @param nbt the nbt
   * @return a hover event
   * @since 4.0.0
   * @deprecated since Minecraft 1.20.5 and replaced with data components, not scheduled for removal
   */
  @Deprecated
  static HoverEvent<ShowItem> showItem(Key item, @Range(from = 0, to = Integer.MAX_VALUE) int count, @Nullable BinaryTagHolder nbt) {
    return showItem(ShowItem.showItem(item, count, nbt));
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @param nbt the nbt
   * @return a hover event
   * @since 4.6.0
   * @deprecated since Minecraft 1.20.5 and replaced with data components, not scheduled for removal
   */
  @Deprecated
  static HoverEvent<ShowItem> showItem(Keyed item, @Range(from = 0, to = Integer.MAX_VALUE) int count, @Nullable BinaryTagHolder nbt) {
    return showItem(ShowItem.showItem(item, count, nbt));
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @param dataComponents the data components
   * @return a hover event
   * @since 4.17.0
   */
  static HoverEvent<ShowItem> showItem(Keyed item, @Range(from = 0, to = Integer.MAX_VALUE) int count, Map<Key, ? extends DataComponentValue> dataComponents) {
    return showItem(ShowItem.showItem(item, count, dataComponents));
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item to show on hover
   * @return a hover event
   * @since 4.0.0
   */
  static HoverEvent<ShowItem> showItem(ShowItem item) {
    return new HoverEventImpl<>(Action.SHOW_ITEM, item);
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
  static HoverEvent<ShowEntity> showEntity(Key type, UUID id) {
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
  static HoverEvent<ShowEntity> showEntity(Keyed type, UUID id) {
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
  static HoverEvent<ShowEntity> showEntity(Key type, UUID id, @Nullable Component name) {
    return showEntity(ShowEntity.showEntity(type, id, name));
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
  static HoverEvent<ShowEntity> showEntity(Keyed type, UUID id, @Nullable Component name) {
    return showEntity(ShowEntity.showEntity(type, id, name));
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
  static HoverEvent<ShowEntity> showEntity(ShowEntity entity) {
    return new HoverEventImpl<>(Action.SHOW_ENTITY, entity);
  }

  /**
   * Creates a hover event that shows an achievement on hover.
   *
   * @param value the achievement value
   * @return a hover event
   * @since 4.14.0
   * @deprecated Removed in Vanilla 1.12, but we keep it for backwards compatibility
   */
  @Deprecated
  static HoverEvent<String> showAchievement(String value) {
    return new HoverEventImpl<>(Action.SHOW_ACHIEVEMENT, value);
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
  static <V> HoverEvent<V> hoverEvent(Action<V> action, V value) {
    return new HoverEventImpl<>(action, value);
  }

  /**
   * Gets the hover event action.
   *
   * @return the hover event action
   * @since 4.0.0
   */
  Action<V> action();

  /**
   * Gets the hover event value.
   *
   * @return the hover event value
   * @since 4.0.0
   */
  V value();

  /**
   * Sets the hover event value.
   *
   * @param value the hover event value
   * @return a new hover event with the value set
   * @since 4.0.0
   */
  @Contract(pure = true)
  HoverEvent<V> value(final V value);

  /**
   * Returns a hover event with the value rendered using {@code renderer} when possible.
   *
   * @param renderer the renderer
   * @param context the render context
   * @param <C> the context type
   * @return a hover event
   * @since 4.0.0
   */
  <C> HoverEvent<V> withRenderedValue(final ComponentRenderer<C> renderer, final C context);

  /**
   * The value of a {@link Action#SHOW_ITEM show_item} hover event.
   *
   * @since 4.0.0
   */
  sealed interface ShowItem extends Examinable permits HoverEventImpl.ShowItemImpl {
    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.14.0
     */
    static ShowItem showItem(final Key item, final @Range(from = 0, to = Integer.MAX_VALUE) int count) {
      return ShowItem.showItem(item, count, Collections.emptyMap());
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.14.0
     */
    static ShowItem showItem(final Keyed item, final @Range(from = 0, to = Integer.MAX_VALUE) int count) {
      return ShowItem.showItem(item, count, Collections.emptyMap());
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.14.0
     * @deprecated since Minecraft 1.20.5 and replaced with data components, not scheduled for removal
     */
    @Deprecated
    static ShowItem showItem(final Key item, final @Range(from = 0, to = Integer.MAX_VALUE) int count, final @Nullable BinaryTagHolder nbt) {
      return new HoverEventImpl.ShowItemImpl(requireNonNull(item, "item"), count, nbt, Collections.emptyMap());
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.14.0
     * @deprecated since Minecraft 1.20.5 and replaced with data components, not scheduled for removal
     */
    @Deprecated
    static ShowItem showItem(final Keyed item, final @Range(from = 0, to = Integer.MAX_VALUE) int count, final @Nullable BinaryTagHolder nbt) {
      return new HoverEventImpl.ShowItemImpl(requireNonNull(item, "item").key(), count, nbt, Collections.emptyMap());
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @param dataComponents the data components
     * @return a {@code ShowItem}
     * @sinceMinecraft 1.20.5
     * @since 4.17.0
     */
    static ShowItem showItem(final Keyed item, final @Range(from = 0, to = Integer.MAX_VALUE) int count, final Map<Key, ? extends DataComponentValue> dataComponents) {
      return new HoverEventImpl.ShowItemImpl(requireNonNull(item, "item").key(), count, null, Map.copyOf(requireNonNull(dataComponents, "dataComponents")));
    }

    /**
     * Gets the item.
     *
     * @return the item
     * @since 4.0.0
     */
    Key item();

    /**
     * Sets the item.
     *
     * @param item the item
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    ShowItem item(final Key item);

    /**
     * Gets the count.
     *
     * @return the count
     * @since 4.0.0
     */
    @Range(from = 0, to = Integer.MAX_VALUE) int count();

    /**
     * Sets the count.
     *
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    ShowItem count(final @Range(from = 0, to = Integer.MAX_VALUE) int count);

    /**
     * Gets the nbt.
     *
     * <p>If there are data components on this item, it will never have NBT data.</p>
     *
     * @return the nbt
     * @since 4.0.0
     * @deprecated since Minecraft 1.20.5 and replaced with data components, not scheduled for removal
     */
    @Deprecated
    @Nullable BinaryTagHolder nbt();

    /**
     * Sets the nbt.
     *
     * <p>This will clear any modern data components set on the item.</p>
     *
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.0.0
     * @deprecated since Minecraft 1.20.5 and replaced with data components, not scheduled for removal
     */
    @Deprecated
    ShowItem nbt(final @Nullable BinaryTagHolder nbt);

    /**
     * Get the data components used for this item.
     *
     * <p>If there is NBT data on this item, it will never have any data components set.</p>
     *
     * @return an unmodifiable map of data components
     * @sinceMinecraft 1.20.5
     * @since 4.17.0
     */
    Map<Key, DataComponentValue> dataComponents();

    /**
     * Set the data components used on this item.
     *
     * <p>This will clear any legacy NBT-format data on the item.</p>
     *
     * @param holder the new data components to set
     * @return a show item data object that has the provided components
     * @sinceMinecraft 1.20.5
     */
    ShowItem dataComponents(final Map<Key, DataComponentValue> holder);

    /**
     * Return an unmodifiable map of data components coerced to the target type.
     *
     * <p>If there is no converter registered with the {@link DataComponentValueConverterRegistry} for the conversion of a value, a {@link IllegalArgumentException} will be thrown.</p>
     *
     * @param targetType the expected target type
     * @param <V> the new data component value type
     * @return the unmodifiable map
     * @since 4.17.0
     */
    <V extends DataComponentValue> Map<Key, V> dataComponentsAs(final Class<V> targetType);
  }

  /**
   * The value of a {@link Action#SHOW_ENTITY show_entity} hover event.
   *
   * @since 4.0.0
   */
  sealed interface ShowEntity extends Examinable permits HoverEventImpl.ShowEntityImpl {
    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @return a {@code ShowEntity}
     * @since 4.14.0
     */
    static ShowEntity showEntity(final Key type, final UUID id) {
      return ShowEntity.showEntity(type, id, null);
    }

    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @return a {@code ShowEntity}
     * @since 4.14.0
     */
    static ShowEntity showEntity(final Keyed type, final UUID id) {
      return ShowEntity.showEntity(type, id, null);
    }

    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @param name the name
     * @return a {@code ShowEntity}
     * @since 4.14.0
     */
    static ShowEntity showEntity(final Key type, final UUID id, final @Nullable Component name) {
      return new HoverEventImpl.ShowEntityImpl(requireNonNull(type, "type"), requireNonNull(id, "id"), name);
    }

    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @param name the name
     * @return a {@code ShowEntity}
     * @since 4.14.0
     */
    static ShowEntity showEntity(final Keyed type, final UUID id, final @Nullable Component name) {
      return new HoverEventImpl.ShowEntityImpl(requireNonNull(type, "type").key(), requireNonNull(id, "id"), name);
    }

    /**
     * Gets the type.
     *
     * @return the type
     * @since 4.0.0
     */
    Key type();

    /**
     * Sets the type.
     *
     * @param type the type
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    ShowEntity type(final Key type);

    /**
     * Sets the type.
     *
     * @param type the type
     * @return a {@code ShowEntity}
     * @since 4.6.0
     */
    default ShowEntity type(final Keyed type) {
      return this.type(requireNonNull(type, "type").key());
    }

    /**
     * Gets the id.
     *
     * @return the id
     * @since 4.0.0
     */
    UUID id();

    /**
     * Sets the id.
     *
     * @param id the id
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    ShowEntity id(final UUID id);

    /**
     * Gets the name.
     *
     * @return the name
     * @since 4.0.0
     */
    @Nullable Component name();

    /**
     * Sets the name.
     *
     * @param name the name
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    ShowEntity name(final @Nullable Component name);
  }

  /**
   * An enumeration of hover event actions.
   *
   * @param <V> the value type an action handles
   * @since 4.0.0
   */
  sealed interface Action<V> permits HoverEventImpl.ActionImpl {
    /**
     * Shows a {@link Component} when hovered over.
     *
     * @since 4.0.0
     */
    Action<Component> SHOW_TEXT = new HoverEventImpl.ActionImpl<>("show_text", Component.class, true, new Renderer<>() {
      @Override
      public <C> Component render(final ComponentRenderer<C> renderer, final C context, final Component value) {
        return renderer.render(value, context);
      }
    });
    /**
     * Shows an item instance when hovered over.
     *
     * @since 4.0.0
     */
    Action<ShowItem> SHOW_ITEM = new HoverEventImpl.ActionImpl<>("show_item", ShowItem.class, true, new Renderer<>() {
      @Override
      public <C> ShowItem render(final ComponentRenderer<C> renderer, final C context, final ShowItem value) {
        return value;
      }
    });
    /**
     * Shows an entity when hovered over.
     *
     * @since 4.0.0
     */
    Action<ShowEntity> SHOW_ENTITY = new HoverEventImpl.ActionImpl<>("show_entity", ShowEntity.class, true, new Renderer<>() {
      @Override
      public <C> ShowEntity render(final ComponentRenderer<C> renderer, final C context, final ShowEntity value) {
        final Component name = value.name();
        if (name == null) return value;
        return value.name(renderer.render(name, context));
      }
    });
    /**
     * Shows a {@link Component} when hovered over.
     *
     * @since 4.14.0
     * @deprecated Removed in Vanilla 1.12, but we keep it for backwards compat
     */
    @Deprecated
    Action<String> SHOW_ACHIEVEMENT = new HoverEventImpl.ActionImpl<>("show_achievement", String.class, true, new Renderer<>() {
      @Override
      public <C> String render(final ComponentRenderer<C> renderer, final C context, final String value) {
        return value;
      }
    });

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    Index<String, HoverEventImpl.Action<?>> NAMES = Index.create(Action::toString, SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY, SHOW_ACHIEVEMENT);

    /**
     * Gets the value type.
     *
     * @return the value type
     * @since 4.0.0
     */
    Class<V> type();

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this
     * action is not readable
     * @since 4.0.0
     */
    boolean readable();

    /**
     * The renderer for this action.
     *
     * @return the renderer
     * @since 5.0.0
     */
    Renderer<V> renderer();

    /**
     * Type-specific renderer.
     *
     * @param <V> the value type
     * @since 4.0.0
     */
    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Renderer<V> {
      /**
       * Renders a value.
       *
       * @param renderer the renderer
       * @param context the context
       * @param value the value
       * @return the rendered value
       * @param <C> the type of the context
       * @since 4.0.0
       */
      <C> V render(final ComponentRenderer<C> renderer, final C context, final V value);
    }
  }
}
