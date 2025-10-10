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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import static java.util.Objects.requireNonNull;

record HoverEventImpl<V>(Action<V> action, V value) implements HoverEvent<V> {

  public HoverEventImpl(final @NotNull Action<V> action, final @NotNull V value) {
    this.action = requireNonNull(action, "action");
    this.value = requireNonNull(value, "value");
  }

  @Override
  public @NotNull HoverEvent<V> value(final @NotNull V value) {
    return new HoverEventImpl<>(this.action, value);
  }

  /**
   * Returns a hover event with the value rendered using {@code renderer} when possible.
   *
   * @param renderer the renderer
   * @param context  the render context
   * @param <C>      the context type
   * @return a hover event
   * @since 4.0.0
   */
  @Override
  public <C> @NotNull HoverEvent<V> withRenderedValue(final @NotNull ComponentRenderer<C> renderer, final @NotNull C context) {
    final V oldValue = this.value;
    final V newValue = this.action.renderer().render(renderer, context, oldValue);
    if (newValue != oldValue) return new HoverEventImpl<>(this.action, newValue);
    return this;
  }

  @Override
  public @NotNull HoverEvent<V> asHoverEvent() {
    return this; // i already am a hover event! hehehehe
  }

  @Override
  public @NotNull HoverEvent<V> asHoverEvent(final @NotNull UnaryOperator<V> op) {
    if (op == UnaryOperator.<V>identity()) return this; // nothing to do, can return ourself
    return new HoverEventImpl<>(this.action, op.apply(this.value));
  }

  @Override
  public void styleApply(final Style.@NotNull Builder style) {
    style.hoverEvent(this);
  }

  @Override
  public @NotNull String toString() {
    return Internals.toString(this);
  }

  record ShowItemImpl(Key item, int count, BinaryTagHolder nbt, Map<Key, DataComponentValue> dataComponents) implements ShowItem {
    @Override
    public @NotNull ShowItem item(final @NotNull Key item) {
      if (requireNonNull(item, "item").equals(this.item)) return this;
      return new ShowItemImpl(item, this.count, this.nbt, this.dataComponents);
    }

    @Override
    public @NotNull ShowItem count(final @Range(from = 0, to = Integer.MAX_VALUE) int count) {
      if (count == this.count) return this;
      return new ShowItemImpl(this.item, count, this.nbt, this.dataComponents);
    }

    @Override
    public @NotNull ShowItem nbt(final @Nullable BinaryTagHolder nbt) {
      if (Objects.equals(nbt, this.nbt)) return this;
      return new ShowItemImpl(this.item, this.count, nbt, Collections.emptyMap());
    }

    @Override
    public @NotNull ShowItem dataComponents(final @NotNull Map<Key, DataComponentValue> holder) {
      if (Objects.equals(this.dataComponents, holder)) return this;
      return new ShowItemImpl(this.item, this.count, null, holder.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(holder)));
    }

    @Override
    public <V extends DataComponentValue> @NotNull Map<Key, V> dataComponentsAs(final @NotNull Class<V> targetType) {
      if (this.dataComponents.isEmpty()) {
        return Collections.emptyMap();
      } else {
        final Map<Key, V> results = new HashMap<>(this.dataComponents.size());
        for (final Map.Entry<Key, DataComponentValue> entry : this.dataComponents.entrySet()) {
          results.put(entry.getKey(), DataComponentValueConverterRegistry.convert(targetType, entry.getKey(), entry.getValue()));
        }
        return Collections.unmodifiableMap(results);
      }
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("item", this.item),
        ExaminableProperty.of("count", this.count),
        ExaminableProperty.of("nbt", this.nbt),
        ExaminableProperty.of("dataComponents", this.dataComponents)
      );
    }

    @Override
    public @NotNull String toString() {
      return Internals.toString(this);
    }
  }

  record ShowEntityImpl(Key type, UUID id, Component name) implements ShowEntity {
    @Override
    public @NotNull ShowEntity type(final @NotNull Key type) {
      if (requireNonNull(type, "type").equals(this.type)) return this;
      return new ShowEntityImpl(type, this.id, this.name);
    }

    @Override
    public @NotNull ShowEntity id(final @NotNull UUID id) {
      if (requireNonNull(id).equals(this.id)) return this;
      return new ShowEntityImpl(this.type, id, this.name);
    }

    @Override
    public @NotNull ShowEntity name(final @Nullable Component name) {
      if (Objects.equals(name, this.name)) return this;
      return new ShowEntityImpl(this.type, this.id, name);
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("type", this.type),
        ExaminableProperty.of("id", this.id),
        ExaminableProperty.of("name", this.name)
      );
    }

    @Override
    public @NotNull String toString() {
      return Internals.toString(this);
    }
  }

  record ActionImpl<V>(String name, Class<V> type, boolean readable, Renderer<V> renderer) implements Action<V> {
    @Override
    public @NotNull String toString() {
      return this.name;
    }
  }
}
