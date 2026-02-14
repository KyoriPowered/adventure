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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.UnaryOperator;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.util.Index;
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
@SuppressWarnings("ClassCanBeRecord") // We need a private constructor.
public final class HoverEvent<V> implements HoverEventSource<V>, StyleBuilderApplicable {
  /**
   * Creates a hover event that shows text on hover.
   *
   * @param text the text to show on hover
   * @return a hover event
   * @since 4.2.0
   */
  public static HoverEvent<Component> showText(final ComponentLike text) {
    return showText(text.asComponent());
  }

  /**
   * Creates a hover event that shows text on hover.
   *
   * @param text the text to show on hover
   * @return a hover event
   * @since 4.0.0
   */
  public static HoverEvent<Component> showText(final Component text) {
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
  public static HoverEvent<ShowItem> showItem(final Key item, @Range(from = 0, to = Integer.MAX_VALUE) final int count) {
    return showItem(item, count, Map.of());
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @return a hover event
   * @since 4.6.0
   */
  public static HoverEvent<ShowItem> showItem(final Keyed item, @Range(from = 0, to = Integer.MAX_VALUE) final int count) {
    return showItem(item, count, Map.of());
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item
   * @param count the count
   * @param nbt the nbt
   * @return a hover event
   * @since 4.0.0
   * @obsoleteSinceMinecraft obsolete since 1.20.5 and replaced with data components
   */
  @ApiStatus.Obsolete
  public static HoverEvent<ShowItem> showItem(final Key item, @Range(from = 0, to = Integer.MAX_VALUE) final int count, final @Nullable BinaryTagHolder nbt) {
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
   * @obsoleteSinceMinecraft obsolete since 1.20.5 and replaced with data components
   */
  @ApiStatus.Obsolete
  public static HoverEvent<ShowItem> showItem(final Keyed item, @Range(from = 0, to = Integer.MAX_VALUE) final int count, final @Nullable BinaryTagHolder nbt) {
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
  public static HoverEvent<ShowItem> showItem(final Keyed item, @Range(from = 0, to = Integer.MAX_VALUE) final int count, final Map<Key, ? extends DataComponentValue> dataComponents) {
    return showItem(ShowItem.showItem(item, count, dataComponents));
  }

  /**
   * Creates a hover event that shows an item on hover.
   *
   * @param item the item to show on hover
   * @return a hover event
   * @since 4.0.0
   */
  public static HoverEvent<ShowItem> showItem(final ShowItem item) {
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
  public static HoverEvent<ShowEntity> showEntity(final Key type, final UUID id) {
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
  public static HoverEvent<ShowEntity> showEntity(final Keyed type, final UUID id) {
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
  public static HoverEvent<ShowEntity> showEntity(final Key type, final UUID id, final @Nullable Component name) {
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
  public static HoverEvent<ShowEntity> showEntity(final Keyed type, final UUID id, final @Nullable Component name) {
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
  public static HoverEvent<ShowEntity> showEntity(final ShowEntity entity) {
    return new HoverEvent<>(Action.SHOW_ENTITY, entity);
  }

  /**
   * Creates a hover event that shows an achievement on hover.
   *
   * @param value the achievement value
   * @return a hover event
   * @since 4.14.0
   * @obsoleteSinceMinecraft removed in 1.12
   */
  @ApiStatus.Obsolete
  public static HoverEvent<String> showAchievement(final String value) {
    return new HoverEvent<>(Action.SHOW_ACHIEVEMENT, value);
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
  public static <V> HoverEvent<V> hoverEvent(final Action<V> action, final V value) {
    return new HoverEvent<>(action, value);
  }

  private final Action<V> action;
  private final V value;

  private HoverEvent(final Action<V> action, final V value) {
    this.action = requireNonNull(action, "action");
    this.value = requireNonNull(value, "value");
  }

  /**
   * Gets the hover event action.
   *
   * @return the hover event action
   * @since 4.0.0
   */
  public Action<V> action() {
    return this.action;
  }

  /**
   * Gets the hover event value.
   *
   * @return the hover event value
   * @since 4.0.0
   */
  public V value() {
    return this.value;
  }

  /**
   * Sets the hover event value.
   *
   * @param value the hover event value
   * @return a new hover event with the value set
   * @since 4.0.0
   */
  @Contract(pure = true)
  public HoverEvent<V> value(final V value) {
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
  public <C> HoverEvent<V> withRenderedValue(final ComponentRenderer<C> renderer, final C context) {
    final V oldValue = this.value;
    final V newValue = this.action.renderer().render(renderer, context, oldValue);
    if (newValue != oldValue) return new HoverEvent<>(this.action, newValue);
    return this;
  }

  @Override
  public HoverEvent<V> asHoverEvent() {
    return this; // i already am a hover event! hehehehe
  }

  @Override
  public HoverEvent<V> asHoverEvent(final UnaryOperator<V> op) {
    if (op == UnaryOperator.<V>identity()) return this; // nothing to do, can return ourself
    return new HoverEvent<>(this.action, op.apply(this.value));
  }

  @Override
  public void styleApply(final Style.Builder style) {
    style.hoverEvent(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof HoverEvent<?> that)) return false;
    return this.action.equals(that.action) && this.value.equals(that.value);
  }

  @Override
  public int hashCode() {
    int result = this.action.hashCode();
    result = 31 * result + this.value.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "HoverEvent{" +
      "action=" + this.action +
      ", value=" + this.value +
      '}';
  }

  /**
   * The value of a {@link Action#SHOW_ITEM show_item} hover event.
   *
   * @since 4.0.0
   */
  @SuppressWarnings("ClassCanBeRecord") // We need a private constructor.
  public static final class ShowItem {
    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.14.0
     */
    public static ShowItem showItem(final Key item, final @Range(from = 0, to = Integer.MAX_VALUE) int count) {
      return ShowItem.showItem(item, count, Map.of());
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.14.0
     */
    public static ShowItem showItem(final Keyed item, final @Range(from = 0, to = Integer.MAX_VALUE) int count) {
      return ShowItem.showItem(item, count, Map.of());
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.14.0
     * @obsoleteSinceMinecraft since 1.20.5 and replaced with data components
     */
    @ApiStatus.Obsolete
    public static ShowItem showItem(final Key item, final @Range(from = 0, to = Integer.MAX_VALUE) int count, final @Nullable BinaryTagHolder nbt) {
      return new ShowItem(requireNonNull(item, "item"), count, nbt, Map.of());
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.14.0
     * @deprecated since 1.20.5 and replaced with data components
     */
    @ApiStatus.Obsolete
    public static ShowItem showItem(final Keyed item, final @Range(from = 0, to = Integer.MAX_VALUE) int count, final @Nullable BinaryTagHolder nbt) {
      return new ShowItem(requireNonNull(item, "item").key(), count, nbt, Map.of());
    }

    /**
     * Creates.
     *
     * @param item the item
     * @param count the count
     * @param dataComponents the data components
     * @return a {@code ShowItem}
     * @since 4.17.0
     * @sinceMinecraft 1.20.5
     */
    public static ShowItem showItem(final Keyed item, final @Range(from = 0, to = Integer.MAX_VALUE) int count, final Map<Key, ? extends DataComponentValue> dataComponents) {
      return new ShowItem(requireNonNull(item, "item").key(), count, null, Map.copyOf(requireNonNull(dataComponents, "dataComponents")));
    }

    private final Key item;
    private final int count;
    private final @Nullable BinaryTagHolder nbt;
    private final Map<Key, DataComponentValue> dataComponents;

    private ShowItem(final Key item, final int count, final @Nullable BinaryTagHolder nbt, final Map<Key, DataComponentValue> dataComponents) {
      this.item = item;
      this.count = count;
      this.nbt = nbt;
      this.dataComponents = dataComponents;
    }

    /**
     * Gets the item.
     *
     * @return the item
     * @since 4.0.0
     */
    public Key item() {
      return this.item;
    }

    /**
     * Sets the item.
     *
     * @param item the item
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    public ShowItem item(final Key item) {
      if (requireNonNull(item, "item").equals(this.item)) return this;
      return new ShowItem(item, this.count, this.nbt, this.dataComponents);
    }

    /**
     * Gets the count.
     *
     * @return the count
     * @since 4.0.0
     */
    public @Range(from = 0, to = Integer.MAX_VALUE) int count() {
      return this.count;
    }

    /**
     * Sets the count.
     *
     * @param count the count
     * @return a {@code ShowItem}
     * @since 4.0.0
     */
    public ShowItem count(final @Range(from = 0, to = Integer.MAX_VALUE) int count) {
      if (count == this.count) return this;
      return new ShowItem(this.item, count, this.nbt, this.dataComponents);
    }

    /**
     * Gets the nbt.
     *
     * <p>If there are data components on this item, it will never have NBT data.</p>
     *
     * @return the nbt
     * @since 4.0.0
     * @obsoleteSinceMinecraft since 1.20.5 and replaced with data components
     */
    @ApiStatus.Obsolete
    public @Nullable BinaryTagHolder nbt() {
      return this.nbt;
    }

    /**
     * Sets the nbt.
     *
     * <p>This will clear any modern data components set on the item.</p>
     *
     * @param nbt the nbt
     * @return a {@code ShowItem}
     * @since 4.0.0
     * @deprecated since 1.20.5 and replaced with data components
     */
    @ApiStatus.Obsolete
    public ShowItem nbt(final @Nullable BinaryTagHolder nbt) {
      if (Objects.equals(nbt, this.nbt)) return this;
      return new ShowItem(this.item, this.count, nbt, this.dataComponents);
    }

    /**
     * Get the data components used for this item.
     *
     * <p>If there is NBT data on this item, it will never have any data components set.</p>
     *
     * @return an unmodifiable map of data components
     * @since 4.17.0
     * @sinceMinecraft 1.20.5
     */
    public Map<Key, DataComponentValue> dataComponents() {
      return this.dataComponents;
    }

    /**
     * Set the data components used on this item.
     *
     * <p>This will clear any legacy NBT-format data on the item.</p>
     *
     * @param holder the new data components to set
     * @return a show item data object that has the provided components
     * @since 4.17.0
     * @sinceMinecraft 1.20.5
     */
    public ShowItem dataComponents(final Map<Key, DataComponentValue> holder) {
      if (Objects.equals(this.dataComponents, holder)) return this;
      return new ShowItem(this.item, this.count, this.nbt, Map.copyOf(holder));
    }

    /**
     * Return an unmodifiable map of data components coerced to the target type.
     *
     * <p>If there is no converter registered with the {@link DataComponentValueConverterRegistry} for the conversion of a value, a {@link IllegalArgumentException} will be thrown.</p>
     *
     * @param targetType the expected target type
     * @param <V> the new data component value type
     * @return the unmodifiable map
     * @since 4.17.0
     * @sinceMinecraft 1.20.5
     */
    public <V extends DataComponentValue> Map<Key, V> dataComponentsAs(final Class<V> targetType) {
      if (this.dataComponents.isEmpty()) {
        return Map.of();
      } else {
        final Map<Key, V> results = new HashMap<>(this.dataComponents.size());
        for (final Map.Entry<Key, DataComponentValue> entry : this.dataComponents.entrySet()) {
          results.put(entry.getKey(), DataComponentValueConverterRegistry.convert(targetType, entry.getKey(), entry.getValue()));
        }
        return Map.copyOf(results);
      }
    }

    @Override
    public boolean equals(final Object o) {
      if (!(o instanceof ShowItem showItem)) return false;
      return this.count == showItem.count
        && this.item.equals(showItem.item)
        && Objects.equals(this.nbt, showItem.nbt)
        && this.dataComponents.equals(showItem.dataComponents);
    }

    @Override
    public int hashCode() {
      int result = this.item.hashCode();
      result = 31 * result + this.count;
      result = 31 * result + Objects.hashCode(this.nbt);
      result = 31 * result + this.dataComponents.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return "ShowItem{" +
        "item=" + this.item +
        ", count=" + this.count +
        ", nbt=" + this.nbt +
        ", dataComponents=" + this.dataComponents +
        '}';
    }
  }

  /**
   * The value of a {@link Action#SHOW_ENTITY show_entity} hover event.
   *
   * @since 4.0.0
   */
  @SuppressWarnings("ClassCanBeRecord") // We need a private constructor.
  public static final class ShowEntity {
    /**
     * Creates.
     *
     * @param type the type
     * @param id the id
     * @return a {@code ShowEntity}
     * @since 4.14.0
     */
    public static ShowEntity showEntity(final Key type, final UUID id) {
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
    public static ShowEntity showEntity(final Keyed type, final UUID id) {
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
    public static ShowEntity showEntity(final Key type, final UUID id, final @Nullable Component name) {
      return new ShowEntity(requireNonNull(type, "type"), requireNonNull(id, "id"), name);
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
    public static ShowEntity showEntity(final Keyed type, final UUID id, final @Nullable Component name) {
      return new ShowEntity(requireNonNull(type, "type").key(), requireNonNull(id, "id"), name);
    }

    private final Key type;
    private final UUID id;
    private final @Nullable Component name;

    private ShowEntity(final Key type, final UUID id, final @Nullable Component name) {
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
    public Key type() {
      return this.type;
    }

    /**
     * Sets the type.
     *
     * @param type the type
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    public ShowEntity type(final Key type) {
      if (requireNonNull(type, "type").equals(this.type)) return this;
      return new ShowEntity(type, this.id, this.name);
    }

    /**
     * Sets the type.
     *
     * @param type the type
     * @return a {@code ShowEntity}
     * @since 4.6.0
     */
    public ShowEntity type(final Keyed type) {
      return this.type(requireNonNull(type, "type").key());
    }

    /**
     * Gets the id.
     *
     * @return the id
     * @since 4.0.0
     */
    public UUID id() {
      return this.id;
    }

    /**
     * Sets the id.
     *
     * @param id the id
     * @return a {@code ShowEntity}
     * @since 4.0.0
     */
    public ShowEntity id(final UUID id) {
      if (requireNonNull(id).equals(this.id)) return this;
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
    public ShowEntity name(final @Nullable Component name) {
      if (Objects.equals(name, this.name)) return this;
      return new ShowEntity(this.type, this.id, name);
    }

    @Override
    public boolean equals(final Object o) {
      if (!(o instanceof ShowEntity that)) return false;
      return this.type.equals(that.type)
        && this.id.equals(that.id)
        && Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
      int result = this.type.hashCode();
      result = 31 * result + this.id.hashCode();
      result = 31 * result + Objects.hashCode(this.name);
      return result;
    }

    @Override
    public String toString() {
      return "ShowEntity{" +
        "type=" + this.type +
        ", id=" + this.id +
        ", name=" + this.name +
        '}';
    }
  }

  /**
   * An enumeration of hover event actions.
   *
   * @param <V> the value type an action handles
   * @since 4.0.0
   */
  @SuppressWarnings("ClassCanBeRecord") // We need a private constructor.
  public static final class Action<V> {
    /**
     * Shows a {@link Component} when hovered over.
     *
     * @since 4.0.0
     */
    public static final Action<Component> SHOW_TEXT = new Action<>("show_text", Component.class, true, HoverEventRenderers.SHOW_TEXT);
    /**
     * Shows an item instance when hovered over.
     *
     * @since 4.0.0
     */
    public static final Action<ShowItem> SHOW_ITEM = new Action<>("show_item", ShowItem.class, true, HoverEventRenderers.SHOW_ITEM);
    /**
     * Shows an entity when hovered over.
     *
     * @since 4.0.0
     */
    public static final Action<ShowEntity> SHOW_ENTITY = new Action<>("show_entity", ShowEntity.class, true, HoverEventRenderers.SHOW_ENTITY);
    /**
     * Shows a {@link Component} when hovered over.
     *
     * @since 4.14.0
     * @obsoleteSinceMinecraft removed in 1.12
     */
    @ApiStatus.Obsolete
    public static final Action<String> SHOW_ACHIEVEMENT = new Action<>("show_achievement", String.class, true, HoverEventRenderers.SHOW_ACHIEVEMENT);

    /**
     * The name map.
     *
     * @since 4.0.0
     */
    public static final Index<String, HoverEvent.Action<?>> NAMES = Index.create(Action::toString, SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY, SHOW_ACHIEVEMENT);

    private final String name;
    private final Class<V> type;
    private final boolean readable;
    private final Renderer<V> renderer;

    private Action(final String name, final Class<V> type, final boolean readable, final Renderer<V> renderer) {
      this.name = name;
      this.type = type;
      this.readable = readable;
      this.renderer = renderer;
    }

    /**
     * Returns the name of this action.
     *
     * @return the name
     * @since 5.0.0
     */
    public String name() {
      return this.name;
    }

    /**
     * Gets the value type.
     *
     * @return the value type
     * @since 4.0.0
     */
    public Class<V> type() {
      return this.type;
    }

    /**
     * Tests if this action is readable.
     *
     * @return {@code true} if this action is readable, {@code false} if this action is not readable
     * @since 4.0.0
     */
    public boolean readable() {
      return this.readable;
    }

    /**
     * The renderer for this action.
     *
     * @return the renderer
     * @since 5.0.0
     */
    public Renderer<V> renderer() {
      return this.renderer;
    }

    @Override
    public String toString() {
      return this.name;
    }

    /**
     * Type-specific renderer.
     *
     * @param <V> the value type
     * @since 4.0.0
     */
    public sealed interface Renderer<V> permits HoverEventRenderers.ShowAchievement, HoverEventRenderers.ShowEntity, HoverEventRenderers.ShowItem, HoverEventRenderers.ShowText {
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
